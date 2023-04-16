package com.aibiigae1221.cookcook.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aibiigae1221.cookcook.service.UserService;
import com.aibiigae1221.cookcook.util.HashMapBean;
import com.aibiigae1221.cookcook.web.controller.tool.ResponseOk;
import com.aibiigae1221.cookcook.web.domain.LoginParameters;
import com.aibiigae1221.cookcook.web.domain.SignUpParameters;

import jakarta.validation.Valid;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;

	@Autowired
	private ResponseOk responseOk;
	
	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@Valid SignUpParameters params) {
		logger.info(params.toString());
		userService.addUser(params);
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		return responseOk.ok(mapHolder);
	}

	@PostMapping("/login") 
	public ResponseEntity<?> login(@Valid LoginParameters params) {
		String jwt = userService.login(params);
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("jwt", jwt);
		return responseOk.ok(mapHolder);
	}
}
