$(document).ready(function() {
	const recipeId = $("#recipeId").val();
	
	let offset = 0;
	const limit = 10;
	let loading = false;

	if (loading) return;
	loading = true;

	fetch(`/api/reviews/${recipeId}?offset=${offset}&limit=${limit}`)
		.then(response => response.json())
		.then(data => {
			if (data.length > 0) {
				offset += limit;
			}
			loading = false;
		})
		.catch(error => {
			loading = false;
		});

	/*
		$("#loading").show();
		$.get(`/reviews/${recipeId}?page=${page}`, function(data) {
			$("#reviews").append(data);
			$("#loading").hide();
			page++;
		});
	*/
	// 무한 스크롤 이벤트
	window.addEventListener("scroll", () => {
		if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 100) {
		}
	});

});
