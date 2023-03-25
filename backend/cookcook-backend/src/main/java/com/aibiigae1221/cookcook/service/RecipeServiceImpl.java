package com.aibiigae1221.cookcook.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.dao.RecipeRepository;
import com.aibiigae1221.cookcook.data.entity.TemporaryImage;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService{

	@Autowired
	private RecipeRepository recipeRepository;

	@Value("${spring.servlet.multipart.location}")
	private String uploadLocalPath;
	
	@Value("${user-resource-server-url}")
	private String resourceServerUrl;
	
	@Override
	public TemporaryImage saveImagePath(String owner, MultipartFile image) throws IllegalStateException, IOException {
		
		String imageFileName = getNewFileName(owner, image.getOriginalFilename());
		
		TemporaryImage entity = new TemporaryImage();
		entity.setCreatedAt(new Date());
		entity.setImageLocalPath(uploadLocalPath + "\\" + imageFileName);
		entity.setImageUrl(resourceServerUrl+"/"+imageFileName);
		entity.setStatus("unused");
		TemporaryImage saved = recipeRepository.save(entity);
		
		image.transferTo( new File(saved.getImageLocalPath()));
		
		return saved;
	}

	private String getNewFileName(String owner, String originalFileName) {
		UUID uuid = UUID.randomUUID();
		LocalDate localDate = LocalDate.now();
		String newFileName = String.join("-", uuid.toString(), owner, localDate.toString());
		String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
		return newFileName + extension;
	}
}
