package com.aibiigae1221.cookcook.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.aibiigae1221.cookcook.util.HashMapBean;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.RecentRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.ReicpeSearchParameters;

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
	
	@Value("${user-resource-server-url}")
	private String resourceServerUrl;
	
	@GetMapping("/recipe/get-recipe-list")
	private ResponseEntity<?> getRecipeList(@Valid ReicpeSearchParameters params){

		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		
		recipeService.getRecipeList(params, mapHolder);
		mapHolder.put("status", "success");
				
		return ok(mapHolder);
	}
	
	@GetMapping("/recipe/get-recent-recipes")
	public ResponseEntity<?> getRecentRecipes(@Valid RecentRecipeParameters params){
		
		List<Recipe> recipeList = recipeService.getRecentRecipes(params.getAmount());
		
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("status", "success");
		mapHolder.put("recipeList", recipeList);
		
		return ok(mapHolder);
	}
	
	@PostMapping("/recipe/upload-image")
	public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, Authentication authentication){
		logger.info("유저[{}]님이 파일업로드 중...{}", authentication.getName(), image.getOriginalFilename());

		HashMapBean mapHolder = hashMapHolderProvider.getObject();		
		try {
			TemporaryImage entity = recipeService.saveImagePath(authentication.getName(), image);
			mapHolder.put("status", "success");
			mapHolder.put("imageUrl", entity.getImageUrl());
			return ok(mapHolder);
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
		
		return ok(mapHolder);
	}
	
	@GetMapping("/recipe/detail")
	public ResponseEntity<?> recipeDetail(@RequestParam("recipeId") String recipeId){
		Recipe recipe = recipeService.getRecipeDetail(recipeId);
		
		HashMapBean mapHolder = hashMapHolderProvider.getObject();
		mapHolder.put("status", "success");
		mapHolder.put("recipe", recipe);
		
		return ok(mapHolder);
	}
	
	public ResponseEntity<?> ok(HashMapBean mapHolder) {
		return ResponseEntity.status(HttpStatus.OK).body(mapHolder.getSource());
	}
}
