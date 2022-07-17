package com.example.login.csd;

import com.example.login.repository.UserRepo;

import lombok.extern.java.Log;


public interface UserLoginService {

	int updateRefreshToken(String nickname, String token);	
	
}
