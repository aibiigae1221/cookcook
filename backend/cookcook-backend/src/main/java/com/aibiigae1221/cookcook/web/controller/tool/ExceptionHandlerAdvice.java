package com.aibiigae1221.cookcook.web.controller.tool;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.aibiigae1221.cookcook.service.exception.RecipeNotFoundException;
import com.aibiigae1221.cookcook.service.exception.UserAlreadyExistsException;
import com.aibiigae1221.cookcook.util.HashMapBean;

import jakarta.validation.ConstraintViolationException;


@RestControllerAdvice
public class ExceptionHandlerAdvice {

//	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
	
	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;
	
	@ExceptionHandler(ConstraintViolationException.class) 
	public ResponseEntity<?> constraintViolationException(ConstraintViolationException e, WebRequest request) {
		String message = e.getMessage();
		message = message.substring(message.indexOf(":")+2);
		HashMapBean map = addErrorIntoMap(message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map.getSource()); 
	}
	
	@ExceptionHandler(RecipeNotFoundException.class) 
	public ResponseEntity<?> recipeNotFoundException(RecipeNotFoundException e, WebRequest request) {
		HashMapBean map = addErrorIntoMap("해당 레시피를 찾을 수 없습니다.");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map.getSource()); 
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class) 
	public ResponseEntity<?> duplicateException(UserAlreadyExistsException e, WebRequest request) {
		HashMapBean map = addErrorIntoMap("해당 이메일은 이미 사용중입니다.");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(map.getSource()); 
	}

	@ExceptionHandler(BindException.class)
	public ResponseEntity<?> handleBindException(BindException e, WebRequest request){
		String errorMessage =  e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		HashMapBean map = addErrorIntoMap(errorMessage);
		
		String fieldName = e.getBindingResult().getFieldError().getField();
		map.put("field", fieldName);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map.getSource());
	}
	
	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<?> handleAuthenticationException(AuthenticationException e, WebRequest request){
		HashMapBean map = addErrorIntoMap("로그인 인증 실패");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map.getSource());
	}
	
	private HashMapBean addErrorIntoMap(String msg) {
		HashMapBean map = hashMapHolderProvider.getObject();
		map.put("status", "error");
		map.put("message", msg);
		return map;
	}
}
