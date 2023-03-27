package com.aibiigae1221.cookcook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aibiigae1221.cookcook.data.dao.UserRepository;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.exception.UserAlreadyExistsException;

@Transactional
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public User addUser(User user) {
		
		if(userRepository.findByEmail(user.getEmail()) != null) {
			throw new UserAlreadyExistsException("해당 이메일이 이미 등록되어 있습니다.", user.getEmail());
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
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

}
