document.addEventListener("DOMContentLoaded", function() {
	// ----- 슬라이드 기능 -----
	const sliderInner = document.getElementById("sliderInner");
	const slideWidth = 800;
	let isAnimating = false;

	function moveNext() {
		if (isAnimating) return;
		isAnimating = true;

		sliderInner.style.transition = "transform 0.5s ease-in-out";
		sliderInner.style.transform = `translateX(-${slideWidth}px)`;

		sliderInner.addEventListener("transitionend", function handler() {
			// 첫 번째 슬라이드를 맨 뒤로 보내고 위치 초기화
			sliderInner.appendChild(sliderInner.firstElementChild);
			sliderInner.style.transition = "none";
			sliderInner.style.transform = "translateX(0)";
			isAnimating = false;

			sliderInner.removeEventListener("transitionend", handler);
		});
	}

	function movePrev() {
		if (isAnimating) return;
		isAnimating = true;

		// 마지막 슬라이드를 맨 앞으로 이동
		sliderInner.insertBefore(sliderInner.lastElementChild, sliderInner.firstElementChild);
		sliderInner.style.transition = "none";
		sliderInner.style.transform = `translateX(-${slideWidth}px)`;

		requestAnimationFrame(() => {
			sliderInner.style.transition = "transform 0.5s ease-in-out";
			sliderInner.style.transform = "translateX(0)";
		});

		sliderInner.addEventListener("transitionend", function handler() {
			isAnimating = false;
			sliderInner.removeEventListener("transitionend", handler);
		});
	}

	document.getElementById("nextBtn").addEventListener("click", moveNext);
	document.getElementById("prevBtn").addEventListener("click", movePrev);

	// 자동 이동
	 // ----- 공지사항 페이지네이션 -----
	const items = document.querySelectorAll(".notice-item");
	const itemsPerPage = 4;
	const pagesPerGroup = 3;
	const totalPages = Math.ceil(items.length / itemsPerPage);
	const paginationWrapper = document.getElementById("paginationWrapper");

	let currentPage = 1;

	function renderPage(page) {
	  currentPage = page;

	  // 항목 보이기 처리
	  items.forEach((item, i) => {
	    item.style.display = (i >= (page - 1) * itemsPerPage && i < page * itemsPerPage) ? "block" : "none";
	  });

	  renderPagination();
	}

	function renderPagination() {
	  paginationWrapper.innerHTML = "";

	  const currentGroup = Math.ceil(currentPage / pagesPerGroup);
	  const startPage = (currentGroup - 1) * pagesPerGroup + 1;
	  const endPage = Math.min(startPage + pagesPerGroup - 1, totalPages);

	 // << 버튼 (이전 그룹으로 이동)
	 const prevGroupBtn = document.createElement("button");
	 prevGroupBtn.innerText = "<<";
	 prevGroupBtn.disabled = currentGroup === 1;
	 prevGroupBtn.addEventListener("click", () => {
	   const prevGroupLastPage = startPage - 1;
	   renderPage(prevGroupLastPage);
	 });
	 paginationWrapper.appendChild(prevGroupBtn);


	 // < 버튼 (이전 페이지)
	 const prevPageBtn = document.createElement("button");
	 prevPageBtn.innerText = "<";
	 prevPageBtn.disabled = currentPage === 1;
	 prevPageBtn.addEventListener("click", () => renderPage(currentPage - 1));
	 paginationWrapper.appendChild(prevPageBtn);

	 // 페이지 번호 버튼
	 for (let i = startPage; i <= endPage; i++) {
	   const btn = document.createElement("button");
	   btn.innerText = i;
	   btn.className = (i === currentPage) ? "active" : "";
	   btn.addEventListener("click", () => renderPage(i));
	   paginationWrapper.appendChild(btn);
	 }

	 // > 버튼 (다음 페이지)
	 const nextPageBtn = document.createElement("button");
	 nextPageBtn.innerText = ">";
	 nextPageBtn.disabled = currentPage === totalPages;
	 nextPageBtn.addEventListener("click", () => renderPage(currentPage + 1));
	 paginationWrapper.appendChild(nextPageBtn);
	 

	 // >> 버튼 (다음 그룹으로 이동)
	 const lastGroupBtn = document.createElement("button");
	 lastGroupBtn.innerText = ">>";
	 lastGroupBtn.disabled = endPage === totalPages;
	 lastGroupBtn.addEventListener("click", () => {
	   const nextGroupFirstPage = endPage + 1;
	   renderPage(nextGroupFirstPage);
	 });
	 paginationWrapper.appendChild(lastGroupBtn);
	}
	renderPage(1);


	// ----- 공지사항 내용 토글 -----
	window.toggleContent = function(element) {
		const content = element.nextElementSibling;
		content.style.display = content.style.display === "block" ? "none" : "block";
	};
});