document.getElementById("fileInput").addEventListener("change", function() {
	const file = this.files[0];
	const fileError = document.getElementById("fileError");

	if (!file) {
		fileError.style.display = "none";
		return;
	}

	const allowedExtensions = ["jpg", "jpeg", "png", "gif", "mp4", "avi", "mov"];
	const fileExtension = file.name.split(".").pop().toLowerCase();

	if (!allowedExtensions.includes(fileExtension)) {
		fileError.style.display = "block";
	} else {
		fileError.style.display = "none";
	}
});