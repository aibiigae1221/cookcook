package com.aibiigae1221.cookcook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.aibiigae1221.cookcook.service.RecipeService;
import com.aibiigae1221.cookcook.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@AutoConfigureMockMvc
@SpringBootTest
public class UserAuthenticationTest {

	private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationTest.class);
	
	private static final String USER_EMAIL = "123abc@gmail.com";
	private static final String USER_PASSWORD = "1234";
	private static final String USER_NICKNAME = "갱훈";

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RecipeService recipeService;
	
	@BeforeEach
	public void initEnvironment() {
		recipeService.removeAllRecipes();
		recipeService.removeAllUploadedImages();
		userService.removeAllUsers();
	}
	
	@Test
	public void loginTest() throws Exception {
		
		signUp(USER_EMAIL, USER_PASSWORD, USER_NICKNAME, status().isOk());
		assertEquals(1, userService.countAllUsers());
		
		String content = login(USER_EMAIL, USER_PASSWORD, status().isOk())
						.andReturn()
						.getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(content);
		String jwt = jsonNode.get("jwt").asText();
		
		assertNotNull(jwt);
		logger.info("jwt:[{}]", jwt);
		
		mvc.perform(get("/restricted") 
				.header("Authorization", "Bearer " + jwt))
		  		.andExpect(status().isOk());
		
		mvc.perform(get("/logout"))
				.andExpect(status().isOk());
	}
	
	private ResultActions login(String email, String password, ResultMatcher resultMatcher) throws Exception {
		MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<String, String>();
		loginParams.put("email", List.of(email));
		loginParams.put("password",List.of(password));
		
		return mvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.params(loginParams))
				.andDo(print())
				.andExpect(resultMatcher);
		
	}

	private void signUp(String email, String password, String nickname, ResultMatcher resultMatcher) throws Exception {
		MultiValueMap<String, String> signInParams = new LinkedMultiValueMap<String, String>();
		signInParams.put("email", List.of(email));
		signInParams.put("password",List.of(password));
		signInParams.put("nickname", List.of(nickname));
		
		mvc.perform(post("/sign-up")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED) 
			.params(signInParams))
			
			.andDo(print()) 
			.andExpect(resultMatcher);
	}

	@Test
	public void signupWithFailedInput() throws Exception {
		signUp("", USER_PASSWORD, USER_NICKNAME, status().isBadRequest());
		
		MultiValueMap<String, String> signInParams = new LinkedMultiValueMap<String, String>();
		// email 안주고 전송해보기
		signInParams.put("password",List.of(USER_PASSWORD));
		signInParams.put("nickname", List.of(USER_NICKNAME));
		
		mvc.perform(post("/sign-up")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED) 
				.params(signInParams))
				.andDo(print()) 
				.andExpect(status().isBadRequest());
		
		signUp("malformedEmail", USER_PASSWORD, USER_NICKNAME, status().isBadRequest());
		
	}
	
	@Test
	public void signUpWithDuplicatedEmail() throws Exception {
		signUp(USER_EMAIL, USER_PASSWORD, USER_NICKNAME, status().isOk());
		signUp(USER_EMAIL, USER_PASSWORD, USER_NICKNAME, status().isConflict());
	}
	
	@Test
	public void loginWithFailedInput() throws Exception {
		
		signUp(USER_EMAIL, USER_PASSWORD, USER_NICKNAME, status().isOk());
		login("malformedEmail", USER_PASSWORD, status().isBadRequest());
		signUp("", USER_PASSWORD, USER_NICKNAME, status().isBadRequest());
		
		
		MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<String, String>();
		// loginParams.put("email", List.of(email));
		// 아이디 누락시키고 로그인 시도
		loginParams.put("password",List.of(USER_PASSWORD));
		
		mvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.params(loginParams))
				.andDo(print())
				.andExpect(status().isBadRequest());
		
		login("worngemail@gmail.com", USER_PASSWORD, status().isForbidden()).andDo(print());
	}
	
	
}
