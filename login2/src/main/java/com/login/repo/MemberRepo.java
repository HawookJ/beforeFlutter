package com.login.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.login.domain.Member;

public interface MemberRepo extends JpaRepository<Member, Long>{
	Optional<Member> findByEmail(String email);
	boolean existsByEmail(String email);

}
