package cookcloud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cookcloud.entity.Member;
import cookcloud.entity.Message;
import cookcloud.entity.Recipe;
import cookcloud.entity.Review;
import cookcloud.service.FollowsService;
import cookcloud.service.MemberService;
import cookcloud.service.MessageService;
import cookcloud.service.RecipeService;
import cookcloud.service.ReviewService;

@Controller
public class MyPageController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private FollowsService followsService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private MessageService messageService;

	@GetMapping("/@{nickname}")
	public String viewPage(@PathVariable String nickname, Model model, @AuthenticationPrincipal User user) {

		Member loggedMember = memberService.getMember(user.getUsername()).get();
		Member findMember = memberService.findByMemNickname(nickname).get();
		String memId = findMember.getMemId();

		// 필요한 데이터 조회
		List<Recipe> myRecipes = recipeService.getMyRecipes(memId);
		List<Member> followings = followsService.getMyFollowings(memId);
		List<Member> followers = followsService.getMyFollowers(memId);

		if (loggedMember == findMember) {
			List<Recipe> likedRecipes = recipeService.getLikedRecipes(memId);
			List<Review> myReviews = reviewService.getMyReviews(memId);
			List<Message> messages = messageService.getMessages(memId);
			
			model.addAttribute("likedRecipes", likedRecipes);
			model.addAttribute("myReviews", myReviews);
			model.addAttribute("messages", messages);
			model.addAttribute("isOwnPage", true); // 본인 페이지
		} else {
			model.addAttribute("isOwnPage", false); // 다른 사람 페이지
		}

		// 모델에 데이터 전달
		model.addAttribute("loggedMember", loggedMember);
		model.addAttribute("member", findMember);
		model.addAttribute("nickname", nickname);
		model.addAttribute("myRecipes", myRecipes);
		model.addAttribute("followings", followings);
		model.addAttribute("followers", followers);

		return "mypage/main"; // Thymeleaf 템플릿 이름
	}

}
