package com.aibiigae1221.cookcook.data.entity;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Table
@Entity
public class Recipe {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name="uuid2", strategy = "uuid2")
	private UUID recipeId;
	
	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String commentary;
	
	private String mainImageUrl;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@JsonManagedReference
	@ManyToMany
	@JoinTable(
		name="recipe_recipe_tag",
		joinColumns = @JoinColumn(name="recipe_id"),
		inverseJoinColumns = @JoinColumn(name="tag_id")
	)
	private Set<RecipeTag> tags;
	
	@JsonManagedReference
	@OneToMany(mappedBy="recipe")
	private Set<RecipeStep> stepList;
	
	public Recipe() {}

	public UUID getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(UUID recipeId) {
		this.recipeId = recipeId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCommentary() {
		return commentary;
	}

	public void setCommentary(String commentary) {
		this.commentary = commentary;
	}

	public String getMainImageUrl() {
		return mainImageUrl;
	}

	public void setMainImageUrl(String mainImageUrl) {
		this.mainImageUrl = mainImageUrl;
	}

	public Set<RecipeTag> getTags() {
		return tags;
	}

	public void setTags(Set<RecipeTag> tags) {
		this.tags = tags;
	}
	
	public Set<RecipeStep> getStepList() {
		return stepList;
	}

	public void setStepList(Set<RecipeStep> stepList) {
		this.stepList = stepList;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "Recipe [recipeId=" + recipeId + ", title=" + title + ", commentary=" + commentary + ", mainImageUrl="
				+ mainImageUrl + ", createdDate=" + createdDate + "]";
	}
	
}
