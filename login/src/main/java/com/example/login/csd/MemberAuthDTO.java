package com.example.login.csd;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.java.Log;

@ToString
@Setter
@Getter
@Log
public class MemberAuthDTO extends User implements OAuth2User{
	
	private String loginId; //username ID에 해당 
	
	private boolean social;
	private String nickname;
	
	private String email;
	
	private Map<String, Object> attr;
	
	public MemberAuthDTO(
			String username,
			String password,
			boolean social,
			Collection<? extends GrantedAuthority> authorities
			) {
		super(username, password, authorities);
		
		this.loginId = username;
		this.social = social;
	}
	
	public MemberAuthDTO(
			String username,
			String password,
			boolean social,
			Collection<? extends GrantedAuthority> authorities,
			Map<String, Object> attr) {

		this(username, password, social, authorities);
		this.attr = attr;
		
	}

	@Override
	public Map<String, Object> getAttributes() {
		
		return this.attr;
	}

	@Override
	public String getName() {
		
		return this.nickname;
	}

	
	
	

}
