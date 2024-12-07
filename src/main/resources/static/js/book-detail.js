const stars = document.querySelectorAll(".star");
const ratingValue = document.getElementById("rating-value");

let currentRating = 0;

stars.forEach((star) => {
    star.addEventListener("mouseover", () => {
        resetStars();
        const rating = parseInt(star.getAttribute("data-rating"));
        highlightStars(rating);
    });

    star.addEventListener("mouseout", () => {
        resetStars();
        highlightStars(currentRating);
    });

    star.addEventListener("click", () => {
        currentRating = parseInt(star.getAttribute("data-rating"));
        ratingValue.textContent = `Bạn đã đánh giá ${currentRating} sao.`;
        highlightStars(currentRating);
    });
});

function resetStars() {
    stars.forEach((star) => {
        star.classList.remove("active");
    });
}

function highlightStars(rating) {
    stars.forEach((star) => {
        const starRating = parseInt(star.getAttribute("data-rating"));
        if (starRating <= rating) {
            star.classList.add("active");
        }
    });
}


// Function to send a PUT request to add the book to favorites
function addToFavorites() {
    fetch('/api/users/add-favourite', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ bookId: bookId }) // Send bookId in the request body
    })
        .then(response => {
            if (response.ok) {
                alert("Book added to favorites successfully.");
                location.reload();
            } else if (response.status === 409) {
                alert("Book is already in favorites.");
            } else {
                alert("Failed to add book to favorites.");
            }
        })
        .catch(error => console.error("Error adding to favorites:", error));
}

// Define in the global scope
function removeFromFavorites() {
    fetch('/api/users/remove-favourite', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ bookId: bookId })
    })
        .then(response => {
            if (response.ok) {
                alert("Book removed from favourites successfully.");
                location.reload();
            } else if (response.status === 404) {
                alert("Book is not in favourites.");
            } else {
                alert("Failed to remove book from favourites.");
            }
        })
        .catch(error => console.error("Error removing from favourites:", error));
}