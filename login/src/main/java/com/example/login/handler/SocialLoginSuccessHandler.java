package com.example.login.handler;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.login.csd.MemberAuthDTO;
import com.example.login.csd.UserLoginService;
import com.example.login.security.util.JWTUtil;


import lombok.extern.slf4j.Slf4j;


@Slf4j
public class SocialLoginSuccessHandler implements AuthenticationSuccessHandler {
 private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
 private final PasswordEncoder passwordEncoder;
 private final JWTUtil jwtUtil;
 private final UserLoginService userLoginService;
 
 

public SocialLoginSuccessHandler(PasswordEncoder passwordEncoder, JWTUtil jwtUtil, UserLoginService userLoginService) {
	super();
	this.passwordEncoder = passwordEncoder;
	this.jwtUtil = jwtUtil;
	this.userLoginService = userLoginService;
}
 @Override
 public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
		 Authentication authentication) throws IOException, ServletException{
	//로그인 성공 후 처리 내용
		
			log.info("소셜로그인 성공 후 처리~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			log.info("authentication 객체==="+authentication);
			
			MemberAuthDTO authMember = (MemberAuthDTO) authentication.getPrincipal();
			log.info("authMember==="+authMember);
			
			boolean isSocial = authMember.isSocial();
			log.info("isSocial==="+isSocial);
			
			Map<String, Object> attrs = authMember.getAttr();
			log.info("resAttr==="+attrs);
			
			String nickname = null; //기존 소셜회원의 닉네임
			String snsId = null; 
			
			
			if(attrs.get("kakao_account") != null) {
				//카카오
				snsId = ""+attrs.get("id");
				nickname = (String) ((Map)attrs.get("properties")).get("nickname");
				
			}else {
				//네이버 
				Map map =  (Map) attrs.get("response");
				log.info("map==="+map);
				
				snsId  = (String) map.get("id");
				nickname  = (String) map.get("nickname"); 
				log.info("id==="+snsId);
				log.info("nickname==="+nickname); 
				
			}
			
			if(authMember.getNickname() != null) {
				log.info("닉네임 정보 갱신 ");
				nickname = authMember.getNickname();
			}
			
			
			String accessToken = null;
			String refreshToken = null;
			
			try {
				
				accessToken = jwtUtil.generateAccessToken(nickname, authentication.getAuthorities());
				refreshToken = jwtUtil.generateRefreshToken(nickname);
				log.info("accessToken==="+accessToken);
				log.info("refreshToken==="+refreshToken);
				
				//발급받은 새로운 리프레시토큰으로 업데이트.
				userLoginService.updateRefreshToken(nickname, refreshToken);
				
			} catch(Exception e ) {
				e.printStackTrace();
			}
			
			
			
			if(authMember != null && isSocial) {
				redirectStrategy.sendRedirect(request, response, "http://localhost:3000/oauth/redirect?accessToken="+accessToken+"&refreshToken="+refreshToken);//쿼리로 액세스토큰이랑 리프토큰 같이 반환. -> 프론트 페이지에서 받아서 로컬에 저장. 후 메인으로 이동
			}
			
			
			
		}

	}
