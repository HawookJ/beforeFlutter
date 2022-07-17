package com.example.login.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


import lombok.extern.java.Log;

@Log
public class LoginFailHandler implements AuthenticationFailureHandler{
	
	//인증에 실패하는 경우를 처리 =>SecurityConfig에도 설정 적용.
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		log.info("@@인증실패@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		log.info(exception.getMessage());
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("aplication/json;charset=utf-8");
		
		JSONObject json = new JSONObject();
		json.put("code", "401");
		json.put("message", exception.getMessage());
		
		PrintWriter out = response.getWriter();
		out.print(json);
		
	}

}
