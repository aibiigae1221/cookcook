package com.aibiigae1221.cookcook.security;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.aibiigae1221.cookcook.data.dao.UserRepository;
import com.aibiigae1221.cookcook.data.entity.User;

public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByEmail(username);
		return org.springframework.security.core.userdetails.User
				.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(basicAuthorities())
				.build();
	}

	private Collection<? extends GrantedAuthority> basicAuthorities() {
		return Set.of( new SimpleGrantedAuthority("basic"));
		
	}

}
