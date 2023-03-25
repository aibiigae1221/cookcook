package com.aibiigae1221.cookcook.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.entity.TemporaryImage;

public interface RecipeService {

	TemporaryImage saveImagePath(String name, MultipartFile image) throws IllegalStateException, IOException ;

}
