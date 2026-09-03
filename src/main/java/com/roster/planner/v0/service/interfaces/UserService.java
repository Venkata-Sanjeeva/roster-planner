package com.roster.planner.v0.service.interfaces;

import com.roster.planner.v0.entity.User;
import com.roster.planner.v0.enums.Roles;
import com.roster.planner.v0.exception.EmailAlreadyExistsException;
import com.roster.planner.v0.exception.InvalidLoginCredentialsException;
import com.roster.planner.v0.exception.UserNotFoundException;

public interface UserService {
	User getUserByEmail(String email) throws UserNotFoundException;
	
	User registerUser(String name, String email, String password, Roles role) throws EmailAlreadyExistsException;
	
	boolean existsByEmail(String email);
	
	boolean verifyUser(String email, String password) throws InvalidLoginCredentialsException;
}

