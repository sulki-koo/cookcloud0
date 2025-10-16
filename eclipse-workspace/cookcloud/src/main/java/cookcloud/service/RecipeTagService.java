package cookcloud.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Hashtag;
import cookcloud.entity.Recipe;
import cookcloud.entity.RecipeTag;
import cookcloud.repository.RecipeTagRepository;

@Service
public class RecipeTagService {

	@Autowired
	private RecipeTagRepository recipeTagRepository;

	@Transactional
	public void saveRecipeTags(Recipe recipe, List<Hashtag> hashtags) {
		// 현재 레시피의 태그들 조회
		List<RecipeTag> existingTags = recipeTagRepository.findByRecipeId(recipe.getRecipeId());

		// 기존 태그의 해시태그 ID 목록을 추출
		List<Long> existingTagIds = existingTags.stream().map(recipeTag -> recipeTag.getHashtag().getHashId())
				.collect(Collectors.toList());

		// 새로 추가할 태그 목록
		List<Long> newTagIds = hashtags.stream().map(Hashtag::getHashId).collect(Collectors.toList());

		// 삭제해야 할 태그 목록 (기존 태그에서 새로 추가된 태그를 제외한 태그들)
		existingTags.stream().filter(recipeTag -> !newTagIds.contains(recipeTag.getHashtag().getHashId()))
				.forEach(recipeTag -> recipeTagRepository.delete(recipeTag)); // 삭제

		// 새로 추가된 태그를 추가
		for (Hashtag hashtag : hashtags) {
			if (!existingTagIds.contains(hashtag.getHashId())) {
				RecipeTag recipeTag = new RecipeTag(recipe.getRecipeId(), hashtag.getHashId());
				recipeTagRepository.save(recipeTag); // 추가
				hashtag.setHashUsageCount(hashtag.getHashUsageCount() + 1L);
			}
		}
	}

}
