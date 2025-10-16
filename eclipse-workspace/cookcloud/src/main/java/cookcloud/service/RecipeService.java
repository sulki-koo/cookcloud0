package cookcloud.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Hashtag;
import cookcloud.entity.Member;
import cookcloud.entity.Recipe;
import cookcloud.entity.RecipeTag;
import cookcloud.repository.HashtagRepository;
import cookcloud.repository.MemberRepository;
import cookcloud.repository.RecipeRepository;
import cookcloud.repository.RecipeTagRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RecipeService {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private RecipeRepository recipeRepository;

	@Autowired
	private RecipeTagRepository recipeTagRepository;

	@Autowired
	private HashtagRepository hashtagRepository;

	@Autowired
	private LikesService likesService;

	public List<Recipe> getRecipes() {
		return recipeRepository.findAllNotDeleted();
	}

	public Optional<Recipe> getRecipe(Long recipeId) {
		return recipeRepository.findByIdAndNotDeleted(recipeId);
	}

	// 개인 레시피 목록 조회
	public List<Recipe> getMyRecipes(String memId) {
		List<Recipe> recipes = recipeRepository.findByMemId(memId);

		// 각 레시피에 첫 번째 첨부파일 URL을 설정
		for (Recipe recipe : recipes) {
			if (!recipe.getAttachList().isEmpty()) {
				recipe.setImageUrl(recipe.getAttachList().get(0).getAttachServerName());
			}
		}
		return recipes;
	}

	// memNickname을 기준으로 회원의 레시피 목록 조회
	public List<Recipe> getMemNicknameRecipes(String memNickname) {
		try {
			Member member = memberRepository.findAllNotDeleted().stream()
					.filter(m -> m.getMemNickname().equals(memNickname)).findFirst()
					.orElseThrow(() -> new IllegalAccessException("닉네임 " + memNickname + " 확인불가"));

			List<Recipe> recipes = recipeRepository.findAllNotDeleted().stream()
					.filter(recipe -> recipe.getMember().getMemId().equals(member.getMemId()))
					.collect(Collectors.toList());

			// 각 레시피에 첫 번째 첨부파일 URL 설정
			recipes.forEach(recipe -> {
				if (!recipe.getAttachList().isEmpty()) {
					recipe.setImageUrl(recipe.getAttachList().get(0).getAttachServerName());
				}
			});
			return recipes;
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			return List.of();
		}
	}

	public List<Recipe> getLikedRecipes(String memId) {
		List<Long> likedRecipeIds = likesService.getLikedRecipeIds(memId); // 좋아요한 recipeId 목록 가져오기
		if (likedRecipeIds.isEmpty()) {
			return Collections.emptyList(); // 좋아요한 레시피가 없으면 빈 리스트 반환
		}
		return recipeRepository.findRecipesByIds(likedRecipeIds);
	}

	@Transactional
	public Recipe createRecipe(Recipe recipe) {
		recipe.setRecipeViewCount(0L);
		recipe.setRecipeInsertAt(LocalDateTime.now());
		recipe.setRecipeIsDeleted("N");
		recipe.setRecipeBoardCode(41L);
		return recipeRepository.save(recipe);
	}

	@Transactional
	public Recipe updateRecipe(Long recipeId, Recipe newRecipe) {
		return recipeRepository.findByIdAndNotDeleted(recipeId).map(recipe -> {
			recipe.setRecipeTitle(newRecipe.getRecipeTitle());
			recipe.setRecipeContent(newRecipe.getRecipeContent());
			recipe.setRecipeCode(newRecipe.getRecipeCode());
			recipe.setRecipeUpdateAt(LocalDateTime.now());
			return recipeRepository.save(recipe);
		}).orElseThrow(() -> new RuntimeException("Recipe not found"));
	}

	@Transactional
	public void deleteRecipe(Long recipeId) {
		recipeRepository.findByIdAndNotDeleted(recipeId).ifPresent(recipe -> {
			recipe.setRecipeIsDeleted("Y"); // 삭제 여부 플래그 변경
			recipe.setRecipeDeleteAt(LocalDateTime.now()); // 삭제 시간 기록
			recipeRepository.save(recipe);
		});
	}

	@Transactional
	public void increaseViewCount(Long recipeId) {
		Recipe findRecipe = getRecipe(recipeId).orElseThrow(() -> new EntityNotFoundException("레시피를 찾을 수 없습니다."));
		findRecipe.setRecipeViewCount(findRecipe.getRecipeViewCount() + 1L);
	}

	// 레시피 중 랜덤 선택
	public Recipe getRandomRecipe(List<Recipe> recipes) {
		Random random = new Random();
		return recipes.get(random.nextInt(recipes.size()));
	}

	// 레시피 제목으로 검색(취향선택용)
	public List<Recipe> findRecipesByTitle(String recipeTitle) {
		return recipeRepository.findByrecipeTitleContaining(recipeTitle);
	}

	// 해시태그로 레시피 검색
	public List<Recipe> findRecipesByHashtag(String hashtagName) {
		// 입력된 해시태그 이름을 가진 해시태그 ID 조회
		Optional<Hashtag> hashtag = hashtagRepository.findByHashName(hashtagName.replace("#", ""));

		if (hashtag.isEmpty()) {
			return Collections.emptyList(); // 해당 해시태그가 없으면 빈 리스트 반환
		}

		// 해당 해시태그를 포함하는 레시피 태그 목록 조회
		List<Long> recipeIds = recipeTagRepository.findByHashId(hashtag.get().getHashId()).stream()
				.map(RecipeTag::getRecipeId).collect(Collectors.toList());

		if (recipeIds.isEmpty()) {
			return Collections.emptyList();
		}

		// 해당 레시피 ID를 가진 레시피 반환
		return recipeRepository.findRecipesByIds(recipeIds);
	}

	// 유형 코드로 레시피 검색
	public List<Recipe> findRecipeCode(Long recipeCode) {
		return recipeRepository.findByRecipeCode(recipeCode);
	}

	public List<Recipe> getLikeRankRecipes() {
		return recipeRepository.findLikeRankRecipes();
	}

}
