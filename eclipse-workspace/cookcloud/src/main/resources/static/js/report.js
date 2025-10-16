function showReportPopup(button, type) {
	let targetId;

	if (type === 'recipe') {
		targetId = button.getAttribute('data-recipe-id');
	} else if (type === 'review') {
		targetId = button.getAttribute('data-review-id');
	}

	document.getElementById('targetId').value = targetId;
	document.getElementById('reportType').value = type;

	// 폼 action 설정
	let form = document.getElementById('reportForm');
	form.action = `/report/${type}/${targetId}`;

	// 팝업과 오버레이 표시 + 부드러운 애니메이션
	document.getElementById('reportOverlay').style.display = 'block';
	let modal = document.getElementById('reportModal');

	modal.style.display = 'block';
	setTimeout(() => {
		modal.style.opacity = '1';
	}, 10);

}

function closeReportPopup() {
	let modal = document.getElementById('reportModal');
	modal.style.opacity = '0';
	setTimeout(() => {
		modal.style.display = 'none';
		document.getElementById('reportOverlay').style.display = 'none';
	}, 300);
}

document.getElementById("reportForm").addEventListener("submit", function(event) {
	event.preventDefault(); // 기본 제출 동작 막기

	let reportType = $('#reportType').val();
	let targetId = $('#targetId').val();
	let reportCode = $('#reportCodeType').val(); // 신고 사유
	let reportReason = $('#reportReason').val();

	let reportData = {
		reportCode: reportCode,
		reportReason: reportReason
	};

	$.ajax({
		url: '/report/' + reportType + '/' + targetId,
		method: "POST",
		contentType: "application/json",
		data: JSON.stringify(reportData), // JSON 형식으로 데이터 전송
		success: function() {
			alert("신고가 접수되었습니다.");
			closeReportPopup();
			location.reload();
		},
		error: function(xhr, status, error) {
			alert("신고 처리 중 오류가 발생했습니다.");
		}
	});
});
