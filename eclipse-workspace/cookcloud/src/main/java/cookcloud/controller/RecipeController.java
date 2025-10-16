package cookcloud.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.entity.Hashtag;
import cookcloud.entity.Member;
import cookcloud.entity.Recipe;
import cookcloud.entity.RecipeTag;
import cookcloud.entity.Review;
import cookcloud.service.AttachmentService;
import cookcloud.service.FollowsService;
import cookcloud.service.HashtagService;
import cookcloud.service.LikesService;
import cookcloud.service.MemberService;
import cookcloud.service.MessageService;
import cookcloud.service.RecipeService;
import cookcloud.service.RecipeTagService;
import cookcloud.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private LikesService likesService;

	@Autowired
	private FollowsService followsService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private HashtagService hashtagService;

	@Autowired
	private RecipeTagService recipeTagService;

	@Autowired
	private AttachmentService attachmentService;

	public Map<CodeId, Code> getRecipeTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 5L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // 필터링된 코드만 반환
	}

	public Map<CodeId, Code> getReportTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 7L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<CodeId, Code> getAttachTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 8L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@GetMapping
	public String getRecipes(@RequestParam(value = "searchRecipe", required = false) String searchRecipe,
			@RequestParam(value = "recipeType", required = false) String recipeCode, Model model,
			HttpServletRequest request) {

		List<Recipe> recipes;

		if (searchRecipe != null && !searchRecipe.isBlank()) {
			// 제목 검색과 해시태그 검색을 동시에 처리
			List<Recipe> titleResults = recipeService.findRecipesByTitle(searchRecipe);
			List<Recipe> hashtagResults = recipeService.findRecipesByHashtag(searchRecipe);

			// 두 리스트 합치고 중복 제거
			recipes = Stream.concat(titleResults.stream(), hashtagResults.stream()).distinct()
					.collect(Collectors.toList());
		} else if (recipeCode != null && !recipeCode.isBlank()) {
			try {
				recipes = recipeService.findRecipeCode(Long.parseLong(recipeCode));
			} catch (NumberFormatException e) {
				// 숫자가 아닌 값이 들어오면 전체 레시피 반환
				recipes = recipeService.getRecipes();
			}
		} else {
			recipes = recipeService.getRecipes();
		}

		List<Hashtag> topHashtags = hashtagService.getTopHashtags();
		model.addAttribute("recipeTypes", getRecipeTypes(request));
		model.addAttribute("recipes", recipes);
		model.addAttribute("topHashtags", topHashtags);
		return "recipe/list";
	}

	@GetMapping("/recipe/{recipeId}")
	public String getRecipe(@PathVariable Long recipeId, Model model, HttpServletRequest request, HttpSession session,
			@AuthenticationPrincipal User user) {
		Set<Long> viewedRecipes = (Set<Long>) session.getAttribute("viewedRecipes");
		if (viewedRecipes == null) {
			viewedRecipes = new HashSet<>();
		}

		if (!viewedRecipes.contains(recipeId)) {
			recipeService.increaseViewCount(recipeId);
			viewedRecipes.add(recipeId);
			session.setAttribute("viewedRecipes", viewedRecipes);
		}

		Recipe recipe = recipeService.getRecipe(recipeId).get();
		Member member = memberService.getMember(recipe.getMemId()).get();

		List<Review> reviews = reviewService.getReviews(recipeId);
		List<String> hashtagNames = recipe.getRecipeTagList().stream().map(RecipeTag::getHashtagName)
				.collect(Collectors.toList());

		// 로그인한 사용자 정보
		String memId = (user != null) ? user.getUsername() : null;

		int recipeLikeCount = likesService.getRecipeLikeCount(recipeId);

		// 레시피 좋아요 여부
		boolean liked = (memId != null) && likesService.isLikedRecipeMemId(recipeId, memId)
				.map(like -> "Y".equals(like.getLikeIsLiked())).orElse(false);

		// 리뷰 좋아요 여부
		Map<Long, Boolean> reviewLikes = new HashMap<>();
		for (Review review : reviews) {
			boolean reviewLiked = (memId != null) && likesService.isLikedReviewMemId(review.getReviewId(), memId)
					.map(like -> "Y".equals(like.getLikeIsLiked())).orElse(false);
			reviewLikes.put(review.getReviewId(), reviewLiked);
		}

		Map<Long, Integer> reviewLikeCounts = new HashMap<>();
		for (Review review : reviews) {
			int likeCount = reviewLikes.getOrDefault(review.getReviewId(), false) ? 1 : 0;
			reviewLikeCounts.put(review.getReviewId(),
					reviewLikeCounts.getOrDefault(review.getReviewId(), 0) + likeCount);
		}

		// 현재 사용자가 이 레시피를 구독했는지 확인
		boolean following = (memId != null) && followsService.isFollowing(recipe.getMemId(), memId)
				.map(follow -> "Y".equals(follow.getFollowIsFollowing())).orElse(false);

		model.addAttribute("recipeTypes", getRecipeTypes(request));
		model.addAttribute("recipe", recipe);
		model.addAttribute("reviews", reviews);
		model.addAttribute("hashtagNames", hashtagNames);
		model.addAttribute("nickname", member.getMemNickname());
		model.addAttribute("liked", liked);
		model.addAttribute("recipeLikeCount", recipeLikeCount);
		model.addAttribute("reviewLikes", reviewLikes);
		model.addAttribute("reviewLikeCounts", reviewLikeCounts);
		model.addAttribute("following", following);
		model.addAttribute("reportCodeTypes", getReportTypes(request));
		return "recipe/view";
	}

	// 레시피 유형 가져오기 (동적으로 코드맵에서 가져오기)
	@GetMapping("/create")
	public String createRecipeForm(Model model, HttpServletRequest request) {
		model.addAttribute("recipeTypes", getRecipeTypes(request));
		return "recipe/create";
	}

	@PostMapping("/create")
	public String createRecipe(Recipe recipe, @RequestParam String memId, @RequestParam String recipeTitle,
			@RequestParam String recipeContent, @RequestParam String recipeCode,
			@RequestParam(required = false) String hashtags, @RequestParam(required = false) MultipartFile attachment,
			HttpServletRequest request) throws IOException {

		Long longRecipeCode = Long.parseLong(recipeCode);

		recipe.setRecipeTitle(recipeTitle);
		recipe.setRecipeContent(recipe.getRecipeContent().replace("\n", "<br/>"));
		recipe.setRecipeCode(longRecipeCode);
		recipe.setMemId(memId);

		Recipe savedRecipe = recipeService.createRecipe(recipe);
		messageService.notifyFollowersNewRecipe(memId);

		// 해시태그 저장 로직 추가
		if (hashtags != null && !hashtags.isEmpty()) {
			List<String> hashtagList = List.of(hashtags.split(",")); // 쉼표로 구분된 해시태그를 리스트로 변환
			List<Hashtag> savedHashtags = hashtagService.saveOrUpdateHashtags(hashtagList); // 해시태그 저장 또는 업데이트
			recipeTagService.saveRecipeTags(savedRecipe, savedHashtags); // 레시피와 해시태그 관계 저장
		}

		if (attachment != null && !attachment.isEmpty()) {
			attachmentService.saveAttachment(savedRecipe.getRecipeId(), attachment, getAttachTypes(request));
		}

		return "redirect:/recipes";
	}

	@GetMapping("/update/{recipeId}")
	public String updateRecipeForm(@PathVariable Long recipeId, Model model, HttpServletRequest request) {
		Recipe recipe = recipeService.getRecipe(recipeId).orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));

		List<String> hashtagNames = recipe.getRecipeTagList().stream().map(RecipeTag::getHashtagName)
				.collect(Collectors.toList());
		
		recipe.setRecipeContent(recipe.getRecipeContent().replace("<br/>", "\n"));

		model.addAttribute("recipe", recipe);
		model.addAttribute("recipeTypes", getRecipeTypes(request));
		model.addAttribute("hashtagNames", hashtagNames);
		return "recipe/update"; // 수정 페이지로 이동
	}

	@PostMapping("/update/{recipeId}")
	public String updateRecipe(@PathVariable Long recipeId, @ModelAttribute Recipe recipe,
			@RequestParam(required = false) String hashtags, @RequestParam(required = false) MultipartFile attachment,
			@RequestParam(required = false) Long deleteAttachmentId, HttpServletRequest request) throws IOException {
		Recipe savedRecipe = recipeService.getRecipe(recipeId)
				.orElseThrow(() -> new IllegalArgumentException("레시피 없음"));

		recipe.setRecipeContent(recipe.getRecipeContent().replace("\n", "<br/>"));
		recipeService.updateRecipe(recipeId, recipe);

		// 해시태그 업데이트 (기존 태그 삭제 및 새로운 태그 추가는 서비스에서 자동 처리)
		if (hashtags != null && !hashtags.isEmpty()) {
			List<String> hashtagList = List.of(hashtags.split(","));
			List<Hashtag> savedHashtags = hashtagService.saveOrUpdateHashtags(hashtagList);
			recipeTagService.saveRecipeTags(savedRecipe, savedHashtags);
		}
		
		if (deleteAttachmentId != null) {
            attachmentService.deleteAttachment(deleteAttachmentId);
        }
		
		if (attachment != null && !attachment.isEmpty()) {
            attachmentService.saveAttachment(recipeId, attachment, getAttachTypes(request));
        }
		
		return "redirect:/recipes/recipe/" + recipeId;
	}

	@PostMapping("/delete/{recipeId}")
	public String deleteRecipe(@PathVariable Long recipeId) {
		recipeService.deleteRecipe(recipeId);
		return "redirect:/recipes"; // 삭제 후 레시피 리스트 페이지로 이동
	}

}
