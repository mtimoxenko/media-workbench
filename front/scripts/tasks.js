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
function cancelTask(userTaskId, taskId) {
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from session storage
    // console.log("taskId", taskId);
    // console.log("userTaskId", userTaskId);


    // Prompt user for a comment before canceling the task
    Swal.fire({
        title: 'Comment Required',
        input: 'textarea',
        inputPlaceholder: 'Your comment...',
        showCancelButton: true,
        confirmButtonText: 'Submit',
        cancelButtonText: 'Cancel',
        inputValidator: (value) => {
            if (!value.trim()) {
                return 'Comment cannot be empty.';
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            // User provided a comment and confirmed the cancellation
            // Start by creating the comment in the database
            return fetch(`http://localhost:8080/comments`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    text: result.value,
                    userId: userId,
                    taskId: taskId
                })
            });
        } else {
            throw new Error('Cancellation Aborted: No comment was provided.');
        }
    }).then(response => {
        if (!response.ok) {
            // If the server responds with an error status, throw an error
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text(); // Expect a text response from the server
    }).then(commentResponse => {
        console.log('Comment created:', commentResponse);
        // Comment created successfully, proceed to cancel the task
        return fetch(`http://localhost:8080/usertasks/${userTaskId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`
            }
        });
    }).then(response => {
        if (!response.ok) {
            // If the server responds with an error status, throw an error
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text(); // Expect a text response from the server
    }).then(cancelResponse => {
        console.log('Task cancelled:', cancelResponse);
        // Task cancelled successfully, now remove the task card from UI
        const taskCard = document.querySelector(`[data-user-task-id="${userTaskId}"]`);
        if (taskCard && taskCard.parentNode) {
            taskCard.parentNode.removeChild(taskCard);
        }
        // Update task counts
        fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
        // Display a success message to the user
        Swal.fire({
            title: 'Task Cancelled!',
            icon: 'success',
            timer: 2500,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
    }).catch(error => {
        console.error('Error:', error);
        Swal.fire({
            title: 'Cancellation aborted!',
            // text: 'There was a problem cancelling the task.',
            icon: 'info',
            timer: 2000,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
    });
}

// Function to initiate the task
function initiateTask(userTaskId, taskId) {
    fetch(`http://localhost:8080/usertasks`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`
        },
        body: JSON.stringify({
            id: userTaskId,
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
        // console.log(userTaskId);
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

// Function to complete a task with a comment
function completeTask(userTaskId, taskId) {
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from session storage

    // Prompt user for a comment before marking the task as completed
    Swal.fire({
        title: 'Completion Comment Required',
        input: 'textarea',
        inputPlaceholder: 'Enter a completion comment...',
        showCancelButton: true,
        confirmButtonText: 'Submit',
        cancelButtonText: 'Cancel',
        inputValidator: (value) => {
            if (!value.trim()) {
                return 'Comment cannot be empty.';
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            // User provided a comment and confirmed completion
            // Start by creating the comment in the database
            return fetch(`http://localhost:8080/comments`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    text: result.value,
                    userId: userId,
                    taskId: taskId
                })
            });
        } else {
            // If the user cancels or the input is empty, do not proceed with creating a comment
            return Promise.reject('Completion Aborted: No comment was provided.');
        }
    }).then(response => {
        if (!response.ok) {
            // If the server responds with an error status, throw an error
            return response.text().then(text => Promise.reject(text));
        }
        // Return text response since the server sends a string
        return response.text();
    }).then(commentMessage => {
        console.log('Comment created:', commentMessage);
        // Comment created successfully, now update the task status to 'COMPLETED'
        return fetch(`http://localhost:8080/usertasks`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`
            },
            body: JSON.stringify({
                id: userTaskId,
                userTaskStatus: 'COMPLETED'
            })
        });
    }).then(response => {
        if (!response.ok) {
            // If the server responds with an error status, throw an error
            return response.text().then(text => Promise.reject(text));
        }
        // Return text response since that is what your server sends
        return response.text();
    }).then(updateMessage => {
        console.log('Task status updated:', updateMessage);
        // Task status updated to 'COMPLETED', now remove the task card from UI
        const taskCard = document.querySelector(`[data-user-task-id="${userTaskId}"]`).parentNode.parentNode;
        if (taskCard) {
            taskCard.remove();
        }
        // Update task counts
            fetchAndDisplayTasksCount('IN_PROGRESS', '#tasksInProgressCount');
            fetchAndDisplayTasksCount('COMPLETED', '#completedTasksCount');
        // Display a success message to the user
        Swal.fire({
            title: 'Task Completed!',
            icon: 'success',
            timer: 1500,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
        fetchAndDisplayInProgressTasks();
    }).catch(error => {
        console.error('Error:', error);
        // Display an error message to the user
        Swal.fire({
            title: 'Completion aborted!',
            icon: 'info',
            timer: 2000,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false
        });
    });
}

// Function to add a comment to a task
function addCommentToTask(taskId) {
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from session storage

    // Prompt user for a comment before adding it to the task
    Swal.fire({
        title: 'Add New Comment',
        input: 'textarea',
        inputPlaceholder: 'Enter your comment...',
        showCancelButton: true,
        confirmButtonText: 'Add Comment',
        cancelButtonText: 'Cancel',
        inputValidator: (value) => {
            if (!value.trim()) {
                return 'Comment cannot be empty.';
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            // User provided a comment and confirmed the action
            // Proceed with creating the comment in the database
            return fetch(`http://localhost:8080/comments`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    text: result.value,
                    userId: userId,
                    taskId: taskId
                })
            });
        } else {
            // If the user cancels or the input is empty, simply close the alert
            return Promise.resolve('No action taken.'); // Resolve the promise with a message
        }
    }).then(response => {
        if (response.ok) {
            // If the server responds with a success status, parse the response
            return response.text();
        } else if (response) {
            // If response is a simple message, just log it (e.g., 'No action taken.')
            console.log(response);
        } else {
            // If there's an actual error response from the server, handle it
            return response.text().then(text => Promise.reject(text));
        }
    }).then(message => {
        if (message) {
            // Handle your successful comment addition or other messages here
            console.log(message);
        }
    }).catch(error => {
        // If there was an error other than the user cancelling, you can handle it here
        // If you want to do nothing, you can leave this catch block empty
    });
}

