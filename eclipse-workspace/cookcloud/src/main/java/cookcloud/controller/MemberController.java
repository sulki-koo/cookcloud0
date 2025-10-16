package cookcloud.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import cookcloud.entity.Member;
import cookcloud.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	// 회원가입 페이지
	@GetMapping("/signup")
	public String showSignupPage(Model model) {
		model.addAttribute("member", new Member());
		return "login/signup"; // signup.html 페이지 반환
	}

	@PostMapping("/member/checkDuplicateId")
	public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestBody Map<String, String> request) {
		Map<String, Boolean> resultMap = new HashMap<>();
		String memId = request.get("memId");
		boolean result = memberService.isDuplicateId(memId);
		resultMap.put("result", result);
		return ResponseEntity.ok(resultMap);
	}

	@PostMapping("/member/checkDuplicateNickname")
	public ResponseEntity<Map<String, Boolean>> checkDuplicateNickname(@RequestBody Map<String, String> request) {
		Map<String, Boolean> resultMap = new HashMap<>();
		String memNickname = request.get("memNickname");
		boolean result = memberService.isDuplicateNickname(memNickname);
		resultMap.put("result", result);
		return ResponseEntity.ok(resultMap);
	}

	// 회원가입 처리
	@PostMapping("/signup")
	@ResponseBody
	public void insertMember(@RequestBody Member member, Model model) {
		member.setMemId(member.getMemId());
		member.setMemPassword(member.getMemPassword());
		member.setMemName(member.getMemName());
		member.setMemNickname(member.getMemNickname());
		member.setMemEmail(member.getMemEmail());
		member.setMemPhone(member.getMemPhone());
		memberService.insertMember(member);
	}
	
	@PutMapping("/mypage/updateMember/{memId}")
	@ResponseBody
	public String updateMember(@PathVariable String memId, @RequestBody Member member) {
		member.setMemId(memId);
		member.setMemName(member.getMemName());
		member.setMemNickname(member.getMemNickname());
		member.setMemEmail(member.getMemEmail());
		member.setMemPhone(member.getMemPhone());
		memberService.updateMember(member);
		return "mypage";
	}

	@PutMapping("/mypage/deleteMember/{memId}")
	public String deleteMember(@PathVariable String memId) {
		memberService.deleteMember(memId);
		return "home";
	}

}
