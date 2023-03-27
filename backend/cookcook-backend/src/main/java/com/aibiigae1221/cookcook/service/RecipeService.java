package com.aibiigae1221.cookcook.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;

public interface RecipeService {

	TemporaryImage saveImagePath(String name, MultipartFile image) throws IllegalStateException, IOException ;

	void removeAllUploadedImages();

	void removeAllRecipes();

	UUID saveNewRecipe(AddRecipeParameters params, User user);


}
