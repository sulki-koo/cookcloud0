package cookcloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cookcloud.entity.Member;
import cookcloud.service.FollowsService;
import cookcloud.service.MemberService;
import cookcloud.service.MessageService;

@Controller
@RequestMapping("/follows")
public class FollowsController {

	@Autowired
	private FollowsService followsService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MessageService messageService;

	@PostMapping("/following/{followingId}")
	public ResponseEntity<String> toggleFollow(@PathVariable String followingId, @AuthenticationPrincipal User user) {
		Member member = memberService.getMember(user.getUsername()).get();
		boolean following = followsService.toggleFollow(followingId, member.getMemId());
		if (following) {
			if (member != null) {
				messageService.followMessage(followingId, member.getMemNickname());
			}
		}
		return ResponseEntity.ok(following ? "구독 완료" : "구독 취소");
	}

}
