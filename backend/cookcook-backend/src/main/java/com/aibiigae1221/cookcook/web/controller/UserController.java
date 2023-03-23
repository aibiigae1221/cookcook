package com.aibiigae1221.cookcook.web.controller;


import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.UserService;
import com.aibiigae1221.cookcook.util.HashMapBean;
import com.aibiigae1221.cookcook.web.domain.LoginParameters;
import com.aibiigae1221.cookcook.web.domain.SignUpParameters;

import jakarta.validation.Valid;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtEncoder jwtEncoder;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;
	
	
	@PostMapping("/sign-up")
	public ResponseEntity<?> signUp(@Valid SignUpParameters params) {
		logger.info(params.toString());
		userService.addUser(paramsToUserEntity(params));
		
		HashMapBean map = hashMapHolderProvider.getObject();
		map.put("status", "success");
		
		return ResponseEntity.status(HttpStatus.OK).body(map.getSource());
	}

	@PostMapping("/login") 
	public ResponseEntity<?> login(@Valid LoginParameters params) {
		logger.info("로그인 파라미터 - {}", params.toString());
		
		Instant now = Instant.now(); long expiry = 36000L;
	  
		UserDetails userDetails = userDetailsService.loadUserByUsername(params.getEmail());
		if(!passwordEncoder.matches(params.getPassword(), userDetails.getPassword())) {
			throw new BadCredentialsException("유효하지 않는 로그인 정보입니다.");
		}
		
		JwtClaimsSet claims = JwtClaimsSet.builder() 
								.issuer("self") 
								.issuedAt(now)
								.expiresAt(now.plusSeconds(expiry)) 
								.subject(userDetails.getUsername())
								.claims(map -> {
									Set<String> set = userDetails
														.getAuthorities()
														.stream()
														.map(authority -> authority.getAuthority())
														.collect(Collectors.toSet());
									map.put("scope", set);
								})
								.build();

		String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		HashMapBean map = hashMapHolderProvider.getObject();
		map.put("status", "success");
		map.put("jwt", jwt);
		
		return ResponseEntity.status(HttpStatus.OK).body(map.getSource()); 
	}
	
	@GetMapping("/restricted")
	public void restrictedPage(Authentication authentication) {
		logger.info("restricted page - name:[{}]", authentication.getName());
	}
	
	private User paramsToUserEntity(SignUpParameters params) {
		return new User(params.getEmail(), params.getPassword(), params.getNickname());
	}
}
