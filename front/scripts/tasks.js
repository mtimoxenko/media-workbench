// Check if JWT is not set or not equal to 1, redirect to index.html
if (!sessionStorage.getItem('jwt') || JSON.parse(sessionStorage.getItem('jwt')) !== 1) {
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
function cardClickHandler(event) {
  alert('This is a placeholder action for card click.');
}

// Function to render assigned tasks into the tasks-container
function renderAssignedTasks(assignedTasks) {
  const tasksContainer = document.querySelector('.tasks-container');
  tasksContainer.innerHTML = ''; // Clear any existing tasks

  assignedTasks.forEach(task => {
      const taskCard = document.createElement('div');
      taskCard.classList.add('task-card');

      const taskProgress = (task.progress || 0) + '%'; // Replace with actual progress

      taskCard.innerHTML = `
          <div class="task-card-status">ASSIGNED</div>
          <h3 class="task-card-title">${task.taskName}</h3>
          <p class="task-card-description">${task.description}</p>
          <div class="task-card-progress">
              <div class="task-card-progress-bar" style="width: ${taskProgress};"></div>
          </div>
          <div class="task-card-subtasks">SUB-TASKS: 4</div> <!-- Replace with actual subtasks count -->
          <div class="task-card-actions">
              <button class="task-card-button cancel">✕</button>
              <button class="task-card-button edit">✎</button>
              <button class="task-card-button done">✔</button>
          </div>
      `;

      tasksContainer.appendChild(taskCard);
  });
}



// Function to fetch tasks and update counts for a given task status
function fetchAndDisplayTasksCount(status, countElementId) {
  const userId = JSON.parse(sessionStorage.getItem('userId'));

  fetch(`http://localhost:8080/users/${userId}`)
      .then(response => {
          if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json();
      })
      .then(userData => {
          const tasksCount = userData.assignedTasks.filter(task => task.userTaskStatus === status).length;
          document.querySelector(countElementId).textContent = tasksCount;

          // Render assigned tasks if status is 'ASSIGNED'
          if (status === 'ASSIGNED') {
              renderAssignedTasks(userData.assignedTasks.filter(task => task.userTaskStatus === 'ASSIGNED'));
          }
      })
      .catch(error => {
          console.error(`Error fetching tasks for status ${status}:`, error);
      });
}

// Event listener for DOMContentLoaded to ensure the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
  // Initialize animations
  AOS.init();

  // Update username display
  updateUsernameDisplay();

  // Fetch and display the count of tasks based on their status and render assigned tasks
  fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
  fetchAndDisplayTasksCount('IN_PROGRESS', '#tasksInProgressCount');
  fetchAndDisplayTasksCount('COMPLETED', '#completedTasksCount');

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

  // Attach cardClickHandler to each card
  const cards = document.querySelectorAll('.task-list.card');
  cards.forEach(card => {
      card.addEventListener('click', cardClickHandler);
  });

  // Additional event listeners and functions can be added here
  // ...
});
