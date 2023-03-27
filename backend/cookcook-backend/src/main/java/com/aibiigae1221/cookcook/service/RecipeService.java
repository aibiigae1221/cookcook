package com.aibiigae1221.cookcook.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;

public interface RecipeService {

	TemporaryImage saveImagePath(String name, MultipartFile image) throws IllegalStateException, IOException ;

	void removeAllUploadedImages();

	void removeAllRecipes();

	void saveNewRecipe(AddRecipeParameters params, User user);


}
