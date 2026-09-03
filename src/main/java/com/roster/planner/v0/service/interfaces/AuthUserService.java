package com.roster.planner.v0.service.interfaces;

import com.roster.planner.v0.exception.EmailAlreadyExistsException;
import com.roster.planner.v0.exception.InvalidLoginCredentialsException;
import com.roster.planner.v0.request.LoginRequest;
import com.roster.planner.v0.request.RegisterRequest;
import com.roster.planner.v0.response.LoginResponse;
import com.roster.planner.v0.response.RegisterResponse;

public interface AuthUserService {
	RegisterResponse register(RegisterRequest request, String role) throws EmailAlreadyExistsException;
	
	LoginResponse login(LoginRequest request) throws InvalidLoginCredentialsException;
}