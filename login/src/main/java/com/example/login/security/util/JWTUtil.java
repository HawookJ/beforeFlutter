package com.example.login.security.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;

public class JWTUtil {

	private final Key accessKey;
	private final Key refreshKey;
	
	public JWTUtil(String accessKey, String refreshKey) {
		this.accessKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
		this.refreshKey = Keys.hmacShaKeyFor(accessKey.getBytes(StandardCharsets.UTF_8));
	}
	
	private long accessExpire = 60*1;
	private long refreshExpire= 60*24*7;
	
	public String generateAccessToken(String nickname, Collection<? extends GrantedAuthority> role )throws InvalidKeyException, UnsupportedEncodingException{
		return Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accessExpire).toInstant()))
				.claim("issuer", "LJ")
				.claim("role", role)
				.claim("nickname", nickname)
				.signWith(accessKey, SignatureAlgorithm.HS256)
				.compact();
	}
	public String generateRefreshToken(String nickname) throws InvalidKeyException, UnsupportedEncodingException{
		return Jwts.builder()
				.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(refreshExpire).toInstant()))
				.claim("issuer", "LJ")
				.signWith(refreshKey, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims validateAccessTokenExtract(String tokenString) {
		String contentValue=null;
		
		Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(tokenString);
		Claims claim = jws.getBody();
		return claim;
	}
	public Claims validateRefreshTokenExtract(String tokenString) {
		
		String contentValue = null;
		
		Jws<Claims> jws = Jwts.parserBuilder()
				.setSigningKey(refreshKey)
				.build()
				.parseClaimsJws(tokenString);
		
		Claims claim =  jws.getBody();
		
		return claim;
		
		
	}
}
