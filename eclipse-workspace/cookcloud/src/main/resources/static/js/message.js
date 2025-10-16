$(document).ready(function() {

	$(".messageTitle").each(function() {
		let messageId = $(this).data("id");
		let storedState = localStorage.getItem("messageToggleState_" + messageId);
		if (storedState === "open") {
			$(this).siblings(".messageContent").show(); // 저장된 상태에 맞게 토글 열기
		}
	});

	$(".messageTitle").on("click", function() {
		let messageId = $(this).data("id");
		let messageContent = $(this).siblings(".messageContent");

		messageContent.toggle();

		if (messageContent.is(":visible")) {
			localStorage.setItem("messageToggleState_" + messageId, "open");
		} else {
			localStorage.setItem("messageToggleState_" + messageId, "closed");
		}

		// 메시지가 '안 읽음' 상태면 읽음 처리
		if ($(this).data("read") !== 'Y') {
			$.ajax({
				url: "/message/read/" + messageId,
				type: "PUT",
				success: function() {
					location.reload();
					messageContent.toggle();
				}
			});
		}
	});

	// 메시지 삭제
	$(".deleteBtn").on("click", function() {
		let messageId = $(this).data("id");
		if (confirm("메시지를 삭제하시겠습니까?")) {
			$.ajax({
				url: "/message/delete/" + messageId,
				type: "PUT",
				success: function() {
					location.reload();
				}
			});
		}
	});
});

