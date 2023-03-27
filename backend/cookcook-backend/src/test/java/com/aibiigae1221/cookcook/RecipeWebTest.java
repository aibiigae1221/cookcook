package com.aibiigae1221.cookcook;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.aibiigae1221.cookcook.service.RecipeService;
import com.aibiigae1221.cookcook.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
public class RecipeWebTest {

	private static final Logger logger = LoggerFactory.getLogger(RecipeWebTest.class);
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RecipeService recipeService;
	
	private static final String USER_EMAIL = "123abc@gmail.com";
	private static final String USER_PASSWORD = "1234";
	private static final String USER_NICKNAME = "갱훈";
	
	private static final String SAMPLE_IMAGE_PATH = "C:\\Users\\aibii\\OneDrive\\Desktop\\nati-melnychuk-ytbgBYivaNE-unsplash.jpg";
	private static final String SAMPLE_IMAGE_ORIGINAL_FILENAME = "nati-melnychuk-ytbgBYivaNE-unsplash.jpg";
	private static final String SAMPLE_IMAGE_CONTENT_TYPE = "image/jpeg";
		
	@BeforeEach
	public void initEnvironment() {
		recipeService.removeAllRecipes();
		recipeService.removeAllUploadedImages();
		userService.removeAllUsers();
	}
	
	@Test
	public void uploadImageTest() throws Exception {
		String jwt = login();
		
		String testImagePath = "C:\\Users\\aibii\\OneDrive\\Desktop\\nati-melnychuk-ytbgBYivaNE-unsplash.jpg";
		MockMultipartFile file = new MockMultipartFile("image", "nati-melnychuk-ytbgBYivaNE-unsplash.jpg", "image/jpeg", new FileInputStream(new File(testImagePath)));
		
		mvc.perform(multipart("/recipe/upload-image")
						.file(file)
						.header("Authorization", "Bearer " + jwt))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void addNewRecipe() throws Exception {
		String jwt = login();
		
		String mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
		String cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
		String cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
		
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("title", "뿌링클 만들기");
		paramsMap.put("tags", List.of("존맛탱", "치킨"));
		paramsMap.put("commentary", "뿌링클 소개글");
		paramsMap.put("mainImageUrl", mainImageUrl);
		
		List<Object> cookStepList = List.of(
			Map.of("uploadUrl", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
			Map.of("uploadUrl", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
		);
		paramsMap.put("cookStepList", cookStepList);
		
		ObjectMapper jsonMapper = new ObjectMapper();
		String paramsJson = jsonMapper.writeValueAsString(paramsMap);
		
		mvc.perform(post("/recipe/add-new-recipe")
							.header("Authorization", "Bearer " + jwt)
							.contentType(MediaType.APPLICATION_JSON)
							.content(paramsJson))
					.andDo(print())
					.andExpect(status().isOk());
		
	}

	

	public String login() throws Exception {
		MultiValueMap<String, String> signInParams = new LinkedMultiValueMap<String, String>();
		signInParams.put("email", List.of(USER_EMAIL));
		signInParams.put("password",List.of(USER_PASSWORD));
		signInParams.put("nickname", List.of(USER_NICKNAME));
		
		mvc.perform(post("/sign-up")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED) 
			.params(signInParams));
		
		MultiValueMap<String, String> loginParams = new LinkedMultiValueMap<String, String>();
		loginParams.put("email", List.of(USER_EMAIL));
		loginParams.put("password",List.of(USER_PASSWORD));
		
		
		String result = mvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.params(loginParams))
				.andReturn().getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		String jwt = jsonNode.get("jwt").asText();
		return jwt;
	}
	
	
	private String uploadImage(String jwt, String originalFileName, String contentType, String filePath) throws Exception {
		
		MockMultipartFile file = new MockMultipartFile("image", originalFileName, contentType, new FileInputStream(new File(filePath)));
		
		String output = mvc.perform(multipart("/recipe/upload-image")
						.file(file)
						.header("Authorization", "Bearer " + jwt))
					.andReturn().getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(output);
		String imageUrl = jsonNode.get("imageUrl").asText();
		return imageUrl;
	} 
	
	
	
}
