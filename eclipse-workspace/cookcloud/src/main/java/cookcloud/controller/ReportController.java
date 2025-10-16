package cookcloud.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.entity.Report;
import cookcloud.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	public Map<CodeId, Code> getReportRoleTypes(HttpServletRequest request) {
		return ((Map<CodeId, Code>) request.getServletContext().getAttribute("codeMap")).entrySet().stream()
				.filter(entry -> entry.getKey().getParentCode() == 7L)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	// 신고 처리
	@PostMapping("/report/{reportType}/{targetId}")
	@ResponseBody
	public String insertReport(@PathVariable String reportType, @PathVariable Long targetId, @RequestBody Report report,
			@AuthenticationPrincipal User user) {

		if ("recipe".equalsIgnoreCase(reportType)) {
			report.setRecipeId(targetId);
		} else if ("review".equalsIgnoreCase(reportType)) {
			report.setReviewId(targetId);
		} else {
			return "잘못된 신고 유형입니다.";
		}

		report.setMemId(user.getUsername()); // 신고한 사용자 정보 유지
		report.setReportReason(report.getReportReason());
		report.setReportCode(report.getReportCode());

		reportService.insertReport(report);
		return "신고가 완료되었습니다.";
	}

	// 신고 관리 페이지
	@GetMapping("/admin/reportManagement")
	public void reportManagement(Model model, HttpServletRequest request) {
		List<Report> reportList = reportService.getReports();
		model.addAttribute("reportList", reportList); // 모델에 데이터 추가
		model.addAttribute("reportCode", getReportRoleTypes(request));
	}
	
	@PostMapping("/admin/updatereportManagement")
	public String updateReportStatusAndRole(@RequestParam String reportId, @RequestParam Long reportCode, @RequestParam String reportIsSend) {
		System.out.println(reportCode + "=====================" + reportIsSend);
		reportService.updateReport(Long.parseLong(reportId), reportCode, reportIsSend.toUpperCase());
		return "redirect:/admin/reportManagement"; // 리다이렉트 처리
	}

}
