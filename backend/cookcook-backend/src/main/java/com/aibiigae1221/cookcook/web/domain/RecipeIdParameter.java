package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecipeIdParameter implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "레시피 번호가 필요합니다.")
	@NotNull(message = "레시피 번호가 필요합니다.")
	private String recipeId;
	
	public RecipeIdParameter() {}

	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
}
