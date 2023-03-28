package com.aibiigae1221.cookcook.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aibiigae1221.cookcook.data.dao.RecipeRepository;
import com.aibiigae1221.cookcook.data.dao.RecipeStepRepository;
import com.aibiigae1221.cookcook.data.dao.RecipeTagRepository;
import com.aibiigae1221.cookcook.data.dao.TemporaryImageRepository;
import com.aibiigae1221.cookcook.data.entity.Recipe;
import com.aibiigae1221.cookcook.data.entity.RecipeStep;
import com.aibiigae1221.cookcook.data.entity.RecipeTag;
import com.aibiigae1221.cookcook.data.entity.TemporaryImage;
import com.aibiigae1221.cookcook.data.entity.User;
import com.aibiigae1221.cookcook.service.exception.RecipeNotFoundException;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService{

	private static final Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);
	

	@Autowired
	private TemporaryImageRepository temporaryImageRepository;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private RecipeStepRepository recipeStepRepository;
	
	@Autowired
	private RecipeTagRepository recipeTagRepository;
	
	
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
		TemporaryImage saved = temporaryImageRepository.save(entity);
		
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

	@Override
	public void removeAllUploadedImages() {
		List<TemporaryImage> list = temporaryImageRepository.findAll();
		
		list.forEach(image -> {
			String path = image.getImageLocalPath();
			File file = new File(path);
			file.deleteOnExit();
		});
		
		temporaryImageRepository.deleteAll();
	}

	@Override
	public void removeAllRecipes() {
		recipeTagRepository.deleteAll();
		recipeStepRepository.deleteAll();
		recipeRepository.deleteAll();
	}

	@Override
	public UUID saveNewRecipe(AddRecipeParameters params, User user) {
		logger.info("레시피 등록 중..");
		Recipe savedRecipe = saveRecipe(params, user);
		saveRecipeTags(params, savedRecipe);
		saveRecipeSteps(params, savedRecipe);
		
		setImageUsedFlag(params.getMainImageUrl());
		params.getCookStepList().forEach(step -> setImageUsedFlag(step.getUploadUrl()));
		return savedRecipe.getRecipeId();
	}


	private void setImageUsedFlag(String imageUrl) {
		if(imageUrl == null || imageUrl.isEmpty())
			return;
		
		TemporaryImage entity = temporaryImageRepository.findByImageUrl(imageUrl);
		entity.setStatus("used");
		temporaryImageRepository.save(entity);
	}

	private void saveRecipeSteps(AddRecipeParameters params, Recipe savedRecipe) {
		Set<RecipeStep> recipeStepList = params.getCookStepList().stream().map(step -> {
			RecipeStep recipeStep = new RecipeStep();
			recipeStep.setStepNumber(Long.valueOf(step.getOrder()));
			recipeStep.setDetail(step.getDetail());
			recipeStep.setImageUrl(step.getUploadUrl());
			recipeStep.setRecipe(savedRecipe);
			return recipeStep;
		}).collect(Collectors.toSet());
		
		recipeStepList.forEach(step -> recipeStepRepository.save(step));
	}

	private void saveRecipeTags(AddRecipeParameters params, Recipe savedRecipe) {
		Set<RecipeTag> recipeTags = params.getTags().stream().map(tag -> {
			RecipeTag recipeTag = recipeTagRepository.findByTagName(tag).orElseGet(() -> {
				logger.info("새로운 태그 등록중..");
				RecipeTag newRecipeTag = new RecipeTag();
				newRecipeTag.setTagName(tag);
				newRecipeTag.setRecipeList(new HashSet<Recipe>());
				return newRecipeTag;
			});
			
			return recipeTag;
		}).collect(Collectors.toSet());
		
		savedRecipe.setTags(recipeTags);
		
		recipeTags.forEach(recipeTag -> {
			recipeTag.getRecipeList().add(savedRecipe);
			recipeTagRepository.save(recipeTag);
		});
		
	}

	private Recipe saveRecipe(AddRecipeParameters params, User user) {
		Recipe recipe = new Recipe();
		recipe.setUser(user);
		recipe.setTitle(params.getTitle());
		recipe.setCommentary(params.getCommentary());
		recipe.setMainImageUrl(params.getMainImageUrl());
		
		return recipeRepository.save(recipe);
	}

	@Override
	public Recipe getRecipeDetail(String recipeId) {
		UUID uuid = null;
		
		try{
			uuid = UUID.fromString(recipeId);
		}catch(IllegalArgumentException e) {
			throw new RecipeNotFoundException();
		}
		
		return recipeRepository.findByRecipeId(uuid).orElseThrow(() -> new RecipeNotFoundException());
	}

}
