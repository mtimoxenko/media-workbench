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
    fetch(`http://localhost:8080/usertasks/${taskId}`, {
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
            title: 'Task Canceled!', // Title from the server's response
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
            timer: 1500,
            showConfirmButton: false, // Hides the confirm button
            allowOutsideClick: false, // Prevents closing by clicking outside
            allowEscapeKey: false, // Prevents closing by pressing the escape key
            allowEnterKey: false // Prevents closing by pressing the enter key
        });
    });
}
// Function to initiate the task
function initiateTask(taskId) {
    fetch(`http://localhost:8080/usertasks`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`
        },
        body: JSON.stringify({
            id: taskId,
            userTaskStatus: 'IN_PROGRESS'
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text(); // Handle text response
    })
    .then(message => {
        console.log('Task initiated:', message);

        // Remove the task card from UI
        const taskCard = document.querySelector(`[data-task-id="${taskId}"]`).parentNode.parentNode;
        taskCard.remove();

        // Update task counts
        fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
        fetchAndDisplayTasksCount('IN_PROGRESS', '#tasksInProgressCount');

        // Display a success message to the user
        Swal.fire({
            title: 'Task Initiated!',
            // text: 'The task has been set to in progress.',
            icon: 'info',
            timer: 1500,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
    })
    .catch(error => {
        console.error('Error initiating task:', error);
        // Display an error message to the user
        Swal.fire({
            title: 'Error!',
            text: 'Could not initiate the task. Please try again.',
            icon: 'error',
            timer: 1500,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
    });
}
// Function to complete a task
function completeTask(taskId) {
    fetch(`http://localhost:8080/usertasks`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`
        },
        body: JSON.stringify({
            id: taskId,
            userTaskStatus: 'COMPLETED'
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    })
    .then(message => {
        console.log('Task completed:', message);

        // Remove the task card from UI
        const taskCard = document.querySelector(`[data-task-id="${taskId}"]`).parentNode.parentNode;
        taskCard.remove();

        // Update task counts
        fetchAndDisplayTasksCount('IN_PROGRESS', '#tasksInProgressCount');
        fetchAndDisplayTasksCount('COMPLETED', '#completedTasksCount');

        // Optionally, display a success message to the user
        Swal.fire({
            title: 'Task Completed!',
            // text: message,
            icon: 'success',
            timer: 1500,
            showConfirmButton: false, // Hides the confirm button
            allowOutsideClick: false, // Prevents closing by clicking outside
            allowEscapeKey: false, // Prevents closing by pressing the escape key
            allowEnterKey: false // Prevents closing by pressing the enter key
        });
    })
    .catch(error => {
        console.error('Error completing task:', error);
        // Optionally, display an error message to the user
        Swal.fire({
            title: 'Error!',
            text: 'Could not complete the task. Please try again.',
            icon: 'error',
            timer: 1500,
            showConfirmButton: false, // Hides the confirm button
            allowOutsideClick: false, // Prevents closing by clicking outside
            allowEscapeKey: false, // Prevents closing by pressing the escape key
            allowEnterKey: false // Prevents closing by pressing the enter key
        });
    });
}





// Function to display a message when there are no "New Assigned" tasks
function displayNoTasksMessage() {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = `
        <div class="no-tasks-message">
            <h2>No New Assigned Tasks</h2>
            <p>It looks like you're all caught up! 👍</p>
            <p>Check "Tasks in Progress" for ongoing work, or see "Available Work" to pick up new tasks.</p>
        </div>
    `;
    // Add styling as needed, e.g., centering the text, adding padding, etc.
}
// Function to display a message when there are no tasks in progress
function displayNoInProgressTasksMessage() {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = `
        <div class="no-tasks-message">
            <h2>No Tasks In Progress</h2>
            <p>You don't have any tasks currently in progress.</p>
            <p>Check "New Assigned Tasks" for new assignments, or "Completed Tasks" to review your completed work.</p>
        </div>
    `;
    // Add additional styling as needed
}
// Function to display a message when there are no completed tasks
function displayNoCompletedTasksMessage() {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = `
        <div class="no-tasks-message">
            <h2>No Completed Tasks</h2>
            <p>You don't have any completed tasks at the moment.</p>
            <p>Check "New Assigned Tasks" for new assignments, or "Tasks In Progress" to continue working on ongoing tasks.</p>
        </div>
    `;
    // Add additional styling as needed
}






