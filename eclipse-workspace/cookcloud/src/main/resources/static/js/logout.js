document.getElementById("logoutLink")?.addEventListener("click", function(event) {
	event.preventDefault();
	logout();  // 로그아웃 함수 호출
});

function logout() {
	sessionStorage.clear();  // 로그아웃 시 모든 세션 스토리지 초기화
	window.location.href = '/logout';
}
