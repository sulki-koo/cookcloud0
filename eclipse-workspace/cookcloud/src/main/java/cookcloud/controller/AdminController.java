package cookcloud.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.entity.Member;
import cookcloud.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private MemberService memberService;
	
	// 계정상태: parentCode가 1인 항목
	public Map<CodeId, Code> getMemberStatusTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 1L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	// 회원등급: parentCode가 2인 항목
	public Map<CodeId, Code> getMemberRoleTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 2L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@GetMapping
	public String administrator() {
		return "admin/administrator";
	}
	
	@GetMapping("/memManagement")
	public void memManagement(Model model, HttpServletRequest request) {
		List<Member> memberList = memberService.getMembersAdmin(); // 회원 리스트 조회
		model.addAttribute("memberList", memberList);
		model.addAttribute("statusTypes", getMemberStatusTypes(request));
		model.addAttribute("roleTypes", getMemberRoleTypes(request));
	}

	@PostMapping("/updateMemManagement")
	public String updateMemberStatusAndRole(@RequestParam String memId,
			@RequestParam Long memStatusCode, @RequestParam Long roleCode) {
		// 서비스 메서드 호출하여 상태코드와 등급코드 수정
		memberService.updateMemberStatusAndRole(memId, memStatusCode, roleCode);
		return "redirect:/admin/memManagement"; // 수정 후 회원 목록 페이지로 리다이렉트
	}

}
