package com.aibiigae1221.cookcook.data.dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.aibiigae1221.cookcook.data.entity.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, UUID>{

	Optional<Recipe> findByRecipeId(UUID recipeId);
	Page<Recipe> findByOrderByCreatedDateDesc(Pageable paging);
}
