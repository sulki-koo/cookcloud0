$(document).ready(function() {
	// 수정 버튼 클릭 시
	$(".edit-review-btn").click(function() {
		let reviewItem = $(this).closest(".review-item"); // 현재 클릭한 리뷰 영역 찾기
		let content = reviewItem.find(".review-content"); // 기존 리뷰 내용
		let editBox = reviewItem.find(".edit-review-content"); // 수정 입력창
		let saveBtn = reviewItem.find(".save-review-btn"); // 저장 버튼

		// 기존 내용을 textarea에 넣고, 요소 보이기/숨기기
		editBox.val(content.text()).show();
		saveBtn.show();
		content.hide();
		$(this).hide(); // 수정 버튼 숨기기
	});

	// 저장 버튼 클릭 시 AJAX 요청 보내기
	$(".save-review-btn").click(function() {
		let reviewItem = $(this).closest(".review-item");
		let reviewId = reviewItem.data("review-id");
		let newContent = reviewItem.find(".edit-review-content").val(); // 입력된 새 리뷰 내용
		$.ajax({
			url: "/reviews/updateReview/" + reviewId,
			type: "PUT",
			contentType: "application/json",
			data: JSON.stringify({ reviewId: reviewId, reviewContent: newContent }),
			success: function(response) {
				location.reload();
			},
			error: function(xhr, status, error) {
				alert("수정 실패: " + error);
			}
		});
	});
});

function deleteReview(reviewId) {
	if (confirm("정말 삭제하시겠습니까?")) {
		$.ajax({
			url: "/reviews/deleteReview/" + reviewId,
			type: "PUT",
			success: function(response) {
				alert("리뷰가 삭제되었습니다.");
				location.reload();
			},
			error: function(xhr, status, error) {
				alert("삭제 실패: " + error);
			}
		});
	}
}