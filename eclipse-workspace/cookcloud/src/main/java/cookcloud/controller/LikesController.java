package cookcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cookcloud.entity.Member;
import cookcloud.repository.RecipeRepository;
import cookcloud.repository.ReviewRepository;
import cookcloud.service.LikesService;
import cookcloud.service.MemberService;
import cookcloud.service.MessageService;
import cookcloud.service.RecipeService;
import cookcloud.service.ReviewService;

@Controller
@RequestMapping("/likes")
public class LikesController {

	@Autowired
	private LikesService likesService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private ReviewService reviewService;

	@PostMapping("/liked")
	@ResponseBody
	public ResponseEntity<String> toggleLike(@RequestParam String type, @RequestParam Long itemId,
			@AuthenticationPrincipal User user) {

		boolean liked = false;
		String sendToMemId = "";

		// 레시피 좋아요
		if ("recipe".equals(type)) {
			liked = likesService.toggleLikeRecipe(itemId, user.getUsername());
			sendToMemId = recipeService.getRecipe(itemId).get().getMemId();
		}
		// 리뷰 좋아요
		else if ("review".equals(type)) {
			liked = likesService.toggleLikeReview(itemId, user.getUsername());
			sendToMemId = reviewService.getReview(itemId).get().getMemId();
		} else {
			throw new IllegalArgumentException("Invalid type: " + type);
		}

		if (liked) {
			Member member = memberService.getMember(user.getUsername()).get();
			if (member != null) {
				messageService.likeMessage(sendToMemId, member.getMemNickname(), type); // 공통 메시지 메소드 호출
			}
		}

		return ResponseEntity.ok(liked ? "좋아요 추가" : "좋아요 취소");

	}

}
