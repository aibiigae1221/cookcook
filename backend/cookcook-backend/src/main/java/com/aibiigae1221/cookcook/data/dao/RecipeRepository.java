package com.aibiigae1221.cookcook.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aibiigae1221.cookcook.data.entity.TemporaryImage;

public interface RecipeRepository extends JpaRepository<TemporaryImage, Long>{

}
