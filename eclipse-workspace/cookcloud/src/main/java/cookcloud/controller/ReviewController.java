package cookcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cookcloud.entity.Review;
import cookcloud.service.ReviewService;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/{recipeId}")
	public void getReviews(@PathVariable Long recipeId, Model model) {
		List<Review> reviews = reviewService.getReviews(recipeId);
		model.addAttribute("reviews", reviews);
	}

	@PostMapping("/createReview")
	@ResponseBody
	public String insertReview(@RequestBody Review review, @AuthenticationPrincipal User user) {
		review.setMemId(user.getUsername());
		review.setReviewContent(review.getReviewContent().replace("\n", "<br/>"));
		reviewService.insertReview(review);
		return "recipe/view";
	}

	@PutMapping("/updateReview/{reviewId}")
	@ResponseBody
	public String updateReview(@PathVariable Long reviewId, @RequestBody Review review) {
		review.setReviewId(reviewId);
		reviewService.updateReview(review);
		return "recipe/view";
	}
	
	@PutMapping("/deleteReview/{reviewId}")
	@ResponseBody
	public String deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
		return "recipe/view";
	}
	
}
