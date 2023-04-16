package com.aibiigae1221.cookcook.service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aibiigae1221.cookcook.data.dao.UserRepository;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.exception.UserAlreadyExistsException;
import com.aibiigae1221.cookcook.web.domain.LoginParameters;
import com.aibiigae1221.cookcook.web.domain.SignUpParameters;

@Transactional
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtEncoder jwtEncoder;
	
	
	@Override
	public User addUser(SignUpParameters params) {
		User user = paramsToUserEntity(params);
		if(userRepository.findByEmail(user.getEmail()) != null) {
			throw new UserAlreadyExistsException("해당 이메일이 이미 등록되어 있습니다.", user.getEmail());
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	private User paramsToUserEntity(SignUpParameters params) {
		return new User(params.getEmail(), params.getPassword(), params.getNickname());
	}

	@Override
	public long countAllUsers() {
		return userRepository.count();
	}

	@Override
	public void removeAllUsers() {
		userRepository.deleteAll();
	}

	@Override
	public User loadUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public String login(LoginParameters params) {
		
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
		return jwt;
	}

}
