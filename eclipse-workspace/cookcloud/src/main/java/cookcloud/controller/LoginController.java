package cookcloud.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cookcloud.entity.Member;
import cookcloud.service.MemberService;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private MemberService memberService;

	// 아이디 찾기 페이지
	@GetMapping("/findId")
	public String findIdForm() {
		return "login/findIdForm"; // 아이디 찾기 폼 화면
	}

	// 아이디찾기
	@PostMapping("/findId")
	@ResponseBody
	public Map<String, Object> findMemberId(@RequestParam String memEmail, @RequestParam String memPhone) {
		String memberId = memberService.findID(memEmail, memPhone); // 이메일과 전화번호로 아이디 찾기

		Map<String, Object> response = new HashMap<>();
		if (memberId != null) {
			String maskedId = memberService.maskMemberId(memberId); // 아이디 앞 3자리 마스킹 처리
			response.put("memberId", maskedId); // 마스킹된 아이디 반환
		} else {
			response.put("error", "일치하는 회원 정보가 없습니다."); // 에러 메시지 전달
		}

		return response; // JSON 형태로 응답 반환
	}

	// 비밀번호 찾기 페이지
	@GetMapping("/findpassword")
	public String findpasswordForm(Model model) {
		model.addAttribute("member", new Member());
		return "login/findpasswordForm";
	}

	// 비밀번호 찾기 처리
	@PostMapping("/findpassword")
	public String findMemberPassword(@RequestParam String memId, @RequestParam String memEmail,
			@RequestParam String memPhone, Model model) {
		memId = memberService.findPassword(memId, memEmail, memPhone);
		if (memId == null || memId.trim().isEmpty() || memId.equals("일치하는 회원 정보가 없습니다.")) {
			return "login/findpassword";
		}
		model.addAttribute("memId", memId);
		System.out.println(memId);
		return "login/updatePass";
	}

	@PostMapping("/updatePass")
	public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> requestData) {
		String memId = requestData.get("memId");
		String memPassword = requestData.get("memPassword");

		System.out.println(memId);

		memberService.updatePass(memId, memPassword);
		return ResponseEntity.ok("비밀번호 변경 성공");
	}
}
