package com.aibiigae1221.cookcook.data.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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

	@Column(nullable = false, columnDefinition = "text")
	private String commentary;
	
	private String imageFileName;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(insertable = false, updatable = false)
	private String createdDateFormatted;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;
	
	@Column(insertable = false, updatable = false)
	private String modifiedDateFormatted;
	
	@JsonManagedReference
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
		name="recipe_recipe_tag",
		joinColumns = @JoinColumn(name="recipe_id"),
		inverseJoinColumns = @JoinColumn(name="tag_id")
	)
	private Set<RecipeTag> tags;
	
	@JsonManagedReference
	@OneToMany(mappedBy="recipe", cascade = CascadeType.REMOVE)
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

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getCreatedDateFormatted() {
		// 여기는 date를 기본으로 가지고 있기 때문에 null 체크를 안함
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(createdDate);
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedDateFormatted() {
		if(this.modifiedDate != null) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.format(modifiedDate);	
		}
		
		return null;
	}


	@Override
	public String toString() {
		return "Recipe [recipeId=" + recipeId + ", user=" + user + ", title=" + title + ", commentary=" + commentary
				+ ", imageFileName=" + imageFileName + ", createdDate=" + createdDate + ", createdDateFormatted="
				+ createdDateFormatted + "]";
	}
	
}
