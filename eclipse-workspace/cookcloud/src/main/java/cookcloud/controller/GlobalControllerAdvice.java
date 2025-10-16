package cookcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cookcloud.entity.Member;
import cookcloud.service.MemberService;

@ControllerAdvice
public class GlobalControllerAdvice {

	@Autowired
	private MemberService memberService;

	@ModelAttribute("loggedMember")
	public Member loggedMember(@AuthenticationPrincipal User user) {
		if (user != null) {
			Member member = memberService.getMember(user.getUsername()).orElse(null);
			return member;
		}
		return null;
	}
	
}
