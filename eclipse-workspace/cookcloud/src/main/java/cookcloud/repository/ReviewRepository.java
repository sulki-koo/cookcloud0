package cookcloud.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cookcloud.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
	
	@Query("SELECT r FROM Review r WHERE r.memId = :memId AND r.reviewIsDeleted = 'N' ORDER BY r.reviewId DESC")
	List<Review> findByMemId(@Param("memId") String memId);
	
	@Query("SELECT r FROM Review r WHERE r.recipeId = :recipeId AND r.reviewIsDeleted = 'N' ORDER BY r.reviewId DESC")
	List<Review> findByRecipeId(@Param("recipeId")Long recipeId);
	
	@Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId AND r.reviewIsDeleted = 'N'")
	Optional<Review> findByReviewId(@Param("reviewId")Long reviewId);
	
}
