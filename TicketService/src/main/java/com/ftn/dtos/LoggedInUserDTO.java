package com.ftn.dtos;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class LoggedInUserDTO {

	private Long id;
	private String token;
	private String username;
	private String email;
	private Collection<? extends GrantedAuthority> authorities;

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LoggedInUserDTO(Long id, String token, String username, String email,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.token = token;
		this.username = username;
		this.email = email;
		this.authorities = authorities;
	}

	public LoggedInUserDTO() {
		super();
	}
	
	
}
