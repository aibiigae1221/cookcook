package com.aibiigae1221.cookcook.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.Recipe;
import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.util.HashMapBean;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.ReicpeSearchParameters;

public interface RecipeService {

	TemporaryImage saveImagePath(String name, MultipartFile image) throws IllegalStateException, IOException ;

	void removeAllUploadedImages();

	void removeAllRecipes();

	UUID saveNewRecipe(AddRecipeParameters params, User user);

	Recipe getRecipeDetail(String recipeId);

	long getAllRecipeCount();

	List<Recipe> getRecentRecipes(int amount);

	void getRecipeList(ReicpeSearchParameters params, HashMapBean mapHolder);


}
