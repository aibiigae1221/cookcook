package com.aibiigae1221.cookcook.data.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table
@Entity
public class RecipeStep {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long stepId;
	
	@Column(nullable = false)
	private Long stepNumber;
	
	@Column(nullable = false, columnDefinition = "text")
	private String detail;
	
	private String imageFileName;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="recipe_id")
	private Recipe recipe;

	public RecipeStep() {}

	public Long getStepId() {
		return stepId;
	}

	public void setStepId(Long stepId) {
		this.stepId = stepId;
	}
	
	public Long getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Long stepNumber) {
		this.stepNumber = stepNumber;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	@Override
	public String toString() {
		return "RecipeStep [stepId=" + stepId + ", stepNumber=" + stepNumber + ", detail=" + detail + ", imageFileName="
				+ imageFileName + "]";
	}

	
}
