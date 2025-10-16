document.getElementById('findIdForm').addEventListener('submit', function (event) {
    event.preventDefault();  // 폼 제출을 막음

    // 사용자가 입력한 이메일과 전화번호 가져오기
    const memEmail = document.getElementById('memEmail').value;
    const memPhone = document.getElementById('memPhone').value;

    // AJAX 요청으로 서버로 데이터 전송
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/login/findId', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    // 서버로 보내는 데이터
    let params = 'memEmail=' + encodeURIComponent(memEmail) + '&memPhone=' + encodeURIComponent(memPhone);
    
    // 서버 응답 처리
    xhr.onload = function () {
        if (xhr.status === 200) {
            let response = JSON.parse(xhr.responseText);  // 서버에서 보낸 JSON 응답
            if (response.memberId) {
                // 아이디가 있으면 alert로 아이디 표시
                alert("아이디: " + response.memberId);
            } else {
                // 아이디가 없으면 alert로 에러 메시지 표시
                alert("아이디가 없습니다.");
            }
        } else {
            alert("서버 오류가 발생했습니다.");
        }
    };
    
    // 데이터 전송
    xhr.send(params);
});
