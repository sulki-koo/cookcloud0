package cookcloud.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cookcloud.entity.Report;
import cookcloud.repository.ReportRepository;

@Service
public class ReportService {

	@Autowired
	private ReportRepository reportRepository;

	public List<Report> getReports() {
		return reportRepository.findAll(); // 리포트 리스트 반환
	}

	@Transactional
	public void insertReport(Report report) {
		report.setReportSendAt(LocalDateTime.now());
		report.setReportIsProc("N");
		report.setReportIsSend("N");
		reportRepository.save(report);
	}
	
	@Transactional
	public void updateReport(Long reportId, Long reportCode, String reportIsSend) {
		// 신고 처리 상태 및 신고 유형 수정
		Report report = reportRepository.findById(reportId).orElse(null);
		report.setReportCode(reportCode);
		report.setReportIsProc("Y");
		report.setReportProcAt(LocalDateTime.now());
		report.setReportIsSend(reportIsSend);
		reportRepository.save(report);
	}

}
