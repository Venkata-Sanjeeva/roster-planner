package com.roster.planner.v0.response;

import com.roster.planner.v0.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
	private String userUID;
	private String email;
	private String token;
	private Roles role;
}
