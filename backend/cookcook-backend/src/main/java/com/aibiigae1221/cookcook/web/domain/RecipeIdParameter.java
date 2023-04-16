package com.aibiigae1221.cookcook.web.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecipeIdParameter {

	@NotBlank(message = "레시피 번호가 필요합니다.")
	@NotNull(message = "레시피 번호가 필요합니다.")
	private String recipeId;
	
	public RecipeIdParameter() {
		super();
	}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
}