// Function to handle card clicks
function cardClickHandler(event) {
    const cardId = event.currentTarget.id;

    if (cardId === 'newAssignedTasks') {
        fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
    } else if (cardId === 'tasksInProgress') {
        fetchAndDisplayInProgressTasks();
    } else if (cardId === 'completedTasks') {
        fetchAndDisplayCompletedTasks();
    }
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

            // If there are no "New Assigned" tasks, display a message
            if (status === 'ASSIGNED' && tasksCount === 0) {
                displayNoTasksMessage();
            } else if (status === 'ASSIGNED') {
                const filteredAndSortedTasks = userData.assignedTasks.filter(task => task.userTaskStatus === status);
                renderAssignedTasks(filteredAndSortedTasks);
            }
        })
        .catch(error => {
            console.error(`Error fetching tasks for status ${status}:`, error);
        });
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

        // Event listener for footer buttons
        const taskCardFooter = taskCard.querySelector('.task-card-footer');
        taskCardFooter.addEventListener('click', function(event) {
            const clickedButton = event.target;
            const taskId = clickedButton.getAttribute('data-task-id');

            if (clickedButton.classList.contains('cancel-no') || clickedButton.classList.contains('initiate-no')) {
                // Reset the card if 'No' is clicked
                renderAssignedTasks(assignedTasks);
            } else if (clickedButton.classList.contains('cancel-yes')) {
                // Cancel the task if 'Yes' is clicked
                cancelTask(taskId);
            } else if (clickedButton.classList.contains('initiate-yes')) {
                // Initiate the task if 'Yes' is clicked
                initiateTask(taskId);
            } else if (clickedButton.classList.contains('secondary')) {
                // Change to cancel confirmation state
                updateTaskCardForConfirmation(taskCard, 'Cancel');
            } else if (clickedButton.classList.contains('primary')) {
                // Change to initiate confirmation state
                updateTaskCardForConfirmation(taskCard, 'Initiate');
            }
        });
    });
}

function updateTaskCardForConfirmation(taskCard, action) {
    const headline = taskCard.querySelector('.task-card-headline');
    const subhead = taskCard.querySelector('.task-card-subhead');
    const body = taskCard.querySelector('.task-card-body');
    const cancelButton = taskCard.querySelector('.secondary');
    const initiateButton = taskCard.querySelector('.primary');

    if (action === 'Cancel') {
        headline.textContent = 'Cancel Task?';
        subhead.textContent = 'This action cannot be undone.';
        body.textContent = 'Do you want to proceed?';
        cancelButton.textContent = 'No';
        initiateButton.textContent = 'Yes';
        cancelButton.classList.replace('secondary', 'cancel-no');
        initiateButton.classList.replace('primary', 'cancel-yes');
    } else if (action === 'Initiate') {
        headline.textContent = 'Initiate Task?';
        subhead.textContent = 'Ready to start this task?';
        body.textContent = 'Confirm to proceed.';
        cancelButton.textContent = 'No';
        initiateButton.textContent = 'Yes';
        cancelButton.classList.replace('secondary', 'initiate-no');
        initiateButton.classList.replace('primary', 'initiate-yes');
    }
}








// Function to fetch and display in-progress tasks
function fetchAndDisplayInProgressTasks() {
    const userId = JSON.parse(sessionStorage.getItem('userId'));

    fetch(`http://localhost:8080/users/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(userData => {
            const inProgressTasks = userData.assignedTasks.filter(task => task.userTaskStatus === 'IN_PROGRESS');

            if (inProgressTasks.length === 0) {
                displayNoInProgressTasksMessage();
            } else {
                renderInProgressTasks(inProgressTasks);
            }
        })
        .catch(error => {
            console.error(`Error fetching in-progress tasks:`, error);
        });
}
// Function to render in-progress tasks
function renderInProgressTasks(inProgressTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = '';

    inProgressTasks.sort((a, b) => new Date(b.assignmentDate) - new Date(a.assignmentDate));

    inProgressTasks.forEach(task => {
        const taskCard = document.createElement('div');
        taskCard.classList.add('task-card');

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
                <button class="task-card-button primary" data-task-id="${task.id}">Complete Task</button>
            </div>
        `;

        tasksContainer.appendChild(taskCard);

        // Event listener for the 'Complete Task' button
        taskCard.querySelector('.primary').addEventListener('click', function(event) {
            // Logic to mark the task as complete
            // This could involve sending a request to your backend to update the task status
            completeTask(task.id);
        });
    });
}






// Function to fetch and display completed tasks
function fetchAndDisplayCompletedTasks() {
    const userId = JSON.parse(sessionStorage.getItem('userId'));

    fetch(`http://localhost:8080/users/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(userData => {
            const completedTasks = userData.assignedTasks.filter(task => task.userTaskStatus === 'COMPLETED');

            if (completedTasks.length === 0) {
                displayNoCompletedTasksMessage();
            } else {
                renderCompletedTasks(completedTasks);
            }
        })
        .catch(error => {
            console.error(`Error fetching completed tasks:`, error);
        });
}
// Function to render completed tasks
function renderCompletedTasks(completedTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = '';

    completedTasks.sort((a, b) => new Date(b.assignmentDate) - new Date(a.assignmentDate));

    completedTasks.forEach(task => {
        const taskCard = document.createElement('div');
        taskCard.classList.add('task-card');

        const assignmentDate = new Date(task.assignmentDate);
        const formattedDate = `${assignmentDate.getDate().toString().padStart(2, '0')}/${(assignmentDate.getMonth() + 1).toString().padStart(2, '0')}/${assignmentDate.getFullYear().toString().substr(-2)} ${assignmentDate.getHours().toString().padStart(2, '0')}:${assignmentDate.getMinutes().toString().padStart(2, '0')}`;

        taskCard.innerHTML = `
            <div class="task-card-status">${task.userTaskStatus}</div>
            <div class="task-card-content">
                <h3 class="task-card-headline">${task.taskName}</h3>
                <h4 class="task-card-subhead">Assigned by: ${task.assignerName} ${task.assignerSurname}</h4>
                <p class="task-card-body">${formattedDate}</p>
            </div>
        `;

        tasksContainer.appendChild(taskCard);
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
