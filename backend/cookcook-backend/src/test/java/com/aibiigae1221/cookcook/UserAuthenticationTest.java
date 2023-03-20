package com.aibiigae1221.cookcook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.aibiigae1221.cookcook.service.UserService;


@AutoConfigureMockMvc
@SpringBootTest
public class UserAuthenticationTest {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
	
	private static final String USER_EMAIL = "123abc@gmail.com";
	private static final String USER_PASSWORD = "1234";
	private static final String USER_NICKNAME = "갱훈";

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserService userService;
	
	@Test
	public void login() throws Exception {
		
		MultiValueMap<String, String> signInParams = new LinkedMultiValueMap<String, String>();
		signInParams.put("email", List.of(USER_EMAIL));
		signInParams.put("password",List.of(USER_PASSWORD));
		signInParams.put("nickname", List.of(USER_NICKNAME));
		
		mvc.perform(post("/sign-up")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED) 
			.params(signInParams))
			.andDo(print()) .andExpect(status().isOk());
		 
		assertEquals(1, userService.countAllUsers());
		
		MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<String, String>();
		loginParams.put("email", List.of(USER_EMAIL));
		loginParams.put("password",List.of(USER_PASSWORD));
		
		String jwt = mvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.params(loginParams))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		assertNotNull(jwt);
		logger.info("jwt: [{}]", jwt);
		
		
		mvc.perform(get("/restricted") 
				.header("Authorization", "Bearer " + jwt))
		  		.andExpect(status().isOk());
		 
	}
}
