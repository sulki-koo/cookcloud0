document.addEventListener("DOMContentLoaded", function() {
	document.getElementById("deleteMember")?.addEventListener("click", function(event) {
		event.preventDefault();
		document.getElementById("deleteModal").style.display = "block";
	});

	document.getElementById("cancelDeleteBtn")?.addEventListener("click", function() {
		document.getElementById("deleteModal").style.display = "none";
	});

	document.getElementById("confirmDeleteBtn")?.addEventListener("click", function() {
		const isChecked = document.getElementById("confirmDelete").checked;

		if (!isChecked) {
			alert("탈퇴 동의 체크박스를 선택해야 합니다.");
			return;
		}
		deleteMember();
	});

	function deleteMember() {
		const memId = $("#memId").val();
		$.ajax({
			url: "/mypage/deleteMember/" + memId,
			type: "PUT",
			success: function() {
				alert("회원 탈퇴가 완료되었습니다.");
				window.location.href = "/logout";
			},
			error: function(xhr, status, error) {
				alert("탈퇴 처리 중 오류 발생: " + xhr.responseText);
			}
		});
	}
	
});