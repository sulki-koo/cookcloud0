package cookcloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cookcloud.entity.Notice;
import cookcloud.service.NoticeService;

@Controller
public class NoticeController {

	@Autowired
	private NoticeService noticeService;

	// 공지 목록 조회 (모든 공지 조회)
	@GetMapping("/admin/noticeManagement")
	public String getNotices(Model model) {
		List<Notice> noticeList = noticeService.getNoticeAdmin();
		model.addAttribute("noticeList", noticeList);
		return "admin/noticeManagement";
	}

	// 공지 추가 처리
	@PostMapping("/admin/noticeManagement")
	public String insertNotice(@RequestParam String noticeTitle, @RequestParam String noticeContent,
			@RequestParam String noticeAnswerName) {
		Notice notice = new Notice();
		
		notice.setNoticeTitle(noticeTitle);
		notice.setNoticeContent(noticeContent.replace("\n", "<br/>"));
		notice.setNoticeAnswerName(noticeAnswerName);
		noticeService.insertNotice(notice);
		return "redirect:/admin/noticeManagement";
	}

	@PostMapping("/admin/updateNotice")
	@ResponseBody
	public Map<String, Object> updateNotice(@RequestBody Notice notice) {
		Map<String, Object> response = new HashMap<>();
		notice.setNoticeContent(notice.getNoticeContent().replace("\n", "<br/>"));
		boolean isSuccess = noticeService.updateNotice(notice);
		if (isSuccess) {
			response.put("success", false);
		}
		response.put("success", true);
		return response;

	}

	@PostMapping("/admin/deleteNotice/{noticeId}")
	@ResponseBody
	public Map<String, Object> deleteNotice(@PathVariable Long noticeId) {
		Map<String, Object> response = new HashMap<>();
		boolean isSuccess = noticeService.deleteNotice(noticeId);
		if (isSuccess) {
			response.put("success", false);
		}
		response.put("success", true);
		return response;
	}

}
