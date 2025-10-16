package cookcloud.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cookcloud.CookcloudApplication;
import cookcloud.entity.Review;
import cookcloud.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
    private ReviewRepository reviewRepository;

	// 내가 작성한 리뷰 조회
    public List<Review> getMyReviews(String memId) {
        return reviewRepository.findByMemId(memId);
    }

	public List<Review> getReviews(Long recipeId) {
		return reviewRepository.findByRecipeId(recipeId);
	}
	
	public Optional<Review> getReview(Long reviewId){
		return reviewRepository.findByReviewId(reviewId);
	}
	
	@Transactional
	public void insertReview(Review review) {
		review.setReviewInsertAt(LocalDateTime.now());
		review.setReviewIsDeleted("N");
		reviewRepository.save(review);
	}
	
	@Transactional
	public void updateReview(Review review) {
		Review findReview = getReview(review.getReviewId()).get();
		System.out.println(review.getReviewContent() + "서비스");
		findReview.setReviewContent(review.getReviewContent());
		findReview.setReviewUpdateAt(LocalDateTime.now());
		findReview.setReviewIsDeleted("N");
		reviewRepository.save(findReview);
	}
	
	@Transactional
	public void deleteReview(Long reviewId) {
		Review findReview = getReview(reviewId).get();
		findReview.setReviewDeleteAt(LocalDateTime.now());
		findReview.setReviewIsDeleted("Y");
		reviewRepository.save(findReview);
	}

}
