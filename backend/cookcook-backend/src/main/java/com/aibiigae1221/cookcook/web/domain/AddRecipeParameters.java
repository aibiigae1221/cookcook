package com.aibiigae1221.cookcook.web.domain;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AddRecipeParameters implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull(message = "제목을 입력해주세요.")
	@NotBlank(message = "제목을 입력해주세요.")
	private String title;
	
	@NotNull(message = "레시피에 태그를 부여해야 합니다.")
	@NotEmpty(message = "레시피에 태그를 부여해야 합니다.")
	private List<String> tags;
	
	@NotNull(message = "보내주시는 레시피의 부연설명을 해주세요.")
	@NotBlank(message = "보내주시는 레시피의 부연설명을 해주세요.")
	private String commentary;
	
	private String imageFileName;
	
	@Valid
	@NotNull(message = "조리과정을 입력해주세요.")
	private List<AddRecipeCookStepParameters> cookStepList;
	
	public AddRecipeParameters() {}
	
	
	
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

	public List<AddRecipeCookStepParameters> getCookStepList() {
		return cookStepList;
	}

	public void setCookStepList(List<AddRecipeCookStepParameters> cookStepList) {
		this.cookStepList = cookStepList;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	@Override
	public String toString() {
		return "AddRecipeParameters [title=" + title + ", commentary=" + commentary + ", imageFileName=" + imageFileName
				+ "]";
	}
}
