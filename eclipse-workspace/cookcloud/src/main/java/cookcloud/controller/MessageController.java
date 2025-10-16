package cookcloud.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	public Map<CodeId, Code> getMessageTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 3L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)); // 필터링된 코드만 반환
	}

	// 메시지 읽음 처리
	@PutMapping("/message/read/{messageId}")
	@ResponseBody
	public void markMessageAsRead(@PathVariable Long messageId) {
		messageService.markMessageAsRead(messageId);
	}

	// 메시지 삭제 처리
	@PutMapping("/message/delete/{messageId}")
	@ResponseBody
	public void deleteMessage(@PathVariable Long messageId) {
		messageService.deleteMessage(messageId);
	}

	// 메시지 전송 폼 화면
	@GetMapping("/admin/messageManagement")
	public String showMessageForm(Model model, HttpServletRequest request) {
		model.addAttribute("messageCodes", getMessageTypes(request));
		return "admin/messageManagement";
	}

	// 메시지 전송 처리
	@PostMapping("/admin/messageManagement")
	public String sendMessage(@RequestParam String memId, @RequestParam String messageTitle,
			@RequestParam String messageContent, @RequestParam Long messageCode,
			RedirectAttributes redirectAttributes) {
		boolean isSuccess = messageService.insertMessage(memId, messageTitle, messageContent, messageCode);
		if (isSuccess) {
			redirectAttributes.addFlashAttribute("successMessage", "메시지가 성공적으로 전송되었습니다!");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "메시지 전송에 실패했습니다. 다시 시도해주세요.");
		}
		return "redirect:/admin/messageManagement"; // 메시지 전송 후 다시 폼으로 리다이렉트
	}

}
