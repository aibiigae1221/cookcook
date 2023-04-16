package com.aibiigae1221.cookcook.service;

import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.web.domain.LoginParameters;
import com.aibiigae1221.cookcook.web.domain.SignUpParameters;

public interface UserService {

	User addUser(SignUpParameters params);

	User loadUserByEmail(String email);
	
	long countAllUsers();

	void removeAllUsers();

	String login(LoginParameters params);

	
}
