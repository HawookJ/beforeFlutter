package com.example.login.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.login.security.filter.HeaderCheckFilter;
import com.example.login.security.util.JWTUtil;



public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final UserRepo userRepo;
	
	//JWT signature
	@Value("${jwt.access}")
	private String SECRET_KEY; 
	
	@Value("${jwt.refresh}")
	private String REFRESH_KEY;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http.csrf().disable();
		http.cors();
		http.httpBasic().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.oauth2Login().successHandler(successHandler());
		http.authorizeHttpRequests().antMatchers("/").permitAll();
		
		http.addFilterBefore(checkFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(loginFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
    	
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	  @Bean
	    PasswordEncoder passwordEncoder() {
	    	return new BCryptPasswordEncoder();
	    }
	    @Bean
	    public JWTUtil jwtUtil() {
	    	return new JWTUtil(SECRET_KEY, REFRESH_KEY);
	    }
	    @Bean
	    public HeaderCheckFilter checkFilter() {
	    	
	    	HeaderCheckFilter checkFilter = new HeaderCheckFilter("/**", jwtUtil());
	   	
	    	return checkFilter;
	    }
}
