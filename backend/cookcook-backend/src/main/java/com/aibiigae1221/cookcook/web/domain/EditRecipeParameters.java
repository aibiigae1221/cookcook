package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EditRecipeParameters implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "레시피 번호가 필요합니다.")
	@NotNull(message = "레시피 번호가 필요합니다.")
	private String recipeId;
	
	private String title;
	
	private List<String> tags;
	
	private String commentary;
	
	private String imageFileName;
	
	private List<EditRecipeCookStepParameters> cookStepList;

	
	
	public EditRecipeParameters() {}
	
	public String getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public List<EditRecipeCookStepParameters> getCookStepList() {
		return cookStepList;
	}

	public void setCookStepList(List<EditRecipeCookStepParameters> cookStepList) {
		this.cookStepList = cookStepList;
	}

	@Override
	public String toString() {
		return "EditRecipeParameters [recipeId=" + recipeId + ", title=" + title + ", tags=" + tags + ", commentary="
				+ commentary + ", imageFileName=" + imageFileName + ", cookStepList=" + cookStepList + "]";
	}
}
