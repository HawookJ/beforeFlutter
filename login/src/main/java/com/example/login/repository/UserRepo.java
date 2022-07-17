package com.example.login.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.login.domain.User;



public interface UserRepo extends JpaRepository<User, Long>{
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE FROM User u set u.refreshToken = :token WHERE u.nickname = :nickname")
	int updateRefreshToken(String token, String nickname);
}
