// Check if JWT is not set or not equal to 1, redirect to index.html
if (!sessionStorage.getItem('jwt') || sessionStorage.getItem('jwt') !== '1') {
  window.location.replace('./index.html');
}

// Function to update the displayed username from session storage
function updateUsernameDisplay() {
  const userNameDisplay = document.querySelector('.user-info p');
  const storedUsername = sessionStorage.getItem('userName');
  if (storedUsername) {
    // Parse the JSON string to remove quotation marks
    userNameDisplay.textContent = JSON.parse(storedUsername);
  } else {
    console.error('User name not found in session storage.');
  }
}

// Function to handle card clicks
function cardClickHandler() {
  alert('This is a placeholder action for card click.');
}

// Event listener for DOMContentLoaded to ensure the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
  // Initialize animations
  AOS.init();

  // Update username display
  updateUsernameDisplay();

  // Logout button event listener
  const btnCloseApp = document.getElementById('closeApp');
  btnCloseApp.addEventListener('click', function () {
    Swal.fire({
      title: 'Are you leaving?',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      background: 'rgba(110, 202, 255, 0.35)',
      customClass: {
        popup: 'swal2-popup',
        title: 'swal2-title',
        confirmButton: 'swal2-confirm',
        cancelButton: 'swal2-cancel',
      }
    }).then((result) => {
      if (result.isConfirmed) {
        sessionStorage.clear();
        window.location.replace('./index.html');
      }
    });
  });

  // Add your additional event listeners and functions here
  // ...
});
