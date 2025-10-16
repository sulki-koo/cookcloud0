document.addEventListener("DOMContentLoaded", function() {
	const saveButton = document.getElementById("saveMember");
	if (saveButton) {
		saveButton.addEventListener("click", function(event) {
			event.preventDefault();  // 기본 폼 제출을 방지

			let buttonVal = $('#saveMember').val();
			let valid = true;

			const memId = $("#memId").val();
			const id = $('#id').val();
			const password = $('#password').val();
			const checkPassword = $('#checkPassword').val();
			const name = $('#name').val();
			const nickname = $('#nickname').val();
			const originalNickname = $('#originalNickname').val();
			const email = $('#email').val();
			const phone = $('#phone').val();

			if (buttonVal != "수정") {
				// 아이디 유효성 검사
				const idPattern = /^[a-zA-Z0-9]+$/;
				if (id.length < 5 || id.length > 20 || !idPattern.test(id)) {
					valid = false;
					$('#idError').text('아이디는 영어와 숫자만 입력 가능하며, 5~20자여야 합니다.');

				} else {
					$('#idError').text('');
				}

				// 비밀번호 유효성 검사
				const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,20}$/;
				if (password.length < 8 || password.length > 20 || !passwordPattern.test(password)) {
					valid = false;
					$('#passwordError').text('비밀번호는 영어 대소문자, 숫자 포함해야 하며, 8~20자여야 합니다.');
				} else {
					$('#passwordError').text('');
				}

				// 비밀번호 확인 유효성 검사
				if (password !== checkPassword) {
					valid = false;
					$('#checkPasswordError').text('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
				} else {
					$('#checkPasswordError').text('');
				}
			}

			// 이름 유효성 검사
			const namePattern = /^[a-zA-Z가-힣]+$/;
			if (name.length < 2 || name.length > 20 || !namePattern.test(name)) {
				valid = false;
				$('#nameError').text('이름은 한글, 영어만 입력 가능하며, 2~20자여야 합니다.');
			} else {
				$('#nameError').text('');
			}

			// 닉네임 유효성 검사
			const nicknamePattern = /^[a-zA-Z0-9가-힣]+$/;
			if (nickname.length < 4 || nickname.length > 8 || !nicknamePattern.test(nickname)) {
				valid = false;
				$('#nicknameError').text('닉네임은 한글, 영어, 숫자만 입력 가능하며, 4~8자여야 합니다.');
			} else {
				$('#nicknameError').text('');
			}

			// 이메일 유효성 검사
			const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
			if (!emailPattern.test(email)) {
				valid = false;
				$('#emailError').text('유효한 이메일을 입력해주세요.');
			} else {
				$('#emailError').text('');
			}

			// 전화번호 유효성 검사
			const phonePattern = /^\d{10,11}$/; // 전화번호는 10~11자리 숫자만 허용
			if (!phonePattern.test(phone)) {
				valid = false;
				$('#phoneError').text('전화번호는 10~11자리 숫자만 입력 가능합니다.');
			} else {
				$('#phoneError').text('');
			}

			if (valid) {
				if (buttonVal != "수정") {
					Promise.all([
						checkDuplicateId(id),
						checkDuplicateNickname(nickname)
					]).then(([isIdDuplicate, isNicknameDuplicate]) => {
						if (isIdDuplicate) {
							alert('아이디가 이미 존재합니다.');
						} else if (isNicknameDuplicate) {
							alert('닉네임이 이미 존재합니다.');
						} else {
							insertMember(); // 중복 없으면 회원가입 진행
						}
					}).catch(error => {
						console.error("중복 확인 오류:", error);
						alert('중복 확인 중 오류가 발생했습니다.');
					});
				} else {
					if (nickname !== originalNickname) {
						checkDuplicateNickname(nickname).then(isDuplicate => {
							if (!isDuplicate) {
								updateMember(memId);
							} else {
								alert('닉네임이 이미 존재합니다.');
							}
						}).catch(error => {
							console.error("중복 확인 오류:", error);
							alert('중복 확인 중 오류가 발생했습니다.');
						});
					} else {
						updateMember(memId); // 닉네임 변경이 없으면 바로 수정
					}
				}
			}
		});
	}
});

function checkDuplicateId(id) {
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "/member/checkDuplicateId",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify({ memId: id }),
			success: function(response) {
				resolve(response.result); // true(중복), false(사용 가능)
			},
			error: function(xhr, status, error) {
				reject(error);
			}
		});
	});
}

function checkDuplicateNickname(nickname) {
	return new Promise((resolve, reject) => {
		$.ajax({
			url: "/member/checkDuplicateNickname",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify({ memNickname: nickname }),
			success: function(response) {
				resolve(response.result); // true(중복), false(사용 가능)
			},
			error: function(xhr, status, error) {
				reject(error);
			}
		});
	});
}

function insertMember() {
	let memberData = {
		memId: $("#id").val(),
		memPassword: $("#password").val(),
		memName: $("#name").val(),
		memNickname: $("#nickname").val(),
		memEmail: $("#email").val(),
		memPhone: $("#phone").val()
	}

	$.ajax({
		url: "/signup",
		type: "POST",
		contentType: "application/json",
		data: JSON.stringify(memberData),
		success: function(response) {
			alert("회원 가입 성공!");
			window.location.href = "/login";
		},
		error: function(xhr, staus, error) {
			alert("오류 발생 : " + xhr.responseText);
		}
	});
}

function updateMember(memId) {
	let nickname = $("#nickname").val();
	
	let memberData = {
		memName: $("#name").val(),
		memNickname: $("#nickname").val(),
		memEmail: $("#email").val(),
		memPhone: $("#phone").val()
	}

	$.ajax({
		url: "/mypage/updateMember/" + memId,
		type: "PUT",
		contentType: "application/json",
		data: JSON.stringify(memberData),
		success: function() {
			alert("회원정보 수정 성공!");
			window.location.href = `/@${nickname}#member-info`;
		},
		error: function(xhr, staus, error) {
			alert("오류 발생 : " + xhr.responseText);
		}
	});
}
