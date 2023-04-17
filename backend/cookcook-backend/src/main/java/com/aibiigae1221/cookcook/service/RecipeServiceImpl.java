package com.aibiigae1221.cookcook.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
import com.aibiigae1221.cookcook.service.auxiliary.CollectionComparer;
import com.aibiigae1221.cookcook.service.exception.RecipeCookStepNotFoundException;
import com.aibiigae1221.cookcook.service.exception.RecipeNotFoundException;
import com.aibiigae1221.cookcook.web.domain.AddRecipeCookStepParameters;
import com.aibiigae1221.cookcook.web.domain.AddRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.EditRecipeCookStepParameters;
import com.aibiigae1221.cookcook.web.domain.EditRecipeParameters;
import com.aibiigae1221.cookcook.web.domain.RecipeSearchParameters;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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
	
	@Autowired
	private CollectionComparer<RecipeTag, String> recipeTagEditComparer;
	
	@Autowired
	private CollectionComparer<RecipeStep, EditRecipeCookStepParameters> recipeStepEditComparer;
	
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
		saveNewRecipeSteps(params.getCookStepList(), savedRecipe);
		setImageUsedFlag(params.getImageFileName());
		params.getCookStepList().forEach(step -> setImageUsedFlag(step.getImageFileName()));
		return savedRecipe.getRecipeId();
	}

	private void saveRecipeTags(AddRecipeParameters params, Recipe savedRecipe) {
		saveRecipeTags(params.getTags().stream(), savedRecipe); 
	}

	private void setImageUsedFlag(String imageUrl) {
		if(imageUrl == null || imageUrl.isEmpty())
			return;
		TemporaryImage entity = temporaryImageRepository.findByImageFileName(imageUrl);
		entity.setStatus("used");
		temporaryImageRepository.save(entity);
	}

	
	private void saveNewRecipeSteps(List<AddRecipeCookStepParameters> stepList, Recipe savedRecipe) {
		Set<RecipeStep> recipeStepList = stepList.stream().map(step -> {
			RecipeStep recipeStep = new RecipeStep();
			recipeStep.setStepNumber(Long.valueOf(step.getOrder()));
			recipeStep.setDetail(step.getDetail());
			recipeStep.setImageFileName(step.getImageFileName());
			recipeStep.setRecipe(savedRecipe);
			return recipeStep;
		}).collect(Collectors.toSet());
		saveRecipeSteps(recipeStepList);
		
	}
	
	private void saveRecipeSteps(Set<RecipeStep> recipeStepList) {
		recipeStepList.forEach(step -> recipeStepRepository.save(step));
	}

	private void saveRecipeTags(Stream<String> tagCollectionStream, Recipe savedRecipe) {
		Set<RecipeTag> recipeTags = tagCollectionStream.map(tag -> {
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

	@Override
	public void editRecipe(Authentication authentication, EditRecipeParameters params) {

		Recipe originalRecipe = recipeRepository.findById(UUID.fromString(params.getRecipeId())).orElseThrow(() -> new RecipeNotFoundException());
		checkRecipeIfAuthorized(authentication, originalRecipe);
		
		// 레시피 기본 정보 수정
		boolean editted = false;
		editted = editted | changeStringValueIfDifferent("title", originalRecipe, params);
		editted = editted | changeStringValueIfDifferent("commentary", originalRecipe, params);
		
		
		String originalMainImageFileName = originalRecipe.getImageFileName();
		boolean newImage = changeStringValueIfDifferent("imageFileName", originalRecipe, params);
		if(newImage) {
			setImageUsedFlag(params.getImageFileName());
			removeUploadedImage(originalMainImageFileName);
		}
		
		editted = editted | newImage;
		
		// 태그 수정
		Set<RecipeTag> originalTags = originalRecipe.getTags();
		List<String> newTags = params.getTags();
		if(newTags != null) {
			// 새로 받은 값과 기존의 있는 태그 값들 검사
			boolean tagEditted = recipeTagEditComparer.checkIfCollectionDataIsDifferent(originalTags, newTags, (originalTag, mayEdittedTag) -> {
				return !(originalTag.getTagName().equals(mayEdittedTag));
			});
			
			if(tagEditted) {
				originalRecipe.getTags().clear();
				saveRecipeTags(newTags, originalRecipe);
				editted = true;
			}
		}
		
		
		// 조리과정 수정
		Set<RecipeStep> originalSteps = originalRecipe.getStepList();
		List<EditRecipeCookStepParameters> newSteps = params.getCookStepList();
		boolean stepEditted = recipeStepEditComparer.checkIfCollectionDataIsDifferent(originalSteps, newSteps, (originalStep, newStep) -> {
			boolean innerDiff = false;
			// 값이 존재하면 수정된 컨텐츠인지 검색
			// 만약 값이 없다면 수정을 하지 않기
			if(StringUtils.hasText(newStep.getDetail())) {
				innerDiff = innerDiff | !originalStep.getDetail().equals(newStep.getDetail());
			}
			if(StringUtils.hasText(newStep.getImageFileName())) {
				innerDiff = innerDiff | !originalStep.getImageFileName().equals(newStep.getImageFileName());	
			}
			if(StringUtils.hasText(String.valueOf(newStep.getOrder()))) {
				innerDiff = innerDiff | originalStep.getStepNumber() != newStep.getOrder();
			}
			
			return innerDiff;
		});
		
		if(stepEditted) {
			Set<Long> stepIdList = originalSteps.stream().map(step -> step.getStepId()).collect(Collectors.toSet());
			originalRecipe.getStepList().clear();
			saveEditRecipeSteps(newSteps, originalRecipe);
			stepIdList.forEach(stepId -> removeStepsById(stepId));
			newSteps.forEach(step -> {
				setImageUsedFlag(step.getImageFileName());
			});
			
			editted = true;
		}
		
		originalRecipe.setModifiedDate(new Date());
	}
	
	private void removeStepsById(Long stepId) {
		RecipeStep step = recipeStepRepository.findById(stepId).orElseThrow(() -> new RecipeCookStepNotFoundException());
		String imageFileName = step.getImageFileName();
		step.setImageFileName(null);
		recipeStepRepository.delete(step);
		removeUploadedImage(imageFileName);
		
	}

	private void removeUploadedImage(String imageFileName) {
		TemporaryImage tempImage = temporaryImageRepository.findByImageFileName(imageFileName);
		temporaryImageRepository.delete(tempImage);
	}

	private void saveEditRecipeSteps(List<EditRecipeCookStepParameters> stepList, Recipe originalRecipe) {
		Set<RecipeStep> recipeStepList = stepList.stream().map(step -> {
			RecipeStep recipeStep = new RecipeStep();
			recipeStep.setRecipe(originalRecipe);
			recipeStep.setStepNumber(Long.valueOf(step.getOrder()));
			
			if(StringUtils.hasText(step.getDetail())) {
				recipeStep.setDetail(step.getDetail());
			}else {
				recipeStep.setDetail("");
			}
			
			if(StringUtils.hasText(step.getImageFileName())) {
				recipeStep.setImageFileName(step.getImageFileName());
			}
			
			return recipeStep;
		}).collect(Collectors.toSet());
		saveRecipeSteps(recipeStepList);
	}

	

	private void saveRecipeTags(List<String> newTags, Recipe originalRecipe) {
		saveRecipeTags(newTags.stream(), originalRecipe);
	}

	private void checkRecipeIfAuthorized(Authentication authentication, Recipe recipe) {
		if(authentication == null) {
			throw new AccessDeniedException("수정할 레시피에 접근 권한이 없습니다");
		}else {
			if(!authentication.getName().equals(recipe.getUser().getEmail())) {
				throw new AccessDeniedException("수정할 레시피에 접근 권한이 없습니다");
			}
		}
	}

	private boolean changeStringValueIfDifferent(String fieldName, Recipe originalEntity, EditRecipeParameters params) {
		boolean changed = false;
		
		try{
			Field paramsField = params.getClass().getDeclaredField(fieldName);
			paramsField.setAccessible(true);
			String paramValue = (String) paramsField.get(params);
			
			if(StringUtils.hasText(paramValue)) {
				Field originalField = originalEntity.getClass().getDeclaredField(fieldName);
				originalField.setAccessible(true);
				String originalValue = (String) originalField.get(originalEntity);
				if(!originalValue.equals(paramValue)) {
					originalField.set(originalEntity, paramValue);
					changed = true;
				}
				originalField.setAccessible(false);
			}
			paramsField.setAccessible(false);
			return changed;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
