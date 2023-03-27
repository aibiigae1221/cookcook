package com.aibiigae1221.cookcook.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aibiigae1221.cookcook.data.entity.TemporaryImage;

public interface TemporaryImageRepository extends JpaRepository<TemporaryImage, Long>{

	TemporaryImage findByImageUrl(String imageUrl);


}
