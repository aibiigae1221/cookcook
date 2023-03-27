package com.aibiigae1221.cookcook.data.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aibiigae1221.cookcook.data.entity.RecipeTag;

public interface RecipeTagRepository extends JpaRepository<RecipeTag, Long>{

	Optional<RecipeTag> findByTagName(String tag);

}
