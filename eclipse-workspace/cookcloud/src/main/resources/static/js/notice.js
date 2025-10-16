// 수정 버튼 클릭 시 입력 필드 활성화 (수정 기능은 그대로)
function editNotice(id) {
	document.getElementById('title-' + id).style.display = 'none';
	document.getElementById('content-' + id).style.display = 'none';

	document.getElementById('edit-title-' + id).style.display = 'inline';
	document.getElementById('edit-content-' + id).style.display = 'inline';

	document.getElementById('edit-btn-' + id).style.display = 'none';
	document.getElementById('save-btn-' + id).style.display = 'inline';
}

// 수정한 데이터 저장 요청 (AJAX)
function saveNotice(id) {
	let title = document.getElementById('edit-title-' + id).value;
	let content = document.getElementById('edit-content-' + id).value;

	fetch('/admin/updateNotice', {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({
			noticeId: id,
			noticeTitle: title,
			noticeContent: content
		})
	})
		.then(response => response.json())
		.then(data => {
			if (data.success) {
				document.getElementById('title-' + id).innerText = title;
				document.getElementById('content-' + id).innerText = content;
				document.getElementById('title-' + id).style.display = 'inline';
				document.getElementById('content-' + id).style.display = 'inline';
				document.getElementById('edit-title-' + id).style.display = 'none';
				document.getElementById('edit-content-' + id).style.display = 'none';
				document.getElementById('edit-btn-' + id).style.display = 'inline';
				document.getElementById('save-btn-' + id).style.display = 'none';
			} else {
				alert('공지 수정 실패');
			}
		});
}

// 삭제 버튼 클릭 시 (소프트 딜리트: 'N' → 'Y' 업데이트)
function deleteNotice(noticeId) {
	if (confirm("정말로 이 공지를 삭제하시겠습니까?")) {
		fetch('/admin/deleteNotice/' + noticeId, {
			method: 'POST',  // POST 메서드 사용 (soft delete 처리)
			headers: { 'Content-Type': 'application/json' }
		})
			.then(response => {
				if (response.ok) {
					return response.json();
				} else {
					throw new Error('삭제에 실패했습니다.');
				}
			})
			.then(data => {
				if (data.success) {
					let cell = document.querySelector("#row-" + noticeId + " td:last-child");
					if (cell) {
						cell.innerHTML = '<span style="color:red; font-weight:bold;">삭제됨</span>';
					}
					alert("공지사항이 삭제 처리되었습니다.");
				} else {
					alert("삭제에 실패했습니다.");
				}
			})
			.catch(error => {
				console.error('Error:', error);
				alert("삭제 중 오류가 발생했습니다.");
			});
	}
}
