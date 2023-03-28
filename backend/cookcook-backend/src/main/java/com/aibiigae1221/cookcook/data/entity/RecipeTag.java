package com.aibiigae1221.cookcook.data.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Table(name="recipe_tag")
@Entity
public class RecipeTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagId;
	
	@Column(nullable = false, unique = true)
	private String tagName;
	
	@JsonBackReference
	@ManyToMany(mappedBy="tags")
	private Set<Recipe> recipeList;

	public RecipeTag() {}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Set<Recipe> getRecipeList() {
		return recipeList;
	}

	public void setRecipeList(Set<Recipe> recipeList) {
		this.recipeList = recipeList;
	}

	@Override
	public String toString() {
		return "RecipeTag [tagId=" + tagId + ", tagName=" + tagName + "]";
	}
}
