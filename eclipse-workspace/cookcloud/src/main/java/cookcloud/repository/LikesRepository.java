package cookcloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cookcloud.entity.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long>{

	@Query("SELECT l.recipeId FROM Likes l WHERE l.memId = :memId AND l.recipeId IS NOT NULL AND l.likeIsLiked = 'Y'")
	List<Long> findRecipeIdsByMemId(@Param("memId") String memId);
	
	@Query("SELECT l FROM Likes l WHERE l.recipeId = :recipeId AND l.memId = :memId AND l.likeIsLiked = 'Y'")
	Optional<Likes> findByRecipeIdAndMemId(@Param("recipeId") Long recipeId, @Param("memId") String memId);
	
	@Query("SELECT l FROM Likes l WHERE l.reviewId = :reviewId AND l.memId = :memId AND l.likeIsLiked = 'Y'")
	Optional<Likes> findByReviewIdAndMemId(@Param("reviewId") Long reviewId, @Param("memId") String memId);

	@Query("SELECT l FROM Likes l WHERE l.recipeId = :recipeId AND l.memId = :memId")
	Optional<Likes> findByIsRecipeIdAndMemId(@Param("recipeId") Long recipeId, @Param("memId") String memId);
	
	@Query("SELECT l FROM Likes l WHERE l.reviewId = :reviewId AND l.memId = :memId")
	Optional<Likes> findByIsReviewIdAndMemId(@Param("reviewId") Long reviewId, @Param("memId") String memId);
	
    @Query("SELECT COUNT(l) FROM Likes l WHERE l.recipeId = :recipeId AND l.likeIsLiked = 'Y'")
	int countByRecipeId(Long recipeId);
	
}
