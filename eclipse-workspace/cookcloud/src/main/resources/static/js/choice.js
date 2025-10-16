let choices = {}; // 사용자의 선택 저장
let currentIndex = 0; // 현재 카테고리 인덱스

const categories = [
	{ name: "국물", image: "https://i.namu.wiki/i/upNZ7cYsFsAfU0KcguO6OHMK68xC-Bj8EXxdCti61Jhjx10UCBgdK5bZCEx41-aAWcjWZ5JMKFUSaUGLC1tqWg.webp" },
	{ name: "간식", image: "https://www.visitbusan.net/uploadImgs/files/cntnts/20230202154258183" },
	{ name: "고기", image: "https://pimg.mk.co.kr/news/cms/202409/03/news-p.v1.20240830.b4869f379bd14577b89f27b9561557f7_P1.jpg" },
	{ name: "매운 음식", image: "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMDA2MTlfMjQx%2FMDAxNTkyNTQ3NTk5ODI5.e5TnwgZ-8-e5g-ZL2K34IsHDT3rmQOfj2U9o5ZjEmXMg.tU-bTdytXRK6pSXB5mJu0I5D4XuuSnbERsqpQ-J_CuYg.PNG.tdhlrdtiww%2F%25C0%25CC%25B9%25CC%25C1%25F6_116.png&type=sc960_832" },
	{ name: "채식", image: "https://static.wtable.co.kr/image-resize/production/service/recipe/144/4x3/215327b9-760d-4284-935a-b90a3d901870.jpg" },
	{ name: "해산물", image: "https://www.travelnbike.com/news/photo/201905/82349_154963_1318.jpg" }
];

// 카테고리 랜덤으로 섞기
const randomCategories = [...categories];
randomCategories.sort(() => Math.random() - 0.5); // 카테고리 배열을 랜덤하게 섞기

const categoryText = document.querySelector(".category");
const imageBox = document.getElementById("image-box");
const prevButton = document.getElementById("prev-button");
const confirmButton = document.getElementById("confirm-button");
const choiceData = document.getElementById("choiceData");

// 현재 카테고리를 화면에 표시하는 함수
function updateCategory() {
	if (currentIndex >= 0 && currentIndex < randomCategories.length) {
		categoryText.textContent = `${currentIndex + 1}. ${randomCategories[currentIndex].name}`;
		imageBox.src = randomCategories[currentIndex].image;
	}
}

function selectChoice(choice) {
	let category = randomCategories[currentIndex].name;
	choices[category] = choice === 'O' ? `${category} 포함` : `${category} 제외`;

	if (currentIndex < randomCategories.length - 1) {
		currentIndex++;
		updateCategory();
	} else {
		// 모든 선택을 마친 후 폼 데이터를 숨은 필드에 저장
		choiceData.value = JSON.stringify(choices);
		//console.log("사용자 선택 결과:", choiceData.value); // 콘솔 체크용

	}
}

// O 또는 X 버튼 클릭 시 선택 처리
document.querySelectorAll(".mark").forEach(button => {
	button.addEventListener("click", function() {
		selectChoice(this.textContent);
	});
});

prevButton.addEventListener("click", function() {
	if (currentIndex > 0) {
		currentIndex--;
		updateCategory();
	}
});

updateCategory();

function submitChoices() {

	if (Object.keys(choiceData.value).length === 0) {
		//console.error("Error: choiceData.value 객체가 비어 있습니다.");
		return;
	}
	axios.post('/choiceResult', JSON.parse(choiceData.value), {
		headers: {
			'Content-Type': 'application/json'
		}
	})
		.then(response => {
			//console.log("사용자 선택 결과:", choiceData.value);
			//console.log("Response Status:", response.status);
			//console.log("Received data:", response.data);

			document.open();
			document.write(response.data);
			document.close();
		})
		.catch(error => {
			//console.error('Error:', error);
		});
}



