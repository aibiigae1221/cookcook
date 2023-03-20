package com.aibiigae1221.cookcook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.UserService;

@SpringBootTest
public class UserServiceTest {

	
	private static final String USER_EMAIL = "123abc@gmail.com";
	private static final String USER_PASSWORD = "1234";
	private static final String USER_NICKNAME = "갱훈";
	
	@Autowired
	private UserService userService;
	
	@Test
	public void test() {
		User user = new User(USER_EMAIL, USER_PASSWORD, USER_NICKNAME);
		
		User savedUser = userService.addUser(user);
		assertNotNull(savedUser);
		assertEquals(savedUser.getEmail(), USER_EMAIL);
	}
}
