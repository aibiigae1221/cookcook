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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
import com.aibiigae1221.cookcook.web.domain.RecipeSearchParameters;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Transactional
@Service
public class RecipeServiceImpl implements RecipeService{

//	private static final Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);
	

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
	
	@Override
	public TemporaryImage saveImagePath(String owner, MultipartFile image) throws IllegalStateException, IOException {
		String imageFileName = getNewFileName(owner, image.getOriginalFilename());
		TemporaryImage entity = new TemporaryImage();
		entity.setCreatedAt(new Date());
		entity.setImageLocalPath(uploadLocalPath + File.separator + imageFileName);
		entity.setImageFileName(imageFileName);
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
		Recipe savedRecipe = saveRecipe(params, user);
		saveRecipeTags(params, savedRecipe);
		saveRecipeSteps(params, savedRecipe);
		setImageUsedFlag(params.getImageFileName());
		params.getCookStepList().forEach(step -> setImageUsedFlag(step.getImageFileName()));
		return savedRecipe.getRecipeId();
	}


	private void setImageUsedFlag(String imageUrl) {
		if(imageUrl == null || imageUrl.isEmpty())
			return;
		TemporaryImage entity = temporaryImageRepository.findByImageFileName(imageUrl);
		entity.setStatus("used");
		temporaryImageRepository.save(entity);
	}

	private void saveRecipeSteps(AddRecipeParameters params, Recipe savedRecipe) {
		Set<RecipeStep> recipeStepList = params.getCookStepList().stream().map(step -> {
			RecipeStep recipeStep = new RecipeStep();
			recipeStep.setStepNumber(Long.valueOf(step.getOrder()));
			recipeStep.setDetail(step.getDetail());
			recipeStep.setImageFileName(step.getImageFileName());
			recipeStep.setRecipe(savedRecipe);
			return recipeStep;
		}).collect(Collectors.toSet());
		recipeStepList.forEach(step -> recipeStepRepository.save(step));
	}

	private void saveRecipeTags(AddRecipeParameters params, Recipe savedRecipe) {
		Set<RecipeTag> recipeTags = params.getTags().stream().map(tag -> {
			RecipeTag recipeTag = recipeTagRepository.findByTagName(tag).orElseGet(() -> {
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
		recipe.setImageFileName(params.getImageFileName());
		recipe.setCreatedDate(new Date());
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

	@Override
	public long getAllRecipeCount() {
		return recipeRepository.count();
	}

	@Override
	public List<Recipe> getRecentRecipes(int amount) {
		Page<Recipe> page = recipeRepository.findByOrderByCreatedDateDesc(PageRequest.of(0, amount));
		return page.getContent();
	}

	@Override
	public Page<Recipe> getRecipeList(@Valid RecipeSearchParameters params, int size) {
		Pageable pageable = PageRequest.of(params.getPageNo()-1, size);
		if(StringUtils.hasText(params.getKeyword())) {
			return recipeRepository.findByTitleContainsOrderByCreatedDateDesc(params.getKeyword(), pageable); 
		}
		return recipeRepository.findByOrderByCreatedDateDesc(pageable);
	}

	@Override
	public void deleteRecipe(String recipeId) {
		Recipe recipe = recipeRepository.findByRecipeId(UUID.fromString(recipeId)).orElseThrow(() -> new RecipeNotFoundException());
		recipeRepository.delete(recipe);
	}

	@Override
	public Recipe getRecipe(UUID recipeId) {
		return recipeRepository.findById(recipeId).orElseThrow(() -> new RecipeNotFoundException());
	}
}
