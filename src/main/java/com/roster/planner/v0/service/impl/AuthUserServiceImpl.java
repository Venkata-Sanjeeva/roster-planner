package com.roster.planner.v0.service.impl;

import com.roster.planner.v0.entity.User;
import com.roster.planner.v0.enums.Roles;
import com.roster.planner.v0.exception.InvalidLoginCredentialsException;
import com.roster.planner.v0.request.LoginRequest;
import com.roster.planner.v0.request.RegisterRequest;
import com.roster.planner.v0.response.LoginResponse;
import com.roster.planner.v0.response.RegisterResponse;
import com.roster.planner.v0.service.interfaces.AuthUserService;
import com.roster.planner.v0.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthUserServiceImpl implements AuthUserService {
	
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtil;
	
    @Override
	public RegisterResponse register(RegisterRequest request, String role) {
    	
    	Roles finalizedRole =  Roles.EMPLOYEE;
    		
    	if(role.toLowerCase().contains("lead")) {
    		finalizedRole = Roles.TEAM_LEAD;
    	} else if(role.toLowerCase().contains("shift")) {
    		finalizedRole = Roles.SHIFT_MANAGER;
    	} else if (role.toLowerCase().equals("manager")){
			finalizedRole = Roles.MANAGER;
		}

		User savedUser = userService.registerUser(request.getName(), request.getEmail(), request.getPassword(), finalizedRole);

        String token = jwtUtil.generateTokenUsingEmail(savedUser.getEmail());

        return RegisterResponse.builder()
        		.email(request.getEmail())
        		.token(token)
        		.build();
    }

    @Override
	public LoginResponse login(LoginRequest request) {
	    
	    if (!userService.verifyUser(request.getEmail(), request.getPassword())) {
	        throw new InvalidLoginCredentialsException("Invalid email or password");
	    }
	    
	    User user = userService.getUserByEmail(request.getEmail());

	    String token = jwtUtil.generateTokenUsingEmailAndRole(user.getEmail(), Roles.valueOf(user.getRole()));

	    return LoginResponse.builder()
	    		.userUID(user.getUserUID())
	            .email(user.getEmail())
	            .token(token)
	            .role(Roles.valueOf(user.getRole()))
	            .build();
	}
}