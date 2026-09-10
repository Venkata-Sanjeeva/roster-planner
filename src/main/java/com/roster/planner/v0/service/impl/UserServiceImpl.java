package com.roster.planner.v0.service.impl;

import com.roster.planner.v0.entity.User;
import com.roster.planner.v0.enums.Roles;
import com.roster.planner.v0.exception.EmailAlreadyExistsException;
import com.roster.planner.v0.exception.InvalidLoginCredentialsException;
import com.roster.planner.v0.exception.UserNotFoundException;
import com.roster.planner.v0.repository.UserRepository;
import com.roster.planner.v0.service.interfaces.UserService;
import com.roster.planner.v0.util.IdentifierGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepo;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public boolean existsByEmail(String email) {
		return userRepo.existsByEmail(email);
	}
	
	@Override
	public User getUserByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with " + email + " not found!"));
    }

    public User getUserByEmpUID(String empUID) {
        return userRepo.findByEmpUID(empUID).orElseThrow(() -> new UserNotFoundException("User with " + empUID + " not found!"));
    }

	@Override
    public User registerUser(String name, String email, String password, Roles role) {

        if (existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email + " already exists in DB");
        }

        User user = new User();

        user.setUserUID(IdentifierGenerator.generate(role.toString()));
        user.setEmpUID(IdentifierGenerator.generate("EMP"));
        user.setName(name);
        user.setEmail(email);

        user.setPassword(passwordEncoder.encode(password));

    	user.setRole(role);

        return userRepo.save(user);
    }
    
    @Override
    public boolean verifyUser(String userEmail, String userPassword) {
        User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new InvalidLoginCredentialsException("Invalid email or password"));
        return passwordEncoder.matches(userPassword, user.getPassword());
    }

    public List<User> saveAllUsers(Iterable<User> usersList) {
        return userRepo.saveAll(usersList);
    }

}
