package cookcloud.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Likes;
import cookcloud.repository.LikesRepository;

@Service
public class LikesService {

	@Autowired
	private LikesRepository likesRepository;

	// 좋아요한 레시피 조회
	public List<Long> getLikedRecipeIds(String memId) {
	    return likesRepository.findRecipeIdsByMemId(memId)
	                          .stream()
	                          .filter(Objects::nonNull)  // null 제거
	                          .collect(Collectors.toList());
	}
	
	public int getRecipeLikeCount(Long recipeId) {
	    return likesRepository.countByRecipeId(recipeId);
	}
	
	public Optional<Likes> isLikedRecipeMemId(Long recipeId, String memId) {
	    return likesRepository.findByRecipeIdAndMemId(recipeId, memId);
	}
	
	public Optional<Likes> isLikedReviewMemId(Long reviewId, String memId) {
	    return likesRepository.findByReviewIdAndMemId(reviewId, memId);
	}
	
	// 레시피 좋아요 토글
	public boolean toggleLikeRecipe(Long recipeId, String memId) {
		return toggleLike(recipeId, memId, "recipe");
	}

	// 리뷰 좋아요 토글
	public boolean toggleLikeReview(Long reviewId, String memId) {
		return toggleLike(reviewId, memId, "review");
	}

	@Transactional
	public boolean toggleLike(Long itemId, String memId, String type) {
		Optional<Likes> like;

		// 레시피일 경우
		if ("recipe".equals(type)) {
			like = likesRepository.findByIsRecipeIdAndMemId(itemId, memId);
		}
		// 리뷰일 경우
		else if ("review".equals(type)) {
			like = likesRepository.findByIsReviewIdAndMemId(itemId, memId);
		} else {
			throw new IllegalArgumentException("Invalid type: " + type);
		}

		// 좋아요가 이미 존재하면
		if (like.isPresent()) {
			Likes existingLike = like.get();
			if ("Y".equals(existingLike.getLikeIsLiked())) {
				existingLike.setLikeIsLiked("N"); // 좋아요 취소
				likesRepository.save(existingLike);
				return false;
			} else {
				existingLike.setLikeIsLiked("Y"); // 다시 좋아요
				likesRepository.save(existingLike);
				return true;
			}
		} else {
			// 좋아요가 없으면 새로 생성
			Likes newLike = new Likes();
			if ("recipe".equals(type)) {
				newLike.setRecipeId(itemId);
			} else if ("review".equals(type)) {
				newLike.setReviewId(itemId);
			}
			newLike.setMemId(memId);
			newLike.setLikeIsLiked("Y"); // 처음 좋아요 등록
			likesRepository.save(newLike);
			return true;
		}
	}

}
