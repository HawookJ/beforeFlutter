//package com.example.login.provider;
//
//
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//
//@Component
//public class JwtTokenProvider {
//
//	@Value("${jwt.token.secret-key}")
//	private String secret_key;
//	
//	@Value("${jwt.token.expire-length}")
//	private long expire_time;
//	
//	@Autowired
//	private UserDetailsService userDetailsService;
//	
// 
//    Key key = Keys.hmacShaKeyFor(secret_key.getBytes(StandardCharsets.UTF_8));
//	
//	
//	public String generateToken(Authentication authentication) {
//		Claims claims = Jwts.claims().setSubject(authentication.getName());
//		
//		Date now = new Date();
//		Date expiresIn = new Date(now.getTime()+ expire_time);
//		
//		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expiresIn).signWith(key, SignatureAlgorithm.HS256).compact();
//	}
//	public Authentication getAuthentication(String token) {
//		String username= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
//		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//		
//	    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//	}
//	
//	public String resolveToken(HttpServletRequest request) {
//		String bearerToken = request.getHeader("Authorization");
//		if(bearerToken !=null && bearerToken.startsWith("Bearer")) {
//			return bearerToken.substring(7);
//		}
//		return null;
//	}
//	public boolean validateToken(String token) {
//		try {
//			Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token);
//			return true;
//		}catch(JwtException e) {
//			throw new CustomException("Error on Token", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//}
