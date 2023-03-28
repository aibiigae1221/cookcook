package com.aibiigae1221.cookcook.data.entity;

import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name="users")
@Entity
public class User {

	@JsonIgnore
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name="uuid2", strategy = "uuid2")
	private UUID userId;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@JsonIgnore
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private String nickname;

	@JsonBackReference
	@OneToMany(mappedBy="user")
	private Set<Recipe> recipeList;
	
	public User() {}

	public User(String email, String password, String nickname) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public Set<Recipe> getRecipeList() {
		return recipeList;
	}

	public void setRecipeList(Set<Recipe> recipeList) {
		this.recipeList = recipeList;
	}
	
	
}