// Function to fetch task information and display it
function fetchTaskInfo(taskId) {
    fetch(`http://localhost:8080/tasks/${taskId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(taskInfo => {
            // Use Swal.fire to display the task description
            Swal.fire({
                title: taskInfo.name,
                html: `<p style="color:#fff;">${taskInfo.description}</p>`, // Using html to inject inline styles
                icon: 'info',
                background: 'rgba(36, 59, 85, 0.9)', // Use a dark background color
                confirmButtonColor: '#3085d6', // Use a blue color for the confirm button
                confirmButtonText: 'Close'
            });
        })
        .catch(error => {
            console.error('Error fetching task info:', error);
            // Optionally, handle errors or display a message if the fetch fails
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
            <div class="task-card-header">
                <div class="task-card-status">${task.userTaskStatus}</div>
                <div class="task-card-info">
                    <button class="icon-button info-button" data-task-id="${task.taskId}" onclick="fetchTaskInfo(${task.taskId})">
                        <i class="fa-solid fa-circle-info"></i>
                    </button>
                </div>
            </div>
            <div class="task-card-content">
                <h3 class="task-card-headline">${task.taskName}</h3>
                    <h4 class="task-card-subhead">Assigned by: ${task.assignerName} ${task.assignerSurname}</h4>
                <p class="task-card-body">${formattedDate}</p>
            </div>
            <div class="task-card-footer">
                <button class="task-card-button secondary" data-user-task-id="${task.id}" data-task-id="${task.taskId}">Cancel</button>
                <button class="task-card-button primary" data-user-task-id="${task.id}" data-task-id="${task.taskId}">Initiate</button>
            </div>
        `;

        tasksContainer.appendChild(taskCard);

        // Event listener for footer buttons
        const taskCardFooter = taskCard.querySelector('.task-card-footer');
        taskCardFooter.addEventListener('click', function(event) {
            const clickedButton = event.target;
            const userTaskId = clickedButton.getAttribute('data-user-task-id');
            const taskId = clickedButton.getAttribute('data-task-id');

            if (clickedButton.classList.contains('cancel-no') || clickedButton.classList.contains('initiate-no')) {
                // Reset the card if 'No' is clicked
                renderAssignedTasks(assignedTasks);
            } else if (clickedButton.classList.contains('cancel-yes')) {
                // Cancel the task if 'Yes' is clicked
                // console.log("taskId", taskId);
                // console.log("userTaskId", userTaskId);
                cancelTask(userTaskId, taskId);
            } else if (clickedButton.classList.contains('initiate-yes')) {
                // Initiate the task if 'Yes' is clicked
                initiateTask(userTaskId, taskId);
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
    tasksContainer.innerHTML = ''; // Clear the tasks container before rendering

    if (inProgressTasks.length === 0) {
        // If there are no tasks in progress, display the no tasks message
        displayNoInProgressTasksMessage();
    } else {
        // Otherwise, render the in-progress tasks
        inProgressTasks.sort((a, b) => new Date(b.assignmentDate) - new Date(a.assignmentDate));

        inProgressTasks.forEach(task => {
            const taskCard = document.createElement('div');
            taskCard.classList.add('task-card');

            const assignmentDate = new Date(task.assignmentDate);
            const formattedDate = `${assignmentDate.getDate().toString().padStart(2, '0')}/${(assignmentDate.getMonth() + 1).toString().padStart(2, '0')}/${assignmentDate.getFullYear().toString().substr(-2)} ${assignmentDate.getHours().toString().padStart(2, '0')}:${assignmentDate.getMinutes().toString().padStart(2, '0')}`;

            taskCard.innerHTML = `
                <div class="task-card-header">
                    <div class="task-card-status">${task.userTaskStatus}</div>
                    <div class="task-card-info">
                        <button class="icon-button comment-button" data-task-id="${task.taskId}" onclick="addCommentToTask(this.getAttribute('data-task-id'))">
                            <i class="fa-solid fa-pencil"></i>
                        </button>
                        <button class="icon-button info-button" data-task-id="${task.taskId}" onclick="fetchTaskInfo(${task.taskId})">
                            <i class="fa-solid fa-circle-info"></i>
                        </button>
                    </div>
                </div>
                <div class="task-card-content">
                    <h3 class="task-card-headline">${task.taskName}</h3>
                    <h4 class="task-card-subhead">Assigned by: ${task.assignerName} ${task.assignerSurname}</h4>
                    <p class="task-card-body">${formattedDate}</p>
                </div>
                <div class="task-card-footer-progress">
                    <button class="task-card-button primary" data-user-task-id="${task.id}" data-task-id="${task.taskId}">
                        Complete Task
                    </button>
                </div>
            `;
            
        
        

            tasksContainer.appendChild(taskCard);

            // Event listener for the 'Complete Task' button
            taskCard.querySelector('.primary').addEventListener('click', function(event) {
                completeTask(task.id, task.taskId);
            });
        });
    }
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
            <div class="task-card-header">
                <div class="task-card-status">${task.userTaskStatus}</div>
                <div class="task-card-info">
                    <button class="icon-button info-button" data-task-id="${task.taskId}" onclick="fetchTaskInfo(${task.taskId})">
                        <i class="fa-solid fa-circle-info"></i>
                    </button>
                </div>
            </div>
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
