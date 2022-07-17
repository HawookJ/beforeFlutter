package com.example.login.csd;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.login.repository.UserRepo;
import com.example.login.security.util.JWTUtil;

import lombok.extern.java.Log;

@Service
@Log
public class UserLoginServiceImpl implements UserLoginService {
	
	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JWTUtil jwtUtil;


	@Autowired
	public UserLoginServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public int updateRefreshToken(String nickname, String token) {
		//로그인시 새로 발급한 리프레쉬 토큰으로 업데이트
		log.info(nickname+"@@@@@"+token);
		int i = userRepo.updateRefreshToken(token, nickname);
		return i;
	
}
}
