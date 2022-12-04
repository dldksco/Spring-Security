package com.side.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String userId;
    private String userPwd;
	@Override
	public String toString() {
		return "LoginDto [userId=" + userId + ", userPwd=" + userPwd + "]";
	}
}