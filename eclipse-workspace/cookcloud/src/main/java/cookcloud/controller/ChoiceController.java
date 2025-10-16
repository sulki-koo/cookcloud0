package cookcloud.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import cookcloud.entity.Recipe;
import cookcloud.openapi.ChoiceSearchApi;
import cookcloud.service.RecipeService;

@Controller
public class ChoiceController {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private ChoiceSearchApi choiceSearchApi;

	@CrossOrigin(origins = "*")
	@PostMapping("/choiceResult")
	public String getRecommendation(@RequestBody(required = false) Map<String, Object> choices, Model model) {

//    	System.out.println("Received choices: " + choices);

		if (choices == null || choices.isEmpty()) {
			return "error";
		}

		try {
			String prompt = choices.toString() + " 이 조건으로 메뉴 한가지만 단어로 대답해줘";
			String aiMenu = choiceSearchApi.getChatGPTResponse(prompt);

			List<Recipe> matchingRecipes = recipeService.findRecipesByTitle(aiMenu);
//            System.out.println("챗지피티 메뉴 :" + aiMenu);

			if (!matchingRecipes.isEmpty()) {
				Recipe selectedRecipe = recipeService.getRandomRecipe(matchingRecipes);
				selectedRecipe.setRecipeContent(selectedRecipe.getRecipeContent().replace("<br/>", "\n"));
				model.addAttribute("recipe", selectedRecipe);
				model.addAttribute("isAIResult", false); // DB 결과
//                System.out.println("db 메뉴 :" + selectedRecipe);
			} else {

				// 중복 호출 방지
				Set<String> previousPrompts = new HashSet<>();
				if (previousPrompts.contains(aiMenu)) {
//                    System.out.println("같은 요청 반복 감지: " + aiMenu);
					return "error";
				}
				previousPrompts.add(aiMenu);

				String recipePrompt = aiMenu + " 레시피를 간략하게 3줄로 요약해서 알려줘. " + "형식: 메뉴이름 : 메뉴명, 레시피는 한 줄씩 줄바꿈하여 작성";

				String newRecipe = choiceSearchApi.getChatGPTResponse(recipePrompt);
				model.addAttribute("recipe", newRecipe);
				model.addAttribute("isAIResult", true); // AI 결과
//              System.out.println("ai 메뉴 :" + newRecipe);
			}

			return "choiceResult";

		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
	}

}
