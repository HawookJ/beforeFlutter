package com.example.login.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.example.login.csd.MemberAuthDTO;
import com.example.login.csd.UserLoginService;
import com.example.login.security.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;


@Log
public class LoginFilter extends AbstractAuthenticationProcessingFilter{

	private final JWTUtil jwtUtil;
	private final UserLoginService userLoginService;
	
	public LoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil, UserLoginService userLoginService) {
		super(defaultFilterProcessesUrl);
		this.jwtUtil=jwtUtil;
		this.userLoginService=userLoginService;
		// TODO Auto-generated constructor stub
	}
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {


		log.info("@@@@@@@@@@loginFilter@@@@@@@@@@@@@");
		log.info("@@@@@@@@@attemptAuthentication@@@@@@@@@");
		
		 
		String loginId = request.getParameter("loginId");
		String pw = request.getParameter("password");
		log.info(loginId+" ::::::" + pw);
		
		//포스트 방식으로 로그인하고 파라미터는 암호화해서 처리
		
		
		if(loginId == null) {
			throw new BadCredentialsException("아이디가 null입니다. ");
		}
		
		
		
		UsernamePasswordAuthenticationToken authToken2 = new UsernamePasswordAuthenticationToken(loginId, pw, new ArrayList<>());
		
		return getAuthenticationManager().authenticate(authToken2);
		
		
	}
	
	
	
	
	
	
	
	
	
	@Override  //인증 성공시 
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
			Authentication authResult) throws IOException, ServletException {
		
		log.info("@@@@@@@@@@@@@@apilogin=> success@@@@@@@@@@@@@@@@@");
		log.info("authResult:"+authResult);
		log.info("authResult.getPrincipal(): "+authResult.getPrincipal());
		log.info("authResult.getCredentials(): "+authResult.getCredentials());
		
		MemberAuthDTO userDTO = (MemberAuthDTO)authResult.getPrincipal();
		String userName = userDTO.getUsername();
		Object info = authResult.getPrincipal();
		
		
		log.info("info => "+info);
		log.info("MemberAuthDTO => "+userDTO);
		log.info("getUsername() => "+userName);
		
		String accessToken = null;
		String refreshToken = null;
		
		try {
			
			accessToken = jwtUtil.generateAccessToken(userDTO.getNickname(), authResult.getAuthorities());
			refreshToken = jwtUtil.generateRefreshToken(userDTO.getNickname());
			
			 Map<String, String> tokens = new HashMap<>();
		        tokens.put("accessToken",accessToken);
		        tokens.put("refreshToken",refreshToken);
			
			response.setContentType("aplication/json;charset=utf-8");
//			response.getOutputStream().write(accessToken.getBytes());
			new ObjectMapper().writeValue(response.getOutputStream(), tokens);
			
			userLoginService.updateRefreshToken(userDTO.getNickname(), refreshToken);//발급받은 새로운 리프레시토큰으로 업데이트.
			
		} catch(Exception e ) {
			e.printStackTrace();
		}
		
	}

}
