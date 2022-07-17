package com.example.login.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.login.group.MemberRole;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Table(name ="user_info" )
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
public class User implements Serializable{

	@JsonIgnore
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String loginId;
	
	private String password;
	
	private String refreshToken;
	
	private boolean social;
	
	private String name;
	
	@Column(length = 45)
	private String email;
	
	@Column(columnDefinition = "integer default 1")
	private Integer level;
	
	@NotNull
	@Column(unique = true)
	private String nickname;
	
	private String status;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@Builder.Default
	private Set<MemberRole> roleSet = new HashSet<>();
	
	public void addMemberRole(MemberRole memberRole) {
		roleSet.add(memberRole);
	}
	
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
	private Date createDate;
	
	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_date")
	private Date modifyDate;
}
