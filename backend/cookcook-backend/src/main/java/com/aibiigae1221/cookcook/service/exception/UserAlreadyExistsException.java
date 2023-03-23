package com.aibiigae1221.cookcook.service.exception;

import org.springframework.dao.DataAccessException;

public class UserAlreadyExistsException extends DataAccessException {

	private static final long serialVersionUID = 1L;
	
	private String email;
	
	public UserAlreadyExistsException(String msg, String email) {
		super(msg);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	

}
