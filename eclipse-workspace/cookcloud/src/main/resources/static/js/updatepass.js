document.getElementById("updatePasswordBtn").addEventListener("click", function(event) {
	event.preventDefault();  // 기본 폼 제출 방지

	let valid = true;

	const memId = $("#memId").val();
	const password = $("#password").val();
	const checkPassword = $("#checkPassword").val();

	// 비밀번호 유효성 검사
	const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,20}$/;
	if (!passwordPattern.test(password)) {
		valid = false;
		$('#passwordError').text('비밀번호는 영어 대소문자, 숫자 포함 8~20자여야 합니다.');
	} else {
		$('#passwordError').text('');
	}

	// 비밀번호 확인 검사
	if (password !== checkPassword) {
		valid = false;
		$('#checkPasswordError').text('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
	} else {
		$('#checkPasswordError').text('');
	}

	if (valid) {
		updatePassword(memId, password);
	}
});

// ✅ URL이 아닌 JSON 데이터로 비밀번호 전달
function updatePassword(memId, password) {
	$.ajax({
		url: "/login/updatePass",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify({ memId: memId, memPassword: password }),
		success: function() {
			alert("비밀번호 변경 성공! 로그인 페이지로 이동합니다.");
			window.location.href = "/login";
		},
		error: function(xhr, status, error) {
			alert("비밀번호 변경 실패: " + xhr.responseText);
		}
	});
}
