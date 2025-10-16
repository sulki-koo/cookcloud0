$(document).ready(function() {
	$("#reviewForm").submit(function(e) {
		e.preventDefault();
		const recipeId = $("#recipeId").val();
		const reviewContent = $("#reviewContent").val();

		$.ajax({
			url: "/reviews/createReview",
			type: "POST",
			contentType: "application/json",  // JSON 형식으로 전송
			data: JSON.stringify({ recipeId, reviewContent }),
			success: function(response) {
				location.reload();
			},
			error: function(xhr, status, error) {
				console.error("Error: ", error);
			}
		});
	});
});

function toggleLike(button, type) {
	let itemId = '';

	if (type == "recipe") {
		itemId = $(button).data("recipe-id");
	} else {
		itemId = $(button).data("review-id");
	}
	// 레시피 ID를 받아옴
	$.post("/likes/liked", {
		type: type,  // 'recipe' 또는 'review'
		itemId: itemId  // 해당 아이템의 ID
	}, function(response) {
		// 서버에서 좋아요 처리 결과가 오면 하트 아이콘 상태 변경
		if (response === "좋아요 추가") {
			$(button).removeClass("bi-heart").addClass("bi-heart-fill text-danger");
			location.reload();
		} else {
			$(button).removeClass("bi-heart-fill text-danger").addClass("bi-heart");
			location.reload();
		}
	});
}

function toggleFollow(button) {
	const followingId = $(button).data("user-id");
	$.post(`/follows/following/${followingId}`, function(response) {
		if (response === "구독 완료") {
			$("#followIcon").removeClass("bi-bell").addClass("bi-bell-fill text-warning");
			location.reload();
		} else {
			$("#followIcon").removeClass("bi-bell-fill text-warning").addClass("bi-bell");
			location.reload();
		}
	});

}
