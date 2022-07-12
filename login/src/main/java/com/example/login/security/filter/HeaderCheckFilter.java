package com.example.login.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.login.security.util.JWTUtil;

import io.jsonwebtoken.Claims;
import lombok.extern.java.Log;


	@Log
public class HeaderCheckFilter extends OncePerRequestFilter {
		
		private AntPathMatcher antPathMatcher;
		private String pattern;
		private JWTUtil jwtUtil;
		
		public HeaderCheckFilter(String pattern, JWTUtil jwtUtil) {
			this.antPathMatcher = new AntPathMatcher();
			this.pattern = pattern;
			this.jwtUtil = jwtUtil;
		}
		
		

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException{
			
			if(antPathMatcher.match(pattern, request.getRequestURI())) {
				
				if(request.getRequestURI().equals("/api/login")
						||request.getRequestURI().equals("/api/refresh")
						||request.getRequestURI().equals("/api/find/**")
						||request.getRequestURI().equals("/reg/**")) {
					filterChain.doFilter(request, response);
					return;
				}else {
					
					String authHeader = request.getHeader("Authorization");
					
					if(StringUtils.hasText(authHeader)&& authHeader.startsWith("Bearer")) {
						log.info("authorization Access exist : "+ authHeader);
						log.info(""+(authHeader == null));
						
					try {
						
						Claims claim = jwtUtil.validateAccessTokenExtract(authHeader.substring(7));
					
						String nickname= (String) claim.get("nickname");
						
						Collection<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
						
						List roleList = (List) claim.get("role");
						
						String role = null;
						String username= null;
						Iterator it= roleList.iterator();
						while(it.hasNext()) {
							Map roleMap = (Map) it.next();
							log.info("Iterator result  : "+ roleMap);
							
							role = (String) roleMap.get("authority");
							log.info("Iterator result role  : "+ role);
						
							authoritiesList.add(new SimpleGrantedAuthority(role));
						}
							UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(nickname, null, authoritiesList);
//						
							SecurityContextHolder.getContext().setAuthentication(authToken);
							filterChain.doFilter(request, response);
							
						} catch(Exception e) {

							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							response.setContentType("aplication/json;charset=utf-8");
							
							String message = "PLEASE CHECK TOKEN";
							JSONObject json = new JSONObject();
							json.put("code", "403");
							json.put("message", message);
							
							PrintWriter out = response.getWriter();
							out.print(json);
							return;
							
							
						}
						
					}else {
						log.info("@@@@@@@@@@@@@@@@토큰 검사 필요한데 Authorization 헤더 없는 경우 @@@@@@@@@@@@@@@@@@");
						filterChain.doFilter(request, response);
						
					}	
			}
			
		}
	}
	}
