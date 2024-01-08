// Check if JWT is not set or not equal to 1, redirect to index.html
if (!sessionStorage.getItem('jwt') || JSON.parse(sessionStorage.getItem('jwt')) !== 1) {
    window.location.replace('./index.html');
}

// Function to update the displayed username from session storage
function updateUsernameDisplay() {
    const userNameDisplay = document.querySelector('.user-info p');
    const storedUsername = sessionStorage.getItem('userName');
    if (storedUsername) {
        userNameDisplay.textContent = JSON.parse(storedUsername);
    } else {
        console.error('User name not found in session storage.');
    }
}

// Function to cancel the task
function cancelTask(taskId) {
    fetch(`http://localhost:8080/tasks/${taskId}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`, // Assuming you use Bearer token
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text(); // Use text() instead of json() because the response is a string
    })
    .then(message => {
        console.log('OK!', message);
        fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount'); // Re-fetch and display tasks
        Swal.fire({
            title: message, // Title from the server's response
            icon: 'success',
            timer: 2500, // Alert will close after 2.5 seconds
            showConfirmButton: false, // Hides the confirm button
            allowOutsideClick: false, // Prevents closing by clicking outside
            allowEscapeKey: false, // Prevents closing by pressing the escape key
            allowEnterKey: false // Prevents closing by pressing the enter key
        });
        
    })
    .catch(error => {
        console.error('Error cancelling task:', error);
        Swal.fire({ // Display an error message
            title: 'Error!',
            text: 'Could not cancel the task. Please try again.',
            icon: 'error',
            timer: 1500
        });
    });
}



// Function to handle card clicks
function cardClickHandler(event) {
    alert('This is a placeholder action for card click.');
}

// Function to render assigned tasks into the tasks-container
function renderAssignedTasks(assignedTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = ''; // Clear any existing tasks

    // Sort the tasks by assignmentDate, most recent first
    assignedTasks.sort((a, b) => new Date(b.assignmentDate) - new Date(a.assignmentDate));

    assignedTasks.forEach(task => {
        const taskCard = document.createElement('div');
        taskCard.classList.add('task-card');

        // Format the assignment date as 'dd/mm/yy hh:mm'
        const assignmentDate = new Date(task.assignmentDate);
        const formattedDate = `${assignmentDate.getDate().toString().padStart(2, '0')}/${(assignmentDate.getMonth() + 1).toString().padStart(2, '0')}/${assignmentDate.getFullYear().toString().substr(-2)} ${assignmentDate.getHours().toString().padStart(2, '0')}:${assignmentDate.getMinutes().toString().padStart(2, '0')}`;

        taskCard.innerHTML = `
            <div class="task-card-status">${task.userTaskStatus}</div>
            <div class="task-card-content">
                <h3 class="task-card-headline">${task.taskName}</h3>
                <h4 class="task-card-subhead">Assigned by: ${task.assignerName} ${task.assignerSurname}</h4>
                <p class="task-card-body">${formattedDate}</p>
            </div>
            <div class="task-card-footer">
                <button class="task-card-button secondary" data-task-id="${task.id}">Cancel</button>
                <button class="task-card-button primary" data-task-id="${task.id}">Initiate</button>
            </div>
        `;

        tasksContainer.appendChild(taskCard);
    });

    // Add event listener for cancel buttons
    tasksContainer.querySelectorAll('.secondary').forEach(button => {
        button.addEventListener('click', function(event) {
            const taskId = event.target.getAttribute('data-task-id');
            Swal.fire({
                title: 'You are about to cancel the task.',
                // text: 'You are about to cancel the task.',
                icon: 'warning',
                // background: 'rgba(0, 123, 255, 0.9)', // Semi-transparent dark blue
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, cancel it!',
                cancelButtonText: 'No, keep it'
            }).then((result) => {
                if (result.isConfirmed) {
                    if (result.isConfirmed) {
                        cancelTask(taskId);
                    }
                    console.log('Task cancelled:', taskId);
                    // Remove the task card or update the status as needed
                }
            });
        });
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
                const filteredAndSortedTasks = userData.assignedTasks.filter(task => task.userTaskStatus === status);
                renderAssignedTasks(filteredAndSortedTasks);
            }
        })
        .catch(error => {
            console.error(`Error fetching tasks for status ${status}:`, error);
        });
}

// Event listener for DOMContentLoaded to ensure the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
    AOS.init(); // Initialize animations

    updateUsernameDisplay(); // Update username display

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
            // background: 'rgba(110, 202, 255, 0.5)',
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
});
