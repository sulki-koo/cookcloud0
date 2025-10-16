package cookcloud.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.service.NoticeService;
import cookcloud.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private RecipeService recipeService;

	public Map<CodeId, Code> getRecipeTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 5L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // 필터링된 코드만 반환
	}

	// 메인 페이지
	@GetMapping
	public String mainPage(Model model, HttpServletRequest request) {
		model.addAttribute("likeRanks", recipeService.getLikeRankRecipes());
		model.addAttribute("notices", noticeService.getNotices());
		model.addAttribute("recipeCodes", getRecipeTypes(request));
		return "home"; // main.html
	}

	// 로그인 페이지
	@GetMapping("/login")
	public String login() {
		// 로그인 페이지로 리다이렉트
		return "login/login"; // login.html 페이지를 렌더링
	}

	// 취향 선택 추천 페이지
	@GetMapping("/choice")
	public String recommendationPage() {
		return "choice"; // recommendation.html
	}

}
