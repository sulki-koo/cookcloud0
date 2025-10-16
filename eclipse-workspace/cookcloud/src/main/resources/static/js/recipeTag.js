document.addEventListener("DOMContentLoaded", function() {
    const input = document.getElementById("hashtagInput");
    const container = document.getElementById("hashtagContainer");
    const hiddenInput = document.getElementById("hashtags");
    let hashtags = [];

    if (!input || !container || !hiddenInput) {
        console.error("Hashtag elements not found");
        return;
    }

    // 기존 해시태그 가져오기 (Thymeleaf에서 렌더링된 데이터 활용)
    document.querySelectorAll(".hashtag-btn").forEach(tag => {
        const tagText = tag.dataset.tag;
        if (tagText) {
            hashtags.push(tagText);
            tag.addEventListener("click", function() {
                removeHashtag(tagText);
            });
        }
    });

    input.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            const tagText = input.value.trim();
            if (tagText !== "" && !hashtags.includes(tagText)) {
                hashtags.push(tagText);
                updateHashtags();
            }
            input.value = "";
        }
    });

    function removeHashtag(tagText) {
        hashtags = hashtags.filter(t => t !== tagText);
        updateHashtags();
    }

    function updateHashtags() {
        container.innerHTML = "";
        hashtags.forEach(tag => {
            const tagElement = document.createElement("span");
            tagElement.classList.add("badge", "bg-primary", "me-1", "hashtag-btn");
            tagElement.textContent = "#" + tag + " ×";
            tagElement.style.cursor = "pointer";
            tagElement.dataset.tag = tag;
            tagElement.addEventListener("click", function() {
                removeHashtag(tag);
            });
            container.appendChild(tagElement);
        });
        hiddenInput.value = hashtags.join(",");
    }
});
