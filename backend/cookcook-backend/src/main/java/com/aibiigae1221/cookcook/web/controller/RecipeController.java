package com.aibiigae1221.cookcook.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.Recipe;
import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.RecipeService;
import com.aibiigae1221.cookcook.service.UserService;
import com.aibiigae1221.cookcook.service.exception.RecipeNotFoundException;
import com.aibiigae1221.cookcook.util.HashMapBean;
import com.aibiigae1221.cookcook.web.controller.auxiliary.ResponseOk;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.EditRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.RecentRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.RecipeIdParameter;
import com.aibiigae1221.cookcook.web.domain.RecipeSearchParameters;

import jakarta.validation.Valid;

@RestController
public class RecipeController {

	private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ObjectProvider<HashMapBean> hashMapHolderProvider;
	
	@Value("${spring.servlet.multipart.location}")
	private String uploadLocalPath;
	
	@Autowired
	private ResponseOk responseOk;
	
	@PostMapping("/recipe/edit-recipe")
	public ResponseEntity<?> editRecipe(@Valid @RequestBody EditRecipeParameters params, Authentication authentication){
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		logger.info(params.toString());
		recipeService.editRecipe(authentication, params);
		return responseOk.ok(mapHolder);
	}
	
	@PostMapping("/recipe/delete-article")
	public ResponseEntity<?> deleteRecipe(@Valid RecipeIdParameter param, Authentication authentication){
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		try {
			Recipe recipe = recipeService.getRecipe(UUID.fromString(param.getRecipeId()));
			if(!(authentication.getName().equals(recipe.getUser().getEmail()))) {
				mapHolder.put("message", "권한이 없습니다!");
				mapHolder.put("status", "error");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapHolder.getSource());
			}
			recipeService.deleteRecipe(param.getRecipeId());
		}catch(RecipeNotFoundException e) {
			mapHolder.put("status", "error");
			mapHolder.put("message", "레시피를 찾을 수 없습니다.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapHolder.getSource());
		}
		return responseOk.ok(mapHolder);
	}
	
	// 이 end point는 ajax로 미리보기 검색결과를 보여주는 용도이기 때문에 totalPage를 전송하지 않음.
	@GetMapping("/recipe/pre-search")
	public ResponseEntity<?> preSearch(RecipeSearchParameters params){
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		if(StringUtils.hasText(params.getKeyword())) {
			Page<Recipe> pages = recipeService.getRecipeList(params, 5);
			mapHolder.put("recipeList", pages.getContent());
		}
		mapHolder.put("status", "success");
		return responseOk.ok(mapHolder);
	}
	
	@GetMapping("/recipe/get-recipe-list")
	public ResponseEntity<?> getRecipeList(@Valid RecipeSearchParameters params){
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		Page<Recipe> pages = recipeService.getRecipeList(params, 5);
		mapHolder.put("status", "success");
		mapHolder.put("recipeList", pages.getContent());
		mapHolder.put("totalPage", pages.getTotalPages());
		return responseOk.ok(mapHolder);
	}
	
	@GetMapping("/recipe/get-recent-recipes")
	public ResponseEntity<?> getRecentRecipes(@Valid RecentRecipeParameters params){
		List<Recipe> recipeList = recipeService.getRecentRecipes(params.getAmount());
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("status", "success");
		mapHolder.put("recipeList", recipeList);
		return responseOk.ok(mapHolder);
	}
	
	@PostMapping("/recipe/upload-image")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication){
		HashMapBean mapHolder = hashMapHolderProvider.getObject();		
		try {
			TemporaryImage entity = recipeService.saveImagePath(authentication.getName(), image);
			mapHolder.put("status", "success");
			mapHolder.put("imageFileName", entity.getImageFileName());
			return responseOk.ok(mapHolder);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			mapHolder.put("status", "error");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapHolder.getSource());
		}
	}
	
	@PostMapping("/recipe/add-new-recipe")
	public ResponseEntity<?> addNewRecipe(@Valid @RequestBody AddRecipeParameters params, Authentication authentication){
		User user = userService.loadUserByEmail(authentication.getName());
		UUID uuid = recipeService.saveNewRecipe(params, user);
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("status", "success");
		mapHolder.put("uuid", uuid.toString());
		
		return responseOk.ok(mapHolder);
	}
	
	@GetMapping("/recipe/detail")
	public ResponseEntity<?> recipeDetail(Authentication authentication, @RequestParam("recipeId") String recipeId){
		Recipe recipe = recipeService.getRecipeDetail(recipeId);
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("status", "success");
		mapHolder.put("recipe", recipe);
		if(authentication != null) {
			if(authentication.getName().equals(recipe.getUser().getEmail())) {
				mapHolder.put("isAuthor", true);
			}
		}
		return responseOk.ok(mapHolder);
	}
}
