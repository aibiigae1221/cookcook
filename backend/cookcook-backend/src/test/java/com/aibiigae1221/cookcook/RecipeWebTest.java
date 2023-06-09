package com.aibiigae1221.cookcook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.aibiigae1221.cookcook.service.RecipeService;
import com.aibiigae1221.cookcook.service.UserService;
import com.aibiigae1221.cookcook.util.CallbackForTest;
import com.aibiigae1221.cookcook.util.MultiValueMapSetterCallback;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@ActiveProfiles("dev")
@AutoConfigureMockMvc
@SpringBootTest
public class RecipeWebTest {

	// private static final Logger logger = LoggerFactory.getLogger(RecipeWebTest.class);
	
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RecipeService recipeService;
	
	private static final String USER_EMAIL = "123abc@gmail.com";
	private static final String USER_PASSWORD = "1234";
	private static final String USER_NICKNAME = "갱훈";
	
	private static final String SAMPLE_IMAGE_PATH = "C:\\Users\\aibii\\OneDrive\\Desktop\\돈가스.PNG";
	private static final String SAMPLE_IMAGE_ORIGINAL_FILENAME = "돈가스.PNG";
	private static final String SAMPLE_IMAGE_CONTENT_TYPE = "image/png";
		
	@BeforeEach
	public void initEnvironment() {
		recipeService.removeAllRecipes();
		recipeService.removeAllUploadedImages();
		userService.removeAllUsers();
	}
	
	
	@Test 
	public void updateRecipeArticle() throws Exception { 
		String jwt = login(); 
		String uuid = addRecipeFixture(jwt, 0); 
		
		String mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("recipeId", uuid);
		paramsMap.put("title", "수정된 제목");
		paramsMap.put("tags", List.of("수정된 태그"));
		paramsMap.put("commentary", "수정된 소개글");
		paramsMap.put("imageFileName", mainImageUrl);
		
		List<Object> cookStepList = List.of(
				Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "수정된 과정1"),
				Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "수정된 과정2")
		);
		paramsMap.put("cookStepList", cookStepList);
		
		ObjectMapper jsonMapper = new ObjectMapper();
		String paramsJson = jsonMapper.writeValueAsString(paramsMap);
		
		mvc.perform(post("/recipe/edit-recipe")
				.header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(paramsJson))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test 
	public void updateRecipeArticleWithWrongInput() throws Exception { 
		String jwt = login(); 
		String uuid = addRecipeFixture(jwt, 0); 
		
		String mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		// 기본 정보 하나씩 누락 시험해봄
		// 누락된 기본 정보는 기존의 정보로 저장되게 처리함
		paramsMap.put("recipeId", uuid);
		paramsMap.put("title", "수정된 제목"); // 값 누락해보기
		paramsMap.put("tags", List.of("수정된 태그"));
		paramsMap.put("commentary", "수정된 소개글");
		paramsMap.put("imageFileName", mainImageUrl);
		
		List<Object> cookStepList = List.of(
				Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "수정된 과정1"),
				Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "수정된 과정2")
		);
		paramsMap.put("cookStepList", cookStepList);
		
		ObjectMapper jsonMapper = new ObjectMapper();
		String paramsJson = jsonMapper.writeValueAsString(paramsMap);
		
		mvc.perform(post("/recipe/edit-recipe")
				.header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(paramsJson))
				.andDo(print())
				.andExpect(status().isOk()); // 누락이된건 수정을 안하면 되니 그대로 ok 진행
	}
 
	@Test 
	public void updateRecipeArticleWithWrongInput2() throws Exception { 
		String jwt = login(); 
		String uuid = addRecipeFixture(jwt, 0); 
		
		String mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("recipeId", uuid);
		paramsMap.put("title", "수정된 제목"); // 값 누락해보기
		paramsMap.put("tags", List.of("수정된 태그"));
		paramsMap.put("commentary", "수정된 소개글");
		paramsMap.put("imageFileName", mainImageUrl);
		
		// 조리과정 누락 시도
		// 조리과정이 누락되고 순서나 다른 값이 변경되면 누락된 값은 빈 문자열로 저장하도록 처리함.
		List<Object> cookStepList = List.of(
				Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "수정된 과정1"),
				Map.of("imageFileName", cookStepImage2, "order", "1") // detail 누락
		);
		paramsMap.put("cookStepList", cookStepList);
		
		ObjectMapper jsonMapper = new ObjectMapper();
		String paramsJson = jsonMapper.writeValueAsString(paramsMap);
		
		mvc.perform(post("/recipe/edit-recipe")
				.header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(paramsJson))
				.andDo(print())
				.andExpect(status().isOk()); // 누락이된건 수정을 안하면 되니 그대로 ok 진행
	}
 
	
	@Test
	public void deleteReicpeArticle() throws Exception {
		String jwt = login();
		String uuid = addRecipeFixture(jwt, 0);
		
		mvc.perform(post("/recipe/delete-article")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + jwt)
				.param("recipeId", uuid))
			.andDo(print())
			.andExpect(status().isOk());
		
		String content = mvc.perform(get("/recipe/get-recipe-list")
				.param("pageNo", "1")
				.param("keyword", "뿌링클"))
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(content);
		ArrayNode list = (ArrayNode) jsonNode.get("recipeList");
		Iterator<JsonNode> iterator = list.elements();
		int cnt = 0;
		while(iterator.hasNext()) {
			iterator.next();
			cnt++;
		}
		assertEquals(0, cnt);
	}
	
	@Test
	public void deleteReicpeArticleWithWrongInput() throws Exception {
		String jwt = login();
		/*String uuid = */addRecipeFixture(jwt, 0);
		
		mvc.perform(post("/recipe/delete-article")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + jwt)
				.param("recipeId", "unknownId")) // 잘못된 recipeId 입력
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteReicpeArticleWithWrongInput2() throws Exception {
		String jwt = login();
		/*String uuid = */addRecipeFixture(jwt, 0);
		
		mvc.perform(post("/recipe/delete-article")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.header("Authorization", "Bearer " + jwt))
				//.param("recipeId", uuid)) -- 값 누락 
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getRecipeListForAjax() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}


		mvc.perform(get("/recipe/pre-search")
				.param("keyword", "뿌링클"))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	
	@Test
	public void getRecipeList() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isOk(), map -> {
			map.put("pageNo", List.of("1"));
			map.put("keyword", List.of("뿌링클"));
		});
	}
	
	@Test
	public void getRecipeList2() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isOk(), map -> {
			map.put("pageNo", List.of("1"));
			map.put("keyword", List.of("만들기9"));
		});
	}
	
	
	@Test
	public void getRecipeListWithWrongInput() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isOk(), map -> {
			//map.put("pageNo", List.of("1")); // 누락해보기
			map.put("keyword", List.of("뿌링클"));
		});
	}
	

	@Test
	public void getRecipeListWithWrongInput2() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isBadRequest(), map -> {
			map.put("pageNo", List.of("-1")); // 음수 값 전달
			map.put("keyword", List.of("뿌링클"));
		});
	}
	
	@Test
	public void getRecipeListWithWrongInput3() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isBadRequest(), map -> {
			map.put("pageNo", List.of("문자")); // 유효하지 않는 타입 전달
			map.put("keyword", List.of("뿌링클"));
		});
	}
	
	@Test
	public void getRecipeListWithWrongInput4() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isOk(), map -> {
			map.put("pageNo", List.of("2")); // 1페이지가 총 범위일 떄 2페이지 요청 시도
			map.put("keyword", List.of("뿌링클"));
		});
	}
	
	@Test
	public void getRecipeListWithWrongInput5() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		searchRecipe(status().isOk(), map -> {
			map.put("pageNo", List.of("1")); 
			// map.put("keyword", List.of("뿌링클")); // 키워드 누락 시 타이틀 필터링 없이 검색
		});
	}
	
	
	private void searchRecipe(ResultMatcher matcher, MultiValueMapSetterCallback callback) throws Exception {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		callback.callback(map);
		mvc.perform(get("/recipe/get-recipe-list")
				.params(map))
				.andDo(print())
				.andExpect(matcher);
	}
	
	@Test
	public void getRecent5RecipeList() throws Exception {
		String jwt = login();
		
		for(int i=0; i<10; i++) {
			addRecipeFixture(jwt, i);
		}
		
		assertEquals(10, recipeService.getAllRecipeCount());
	
		String json = mvc.perform(get("/recipe/get-recent-recipes")
						.param("amount", "5"))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn()
						.getResponse().getContentAsString();
		
		assertNotNull(json);
	}
	
	@Test
	public void getRecentRecipeListWithWrongAmount1() throws Exception {
		mvc.perform(get("/recipe/get-recent-recipes")
						.param("amount", "-1")) // 음수 값 전달
					.andDo(print())
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getRecentRecipeListWithWrongAmount2() throws Exception {
		mvc.perform(get("/recipe/get-recent-recipes"))
						//.param("amount", "-1")) // 파라미터 누락
					.andDo(print())
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void getRecentRecipeListWithWrongAmount3() throws Exception {
		mvc.perform(get("/recipe/get-recent-recipes")
						.param("amount", "문자입력")) // 유효하지 않는 파라미터 타입
					.andDo(print())
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void accessRecipeDetail() throws Exception {
		String jwt = login();
		String uuid = addRecipeFixture(jwt, 1);
		assertNotNull(uuid);
	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(); 
		params.set("recipeId", uuid);
  
		String json = mvc.perform(get("/recipe/detail") 
							.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED)
							.header("Authorization", "Bearer " + jwt)
							.params(params)) 
				.andDo(print())
				.andExpect(status().isOk()) 
				.andReturn()
					.getResponse().getContentAsString();
  
		assertNotNull(json);		
	}
	
	
	@Test
	public void accessRecipeDetailWithWronId() throws Exception {
		String jwt = login();
		//String uuid = addRecipeFixture(jwt); // 레시피 등록 안함
		String uuid = "wrong_uuid"; // 유효하지 않는 uuid
		assertNotNull(uuid);
	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(); 
		params.set("recipeId", uuid);
  
		mvc.perform(get("/recipe/detail") 
							.header("Authorization", "Bearer " + jwt) 
							.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED) 
							.params(params)) 
				.andDo(print())
				.andExpect(status().isNotFound()); 
	}
	

	@Test
	public void accessRecipeDetailWithWronId2() throws Exception {
		String jwt = login();
		//String uuid = addRecipeFixture(jwt); // 레시피 등록 안함
		String uuid = ""; // uuid 빈값
		assertNotNull(uuid);
	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(); 
		params.set("recipeId", uuid);
  
		mvc.perform(get("/recipe/detail") 
							.header("Authorization", "Bearer " + jwt) 
							.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED) 
							.params(params)) 
				.andDo(print())
				.andExpect(status().isNotFound()); 
	}
	
	@Test
	public void accessRecipeDetailWithWronId3() throws Exception {
		String jwt = login();
		//String uuid = addRecipeFixture(jwt); // 레시피 등록 안함
		String uuid = ""; // uuid 빈값
		assertNotNull(uuid);
	
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(); 
		// params.set("recipeId", uuid); // 파라미터 전달 누락
  
		mvc.perform(get("/recipe/detail") 
							.header("Authorization", "Bearer " + jwt) 
							.header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED) 
							.params(params)) 
				.andDo(print())
				.andExpect(status().isBadRequest()); 
	}
	
	private String addRecipeFixture(String jwt, int idxSufix) throws Exception {
		String mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		String cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);;
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("title", "뿌링클 만들기"+idxSufix);
		paramsMap.put("tags", List.of("존맛탱", "치킨"));
		paramsMap.put("commentary", "뿌링클 소개글"+idxSufix);
		paramsMap.put("imageFileName", mainImageUrl);
		
		List<Object> cookStepList = List.of(
				Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"+idxSufix),
				Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다."+idxSufix)
		);
		paramsMap.put("cookStepList", cookStepList);
		
		ObjectMapper jsonMapper = new ObjectMapper();
		String paramsJson = jsonMapper.writeValueAsString(paramsMap);
		
		String json = mvc.perform(post("/recipe/add-new-recipe")
				.header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(paramsJson))
				.andReturn().getResponse().getContentAsString();
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(json);
		return jsonNode.get("uuid").asText();
	}
	
	@Test
	public void uploadImageTest() throws Exception {
		String jwt = login();
		
		String testImagePath = "C:\\Users\\aibii\\OneDrive\\Desktop\\돈가스.PNG";
		MockMultipartFile file = new MockMultipartFile("image", "돈가스.PNG", "image/jpeg", new FileInputStream(new File(testImagePath)));
		
		mvc.perform(multipart("/recipe/upload-image")
						.file(file)
						.header("Authorization", "Bearer " + jwt))
			.andDo(print())
			.andExpect(status().isOk());
	}
	

	@Test
	public void addNewRecipe() throws Exception {
		String jwt = login();
		
		adjustParametersAndSend(jwt, status().isOk(), () -> {
			String mainImageUrl = null;
			String cookStepImage1 = null;
			String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", "뿌링클 만들기");
			paramsMap.put("tags", List.of("존맛탱", "치킨"));
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList);
			
			return paramsMap;
		});
	}
	
	@Test
	public void addNewRecipeWithWrongInput1() throws Exception {
		String jwt = login();
		
		adjustParametersAndSend(jwt, status().isBadRequest(), () -> {
			String mainImageUrl = null;
			String cookStepImage1 = null;
			String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			//paramsMap.put("title", "뿌링클 만들기"); // title 입력값 없애보기
			paramsMap.put("tags", List.of("존맛탱", "치킨"));
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList);
			
			return paramsMap;
		});
	}
	
	@Test
	public void addNewRecipeWithWrongInput2() throws Exception {
		String jwt = login();
		
		adjustParametersAndSend(jwt, status().isBadRequest(), () -> {
			String mainImageUrl = null;
			String cookStepImage1 = null;
			String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", "뿌링클 만들기"); 
			paramsMap.put("tags", null); // tag 값 null로 전송해보기
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList);
			
			return paramsMap;
		});
	}
	
	@Test
	public void addNewRecipeWithWrongInput3() throws Exception {
		String jwt = login();
		

		adjustParametersAndSend(jwt, status().isBadRequest(), () -> {
			String mainImageUrl = null;
			//String cookStepImage1 = null;
			//String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				//cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				//cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", "뿌링클 만들기"); 
			paramsMap.put("tags", List.of("존맛탱", "치킨")); // tag 입력값 없애보기
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			/*
			// 조리과정 입력 없이 전송해보기
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList); 
			*/
			
			return paramsMap;
		});
	}
	
	@Test
	public void addNewRecipeWithWrongInput4() throws Exception {
		String jwt = login();
		
		adjustParametersAndSend(jwt, status().isBadRequest(), () -> {
			String mainImageUrl = null;
			String cookStepImage1 = null;
			String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", ""); // 빈 값 입력해보기
			paramsMap.put("tags", List.of("존맛탱", "치킨"));
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList);
			
			return paramsMap;
		});
	}
	
	@Test
	public void addNewRecipeWithWrongInput5() throws Exception {
		String jwt = login();
		
		adjustParametersAndSend(jwt, status().isBadRequest(), () -> {
			String mainImageUrl = null;
			String cookStepImage1 = null;
			String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", "뿌링클 만들기"); 
			//paramsMap.put("tags", List.of("존맛탱", "치킨")); 태그 값 누락해보기 
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "0", "detail", "이런 과정을 거쳐서"),
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList);
			
			return paramsMap;
		});
	}
	
	@Test
	public void addNewRecipeWithWrongInput6() throws Exception {
		String jwt = login();
		
		adjustParametersAndSend(jwt, status().isBadRequest(), () -> {
			String mainImageUrl = null;
			String cookStepImage1 = null;
			String cookStepImage2 = null;
			
			try{
				mainImageUrl = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage1 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
				cookStepImage2 = uploadImage(jwt, SAMPLE_IMAGE_ORIGINAL_FILENAME, SAMPLE_IMAGE_CONTENT_TYPE, SAMPLE_IMAGE_PATH);
			}catch(Exception e) {
				e.printStackTrace();
				return null;
			}
			
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("title", "뿌링클 만들기");
			paramsMap.put("tags", List.of("존맛탱", "치킨"));
			paramsMap.put("commentary", "뿌링클 소개글");
			paramsMap.put("imageFileName", mainImageUrl);
			
			List<Object> cookStepList = List.of(
					Map.of("imageFileName", cookStepImage1, "order", "-1", "detail", "이런 과정을 거쳐서"), // 음수 값 입력해보기
					Map.of("imageFileName", cookStepImage2, "order", "1", "detail", "이렇게 만듭니다.")
			);
			paramsMap.put("cookStepList", cookStepList);
			
			return paramsMap;
		});
	}
	
	
	private void adjustParametersAndSend(String jwt, ResultMatcher resultMatcher, CallbackForTest callback) throws Exception {
		
		Object obj = callback.consumeWebParameters();
		if(obj == null) {
			return;
		}
		
		ObjectMapper jsonMapper = new ObjectMapper();
		String paramsJson = jsonMapper.writeValueAsString(obj);
		
		mvc.perform(post("/recipe/add-new-recipe")
				.header("Authorization", "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON)
				.content(paramsJson))
		.andDo(print())
		.andExpect(resultMatcher);
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
		String imageUrl = jsonNode.get("imageFileName").asText();
		return imageUrl;
	} 
	
	
	
}
