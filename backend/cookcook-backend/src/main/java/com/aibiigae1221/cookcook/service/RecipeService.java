package com.aibiigae1221.cookcook.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.Recipe;
import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.EditRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.RecipeSearchParameters;

import jakarta.validation.Valid;

public interface RecipeService {

	TemporaryImage saveImagePath(String name, MultipartFile image) throws IllegalStateException, IOException ;

	void removeAllUploadedImages();

	void removeAllRecipes();

	UUID saveNewRecipe(AddRecipeParameters params, User user);

	Recipe getRecipeDetail(String recipeId);

	long getAllRecipeCount();

	List<Recipe> getRecentRecipes(int amount);

	Page<Recipe> getRecipeList(@Valid RecipeSearchParameters params, int size);

	void deleteRecipe(String recipeId);

	Recipe getRecipe(UUID fromString);

	void editRecipe(Authentication authentication ,EditRecipeParameters params);

}
