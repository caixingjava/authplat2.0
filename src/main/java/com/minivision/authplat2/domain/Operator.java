package com.minivision.authplat2.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.minivision.authplat2.enumeration.Role;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作人员信息实体类
 * @author hughzhao
 * @2017年5月22日
 */
@Entity
@Setter
@Getter
public class Operator extends IdEntity implements UserDetails {

	private static final long serialVersionUID = -7705204229233151335L;
	
	//操作账号所属公司
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private Company company;

	//用户名
	@Column(unique = true)
	private String username;
	//密码
	private String password;
	//账号所属角色
	private int role;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Role.getNameByIndex(role));
		authorities.add(authority);
		if (role != Role.ROLE_USER.getIndex()) {
			authorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
		}
		return authorities;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "Operator [company=" + company + ", username=" + username + ", role=" + role + ", createTime="
				+ createTime + ", updateTime=" + updateTime + "]";
	}
	
}
