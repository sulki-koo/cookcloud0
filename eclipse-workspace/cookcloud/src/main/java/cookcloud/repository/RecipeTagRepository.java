package cookcloud.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cookcloud.entity.RecipeTag;
import cookcloud.entity.RecipeTagId;

public interface RecipeTagRepository extends JpaRepository<RecipeTag, RecipeTagId> {

	// 레시피 ID로 해시태그 아이디들 조회
    List<RecipeTag> findByRecipeId(Long recipeId);
    
    List<RecipeTag> findByHashId(Long hashId);
    
}
