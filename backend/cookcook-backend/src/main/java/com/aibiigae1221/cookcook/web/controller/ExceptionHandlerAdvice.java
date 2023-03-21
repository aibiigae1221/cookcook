package com.aibiigae1221.cookcook.web.controller;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.aibiigae1221.cookcook.util.HashMapBean;


@RestControllerAdvice
public class ExceptionHandlerAdvice {

	// private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
	
	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;
	
	@ExceptionHandler(BindException.class)
	public ResponseEntity<?> handleBindException(BindException e, WebRequest request){
		HashMapBean map = hashMapHolderProvider.getObject();
		String errorMessage =  e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		map.put("error", errorMessage);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map.getSource());
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException e, WebRequest request){
		HashMapBean map = hashMapHolderProvider.getObject();
		map.put("error", "bad credential");
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map.getSource());
	}
}
