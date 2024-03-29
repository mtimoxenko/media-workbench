// Redirects to index.html if JWT is not set or not equal to 1
if (!sessionStorage.getItem('jwt') || JSON.parse(sessionStorage.getItem('jwt')) !== 1) {
    window.location.replace('./index.html');
}


// const endpoint = '/api';
const endpoint = 'http://localhost:8080';



// Updates the displayed username and user image from session storage
function updateUserDisplay() {
    const userNameDisplay = document.getElementById('username');
    const userIcon = document.getElementById('user-icon');
    
    const storedUsername = sessionStorage.getItem('userName');
    const storedShift = sessionStorage.getItem('shift');
    
    if (storedUsername) {
      userNameDisplay.textContent = JSON.parse(storedUsername);
    } else {
      console.error('User name not found in session storage.');
    }
    
    // Remove the role and shift display logic if you don't need it
    
    if (storedShift) {
      switch (JSON.parse(storedShift)) {
        case 'NIGHT':
          userIcon.innerHTML = '<i class="fa-solid fa-user-secret"></i>';
          break;
        case 'EVENING':
          userIcon.innerHTML = '<i class="fa-solid fa-user-injured"></i>';
          break;
        case 'DAY':
          userIcon.innerHTML = '<i class="fa-solid fa-user-tie"></i>';
          break;
        default:
          userIcon.innerHTML = '<i class="fa-solid fa-user"></i>'; // Default icon
          break;
      }
    } else {
      console.error('User shift not found in session storage.');
      userIcon.innerHTML = '<i class="fa-solid fa-user"></i>'; // Default icon if shift is not found
    }
}
function showUserInfo() {
    const storedRole = sessionStorage.getItem('role');

    if (storedRole) {
        // Fetch the roles data from the JSON file
        fetch('../templates/roles.json')
            .then(response => response.json())
            .then(roles => {
                // Find the role that matches the stored role
                const userRole = JSON.parse(storedRole);
                const roleInfo = roles.find(role => role.role === userRole);

                if (roleInfo) {
                    // Display the role and description in Swal.fire
                    Swal.fire({
                        title: `Your role is: ${userRole}`,
                        text: roleInfo.description,
                        icon: 'info',
                        confirmButtonText: 'OK!',
                        customClass: {
                            title: 'swal2-title',
                            content: 'swal2-text',
                            popup: 'swal2-popup',
                            confirmButton: 'swal2-confirm',
                            cancelButton: 'swal2-cancel',
                        }
                    });
                } else {
                    // When rol is null or not set
                    Swal.fire({
                        title: 'Your role is not set.',
                        text: 'No specific role has been assigned to you.',
                        icon: 'info',
                        confirmButtonText: 'OK!',
                        customClass: {
                            title: 'swal2-title',
                            content: 'swal2-text',
                            popup: 'swal2-popup',
                            confirmButton: 'swal2-confirm',
                            cancelButton: 'swal2-cancel',
                        }
                    });
                }
            })
            .catch(error => {
                console.error('Error fetching roles data:', error);
                Swal.fire({
                    title: 'Error',
                    text: 'Unable to fetch role information.',
                    icon: 'error',
                    confirmButtonText: 'Close',
                    customClass: {
                        title: 'swal2-title',
                        content: 'swal2-textarea',
                        popup: 'swal2-popup',
                        confirmButton: 'swal2-confirm',
                        cancelButton: 'swal2-cancel',
                    }
                });
            });
    } else {
        console.error('User role not found in session storage.');
        Swal.fire({
            title: 'No Role Found',
            text: 'User role not found in session storage.',
            icon: 'error',
            confirmButtonText: 'Close',
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
    }
}

  
  

// Cancels a user task with a required comment
function cancelTask(userTaskId, taskId) {
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from session storage
    // console.log("taskId", taskId);
    // console.log("userTaskId", userTaskId);


    // Prompt user for a comment before canceling the task
    Swal.fire({
        // title: 'Comment Required',
        input: 'textarea',
        inputPlaceholder: 'Comment Required...',
        icon: 'info',
        showCancelButton: true,
        confirmButtonText: 'Submit',
        cancelButtonText: 'Cancel',
        customClass: {
            title: 'swal2-title',
            content: 'swal2-textarea',
            popup: 'swal2-popup',
            confirmButton: 'swal2-confirm',
            cancelButton: 'swal2-cancel',
        },
        inputValidator: (value) => {
            if (!value.trim()) {
                return 'Comment cannot be empty.';
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            // User provided a comment and confirmed the cancellation
            // Start by creating the comment in the database
            return fetch(`${endpoint}/comments`, {
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
        } else if (result.isDismissed) {
            console.log('User aborted the cancelation process.');
            return null; // Return null to indicate cancellation
        }
    }).then(response => {
        if (response === null) {
            return null; // Early exit if response is null
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(commentResponse => {
        if (commentResponse === null) {
            return null; // Early exit if commentResponse is null
        }
        console.log(commentResponse);
        // Comment created successfully, proceed to cancel the task
        return fetch(`${endpoint}/usertasks/${userTaskId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`
            }
        });
    }).then(response => {
        if (response === null) {
            return null; // Early exit if response is null
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(cancelResponse => {
        if (cancelResponse === null) {
            return null; // Early exit if commentResponse is null
        }
        console.log(cancelResponse);
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
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
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
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
    });
}
// Initiates a task, updating its status to 'IN_PROGRESS'
function initiateTask(userTaskId, taskId) {
    fetch(`${endpoint}/usertasks`, {
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
        console.log(message);

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
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
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
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
    });
}
// Completes a task with a required comment
function completeTask(userTaskId, taskId) {
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from session storage

    // Prompt user for a comment before marking the task as completed
    Swal.fire({
        // title: 'Completion Comment Required',
        input: 'textarea',
        inputPlaceholder: 'Comment Required...',
        icon: 'info',
        showCancelButton: true,
        confirmButtonText: 'Submit',
        cancelButtonText: 'Cancel',
        customClass: {
            title: 'swal2-title',
            content: 'swal2-textarea',
            popup: 'swal2-popup',
            confirmButton: 'swal2-confirm',
            cancelButton: 'swal2-cancel',
        },
        inputValidator: (value) => {
            if (!value.trim()) {
                return 'Comment cannot be empty.';
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            // User provided a comment and confirmed completion
            // Start by creating the comment in the database
            return fetch(`${endpoint}/comments`, {
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
        } else if (result.isDismissed) {
            console.log('User canceled completion process.');
            return null; // Return null to indicate cancellation
        }
    }).then(response => {
        if (response === null) {
            return null; // Early exit if response is null
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(commentMessage => {
        if (commentMessage === null) {
            return null; // Early exit if commentResponse is null
        }
        console.log(commentMessage);
        // Comment created successfully, now update the task status to 'COMPLETED'
        return fetch(`${endpoint}/usertasks`, {
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
        if (response === null) {
            return null; // Early exit if response is null
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(updateMessage => {
        if (updateMessage === null) {
            return null; // Early exit if commentResponse is null
        }
        console.log(updateMessage);
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
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
        fetchAndDisplayInProgressTasks();
    }).catch(error => {
        console.error('Error:', error);
        Swal.fire({
            title: 'Completion aborted!',
            // text: 'There was a problem cancelling the task.',
            icon: 'info',
            timer: 2000,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
    });
}



// Adds a comment to a task
function addCommentToTask(taskId) {
    const userId = sessionStorage.getItem('userId'); // Retrieve userId from session storage


    // Prompt user for a comment before adding it to the task
    Swal.fire({
        title: 'Add New Comment',
        input: 'textarea',
        inputPlaceholder: 'Enter your comment...',
        showCancelButton: true,
        confirmButtonText: 'Add Comment',
        confirmButtonColor: '#3498dbbc',
        cancelButtonText: 'Cancel',
        customClass: {
            title: 'swal2-title',
            content: 'swal2-textarea',
            popup: 'swal2-popup',
            confirmButton: 'swal2-confirm',
            cancelButton: 'swal2-cancel',
        },
        inputValidator: (value) => {
            if (!value.trim()) {
                return 'Comment cannot be empty.';
            }
        }
    }).then((result) => {
        if (result.isConfirmed && result.value) {
            // User provided a comment and confirmed the action
            // Proceed with creating the comment in the database
            return fetch(`${endpoint}/comments`, {
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
        } else if (result.isDismissed) {
            console.log('User canceled comment.');
            return null; // Return null to indicate cancellation
        }
    }).then(response => {
        if (response === null) {
            return null; // Early exit if response is null
        }
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.text();
    }).then(message => {
        if (message === null) {
            return null; // Early exit if commentResponse is null
        }
        if (message) {
            // Handle your successful comment addition or other messages here
            console.log(message);
        }
    }).catch(error => {
        console.error('Error:', error);
        Swal.fire({
            title: 'Comment aborted!',
            // text: 'There was a problem cancelling the task.',
            icon: 'info',
            timer: 2000,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
    });
}
// Sets up hover effect to display comments on task info buttons
function fetchTaskInfo(taskId) {
    fetch(`${endpoint}/tasks/${taskId}`)
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
                html: `<p class="task-description">${taskInfo.description}</p>
                       <p class="task-creator-info">Created by @${taskInfo.creatorName} ${taskInfo.creatorSurname}</p>`, // Including creator's information
                icon: 'info',
                confirmButtonText: 'Close',
                customClass: {
                    title: 'swal2-title',
                    content: 'swal2-textarea',
                    popup: 'swal2-popup',
                    confirmButton: 'swal2-confirm',
                    cancelButton: 'swal2-cancel',
                }
            });
        })
        .catch(error => {
            console.error('Error fetching task info:', error);
            // Optionally, handle errors or display a message if the fetch fails
        });
}
function setupCommentHoverEffect() {
    // Select all info buttons
    const infoButtons = document.querySelectorAll('.icon-button.info-button');

    infoButtons.forEach(button => {
        const taskId = button.getAttribute('data-task-id');
        const contentContainer = button.closest('.task-card').querySelector('.task-card-content');
        const originalContent = contentContainer.innerHTML;

        // Function to handle hover effect
        function handleHover() {
            // Check if comments are already rendered
            if (!contentContainer.classList.contains("comments-loaded")) {
                fetch(`${endpoint}/tasks/${taskId}`)
                    .then(response => response.json())
                    .then(task => {
                        const lastThreeComments = task.comments.slice(-3).reverse();
                        let commentsHtml;

                        if (lastThreeComments.length === 0) {
                            // Message to display when there are no comments
                            commentsHtml = '<p class="no-comments-message stylish-text">No comments were added to this task yet.</p>';
                        } else {

                            commentsHtml = lastThreeComments.map(comment => {
                                const date = new Date(comment.commentTimestamp);
                                const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                                const formattedDate = `${date.getDate().toString().padStart(2, '0')} ${monthNames[date.getMonth()]} at ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
                                
                                return `
                                    <div class="comment">
                                        <p class="comment-timestamp cursive">${formattedDate} @${comment.userName} ${comment.userSurname}</p>
                                        <p class="comment-text stylish-text">${comment.commentText}</p>
                                    </div>
                                `;
                            }).join('');

                        }

                        contentContainer.innerHTML = `<div class="comments-section">${commentsHtml}</div>`;
                        contentContainer.classList.add("comments-loaded"); // Mark as loaded
                    })
                    .catch(error => console.error('Error fetching comments:', error));
            }
        }

        // Function to handle hover end
        function handleHoverEnd() {
            contentContainer.innerHTML = originalContent;
            contentContainer.classList.remove("comments-loaded"); // Remove loaded mark
        }

        // Attach the hover handlers to the info button
        button.addEventListener('mouseenter', handleHover);
        button.addEventListener('mouseleave', handleHoverEnd);
    });
}






// Displays a message when there are no new assigned tasks
function displayNoTasksMessage() {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = `
        <div class="no-tasks-message">
            <h2>All Monitors Silent 🌃</h2>
            <p>Your vigilance is complete. The city breathes easy, for now.</p>
            <p>Check back for "New Assigned" tasks or monitor "Tasks In Progress".</p>
        </div>
    `;
}
// Displays a message when there are no tasks in progress
function displayNoInProgressTasksMessage() {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = `
        <div class="no-tasks-message">
            <h2>Tracking Systems Idle 🛸</h2>
            <p>Replicants lay dormant. Your pursuit has paused.</p>
            <p>Redirect to "New Assigned" or await updates in "Completed Tasks".</p>
        </div>
    `;
}
// Displays a message when there are no completed tasks
function displayNoCompletedTasksMessage() {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = `
        <div class="no-tasks-message">
            <h2>Memory Archives Empty 🚀</h2>
            <p>No echoes from past chases. The archives wait to be filled.</p>
            <p>Mark your achievements under "Completed Tasks" soon.</p>
        </div>
    `;
}
// Displays a message when there are no available work tasks
function displayNoAvailableWorkMessage(container) {
    container.innerHTML = `
        <div class="no-tasks-message">
            <p>Soon, "Available Work" will beckon with new missions.</p>
        </div>
    `;
}









// Handles clicks on task cards
function cardClickHandler(event) {
    const cardId = event.currentTarget.id;

    // Fetch and display the appropriate tasks based on the clicked card
    if (cardId === 'newAssignedTasks') {
        fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
    } else if (cardId === 'tasksInProgress') {
        fetchAndDisplayInProgressTasks();
    } else if (cardId === 'completedTasks') {
        fetchAndDisplayCompletedTasks();
    }
}



// Fetches and displays task counts based on status
function fetchAndDisplayTasksCount(status, countElementId) {
    const userId = JSON.parse(sessionStorage.getItem('userId'));

    fetch(`${endpoint}/users/${userId}`)
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
// Fetches and updates the count of available work tasks
function fetchAndUpdateAvailableWorkCount() {
    // Retrieve the current shift from sessionStorage
    const currentShift = JSON.parse(sessionStorage.getItem('shift'));

    fetch(`${endpoint}/tasks`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(allTasks => {
            // Filter to get only 'ACTIVE' tasks that match the current shift
            const activeTasksCount = allTasks.filter(task => 
                task.status === 'ACTIVE' && task.shiftStatus === currentShift).length;

            // Update the count in the DOM
            document.getElementById('activeTasksCount').textContent = activeTasksCount;
        })
        .catch(error => {
            console.error('Error fetching available work tasks:', error);
        });
}





// Renders assigned tasks into the tasks container
function renderAssignedTasks(assignedTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = ''; // Clear any existing tasks

    // Remove the 'available-work-container' class
    tasksContainer.classList.remove('available-work-container');

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
    setupCommentHoverEffect();
}
// Updates a task card to show confirmation options
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





// Fetches and displays in-progress tasks
function fetchAndDisplayInProgressTasks() {
    const userId = JSON.parse(sessionStorage.getItem('userId'));

    fetch(`${endpoint}/users/${userId}`)
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
// Renders in-progress tasks
function renderInProgressTasks(inProgressTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = ''; // Clear the tasks container before rendering

    // Remove the 'available-work-container' class
    tasksContainer.classList.remove('available-work-container');

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
    setupCommentHoverEffect();
}





// Fetches and displays completed tasks
function fetchAndDisplayCompletedTasks() {
    const userId = JSON.parse(sessionStorage.getItem('userId'));

    fetch(`${endpoint}/users/${userId}`)
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
// Renders completed tasks
function renderCompletedTasks(completedTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = '';

    // Remove the 'available-work-container' class
    tasksContainer.classList.remove('available-work-container');

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
    setupCommentHoverEffect();
}





// CUSTOM TASKS
// Renders 'Add New Task' card for exclusively creating custom tasks
function renderAddNewTaskCard(tasksContainer) {
    const addTaskCard = document.createElement('div');
    addTaskCard.classList.add('task-card', 'add-task-card');
    // addTaskCard.setAttribute('data-aos', 'flip-right');
    // addTaskCard.setAttribute('data-aos-duration', '1000');
    addTaskCard.innerHTML = `
        <div class="task-card-header">
            <div class="task-card-status custom-status">CUSTOM CARD</div>
            <div class="task-card-info">
                <button class="icon-button new-info-button" onclick="displayNewTaskInfo()">
                    <i class="fa-solid fa-circle-info"></i>
                </button>
            </div>
        </div>
        <div class="new-task-card-content">
            <h3>Create Your Custom Task</h3>
            <p>Personalize tasks to fit your specific needs.</p>
        </div>
        <div class="task-card-footer add-custom-footer">
            <button class="task-card-button add-task-button">+ Custom Task</button>
        </div>
    `;

    // Attach the event listener right after creating the button
    const addTaskBtn = addTaskCard.querySelector('.add-task-button');
    if (addTaskBtn) {
        addTaskBtn.addEventListener('click', createNewTask);
    }

    tasksContainer.prepend(addTaskCard); // Place the card at the beginning
}
// Displays a custom message for the new task info button
function displayNewTaskInfo() {
    Swal.fire({
        title: 'Create Your Custom Task',
        html: "<p style='color:#fff;'>Design tasks tailored to your specific requirements and objectives.</p>",
        icon: 'info',
        confirmButtonText: 'Got it!',
        customClass: {
            title: 'swal2-title',
            content: 'swal2-textarea',
            popup: 'swal2-popup',
            confirmButton: 'swal2-confirm',
            cancelButton: 'swal2-cancel',
        }
    });
}
// Handles the creation of a custom task form when '+ New Task' is clicked
function createNewTask() {
    // Change task card status to CUSTOM
    const taskCardStatus = document.querySelector('.task-card-status');
    taskCardStatus.textContent = 'CUSTOM';

    // Set new inner HTML with form-like inputs for custom task
    const newTaskCardContent = document.querySelector('.new-task-card-content');
    newTaskCardContent.style.position = 'relative'; // Ensure relative positioning for absolute child
    newTaskCardContent.innerHTML = `
        <div class="custom-field two">
            <input type="text" id="taskName" placeholder="Task Name" required>
            <label class="placeholder" for="taskName"></label>
        </div>
        <div class="custom-field two">
            <input type="text" id="taskDescription" placeholder="Task Description" required>
            <label class="placeholder" for="taskDescription"></label>
        </div>
    `;

    // Transform the '+ New Task' button into 'Cancel' and 'Submit' buttons in the footer
    const taskCardFooter = document.querySelector('.add-custom-footer');
    taskCardFooter.innerHTML = `
        <button class="task-card-button cancel-task-button">Cancel</button>
        <button class="task-card-button submit-task-button">Submit</button>
    `;

    const customStatus = document.querySelector('.custom-status');
    customStatus.innerHTML = `
        CUSTOM
    `;  

    // Adjust the footer to space the buttons appropriately
    taskCardFooter.style.display = 'flex';
    taskCardFooter.style.justifyContent = 'space-around';
    taskCardFooter.style.alignItems = 'center';
    taskCardFooter.style.gap = '10px';

    // Add event listeners to Cancel and Submit buttons
    document.querySelector('.cancel-task-button').addEventListener('click', resetTaskCard);
    document.querySelector('.submit-task-button').addEventListener('click', submitTask);
}
// Resets the task card to its original state for custom tasks
function resetTaskCard() {
    // Reset the content of the .new-task-card-content div to its original state
    const newTaskCardContent = document.querySelector('.new-task-card-content');
    newTaskCardContent.innerHTML = `
        <h3>Create Your Custom Task</h3>
        <p>Personalize tasks to fit your specific needs.</p>
    `;
    // Reset task card status to CUSTOM TASK
    const taskCardStatus = document.querySelector('.task-card-status');
    taskCardStatus.textContent = 'CUSTOM CARD';

    // Transform the 'Cancel' button back to '+ New Task'
    const taskCardFooter = document.querySelector('.add-custom-footer');
    taskCardFooter.innerHTML = `
        <button class="task-card-button add-task-button">+ Custom Task</button>
    `;

    const customStatus = document.querySelector('.custom-status');
    customStatus.innerHTML = `
        CUSTOM CARD
    `;  

    // Attach event listener to the '+ New Task' button
    document.querySelector('.add-task-button').addEventListener('click', createNewTask);
}
// Handles the 'Custom' button click to create inputs for a new task
function handleCustomButton() {
    createNewTask(); // Call createNewTask to revert to the custom task creation form
}
// Submits a new task to the server after validating the input fields are not empty
function submitTask() {
    // Get values from input fields
    const taskName = document.getElementById('taskName').value.trim();
    const taskDescription = document.getElementById('taskDescription').value.trim();
    
    // Check if inputs are empty
    if (!taskName || !taskDescription) {
        alert('Please fill in both the task name and description.');
        return; // Exit the function if validation fails
    }

    // Prepare the task data with the user ID from session storage
    const shiftStatus = JSON.parse(sessionStorage.getItem('shift'));
    const userId = Number(sessionStorage.getItem('userId'));

    const taskData = {
        name: taskName,
        description: taskDescription,
        creatorId: userId,
        category: "CUSTOM",
        shiftStatus: shiftStatus
    };

    // Continue with the fetch request if validation is successful
    fetch(`${endpoint}/tasks`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(taskData)
    })
    .then(response => response.text())
    .then(result => {
        Swal.fire({
            title: `${result} !`,
            icon: 'success', 
            timer: 1500,
            showConfirmButton: false,
            allowOutsideClick: false,
            allowEscapeKey: false,
            allowEnterKey: false,
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        });
        // Re-fetch the available work count and cards
        fetchAndDisplayAvailableTasks();
        // Re-fetch and update the counter for 'Available Work' tasks
        fetchAndUpdateAvailableWorkCount();
    })
    .catch(error => {
        console.error('Error:', error);
    });
}





// TEMPLATE TASKS
// Renders 'Add New Template Task' card
function renderAddNewTemplateTaskCard(tasksContainer) { 
    const addTemplateTaskCard = document.createElement('div');
    addTemplateTaskCard.classList.add('task-card', 'add-template-task-card');
    // addTemplateTaskCard.setAttribute('data-aos', 'flip-left');
    // addTemplateTaskCard.setAttribute('data-aos-duration', '1000');
    addTemplateTaskCard.innerHTML = `
        <div class="task-card-header">
            <div class="task-card-status template-status">TEMPLATE CARD</div>
            <div class="task-card-info">
                <button class="icon-button template-info-button" onclick="displayTemplateTaskInfo()">
                    <i class="fa-solid fa-circle-info"></i>
                </button>
            </div>
        </div>
        <div class="new-task-card-content" id="template-task-content">
            <h3>Select a Template for Quick Task Creation</h3>
            <p>Choose from a range of predefined templates.</p>
        </div>
        <div class="task-card-footer">
            <button class="task-card-button add-template-task-button">+ Template Task</button>
        </div>
    `;

    // Attach the event listener
    const addTemplateTaskBtn = addTemplateTaskCard.querySelector('.add-template-task-button');
    if (addTemplateTaskBtn) {
        addTemplateTaskBtn.addEventListener('click', createTemplateTaskButton);
    }

    tasksContainer.append(addTemplateTaskCard); // Append the card at the end
}
// Displays a custom message for the template task info button
function displayTemplateTaskInfo() {

    Swal.fire({
        title: 'Use Templates for Efficiency',
        html: "<p style='color:#fff;'>Select from existing templates to quickly set up common task types.</p>",
        icon: 'info',
        confirmButtonText: 'Understood',
        customClass: {
            title: 'swal2-title',
            content: 'swal2-textarea',
            popup: 'swal2-popup',
            confirmButton: 'swal2-confirm',
            cancelButton: 'swal2-cancel',
        }
    });
}
// Handles the creation of a template task buttons
function createTemplateTaskButton() {
    fetch('../templates/tasks.json') 
        .then(response => response.json())
        .then(templates => {
            const newTaskCardContent = document.querySelector('#template-task-content');
            newTaskCardContent.innerHTML = `
                <h3>Choose a template.</h3>
                <div class="tasks-from-template">
                    ${templates.map(template => `
                        <button class="generated-task-button" onclick="renderTemplateDetails(${template.id})">
                            ${template.btn}
                        </button>
                    `).join('')}
                </div>
            `;
        })
        .catch(error => console.error('Error fetching templates:', error));
}
function renderTemplateDetails(templateId) {
    fetch('../templates/tasks.json')
        .then(response => response.json())
        .then(templates => {
            const template = templates.find(t => t.id === templateId);
            if (template) {
                const taskDetailsContainer = document.querySelector('.add-template-task-card');
                

                // Update the task details container with the selected template's details
                taskDetailsContainer.innerHTML = `
                                            
                <div class="task-card-header">
                    <div class="task-card-status template-status">TEMPLATE</div>
                    <div class="task-card-info">
                        <button class="icon-button template-info-button" onclick="displayTemplateTaskInfo()">
                            <i class="fa-solid fa-circle-info"></i>
                        </button>
                    </div>
                </div>
                <div class="new-task-card-content" id="template-task-content">
                    <h3>${template.name}</h3>
                    <p>${template.description}</p>
                </div>
                <div class="task-card-footer">
                    <button class="task-card-button cancel-task-button" onclick="resetTemplateTaskCard()">Cancel</button>
                    <button class="task-card-button submit-task-button" onclick="submitTemplateTask(${templateId})">Submit</button>
                </div>

                `;

                const taskCardFooter = document.querySelector('.task-card-footer');

                // Adjust the footer to space the buttons appropriately
                taskCardFooter.style.display = 'flex';
                taskCardFooter.style.justifyContent = 'space-around';
                taskCardFooter.style.alignItems = 'center';
                taskCardFooter.style.gap = '10px';

            }
        })
        .catch(error => console.error('Error fetching template details:', error));
}
function resetTemplateTaskCard() {
    const taskDetailsContainer = document.querySelector('.add-template-task-card');
    taskDetailsContainer.innerHTML = `
        <div class="task-card-header">
            <div class="task-card-status template-status">TEMPLATE CARD</div>
            <div class="task-card-info">
                <button class="icon-button template-info-button" onclick="displayTemplateTaskInfo()">
                    <i class="fa-solid fa-circle-info"></i>
                </button>
            </div>
        </div>
        <div class="new-task-card-content" id="template-task-content">
            <h3>Select a Template for Quick Task Creation</h3>
            <p>Choose from a range of predefined templates.</p>
        </div>
        <div class="task-card-footer">
            <button class="task-card-button add-template-task-button">+ Template Task</button>
        </div>
    `;

    // Reattach the event listener to the '+ Template Task' button
    const addTemplateTaskBtn = taskDetailsContainer.querySelector('.add-template-task-button');
    if (addTemplateTaskBtn) {
        addTemplateTaskBtn.addEventListener('click', createTemplateTaskButton);
    }
}
// This function is specifically for submitting a template task
function submitTemplateTask(templateId) {
    fetch('../templates/tasks.json') // Replace with the actual path to your tasks.json file
        .then(response => response.json())
        .then(templates => {
            const template = templates.find(t => t.id === templateId);
            if (!template) {
                throw new Error('Template not found.');
            }

            // Construct the task data from the template
            const userId = Number(sessionStorage.getItem('userId'));
            const shiftStatus = JSON.parse(sessionStorage.getItem('shift'));
            
            const taskData = {
                name: template.name,
                description: template.description,
                creatorId: userId,
                category: "TEMPLATE",
                shiftStatus: shiftStatus
            };
            

            // Proceed with the fetch request to submit the task data
            return fetch(`${endpoint}/tasks`, { // Make sure this is the correct endpoint
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(taskData)
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text(); // Expecting a text response, not JSON
        })
        .then(result => {
            Swal.fire({
                title: `${result} !`,
                icon: 'success', // You can choose different icons such as 'success', 'error', 'warning', etc.
                timer: 1500,
                showConfirmButton: false,
                allowOutsideClick: false,
                allowEscapeKey: false,
                allowEnterKey: false,
                customClass: {
                    title: 'swal2-title',
                    content: 'swal2-textarea',
                    popup: 'swal2-popup',
                    confirmButton: 'swal2-confirm',
                    cancelButton: 'swal2-cancel',
                }
            });

            
            // Re-fetch the available work count and cards
            fetchAndDisplayAvailableTasks();
            // Re-fetch and update the counter for 'Available Work' tasks
            fetchAndUpdateAvailableWorkCount();
        })
        .catch(error => {
            console.error('Error:', error);
            alert(`An error occurred while submitting the task: ${error.message}`);
        });
}






// Fetches and displays available tasks
function fetchAndDisplayAvailableTasks() {
    // Retrieve the current shift from sessionStorage
    const currentShift = JSON.parse(sessionStorage.getItem('shift'));

    fetch(`${endpoint}/tasks`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(allTasks => {
            // Filter tasks that are active and match the current shift
            const availableTasks = allTasks.filter(task => 
                task.status === 'ACTIVE' && task.shiftStatus === currentShift);

            renderAvailableTasks(availableTasks);
        })
        .catch(error => {
            console.error('Error fetching available tasks:', error);
        });
}
// Renders available (active) tasks
function renderAvailableTasks(activeTasks) {
    const tasksContainer = document.querySelector('.tasks-container');
    tasksContainer.innerHTML = ''; // Clear existing tasks

    // Add the 'available-work-container' class
    tasksContainer.classList.add('available-work-container');

    // Create a container for the rest of the active tasks
    const activeTasksContainer = document.createElement('div');
    activeTasksContainer.classList.add('active-tasks-container');

    // Create a container for special task cards (Custom and Template)
    const specialTasksContainer = document.createElement('div');
    specialTasksContainer.classList.add('special-tasks-container');

    // Append the active task container first
    tasksContainer.appendChild(activeTasksContainer);

    // Append the special task container second
    tasksContainer.appendChild(specialTasksContainer);

    // Call function to render Custom Task card
    renderAddNewTaskCard(specialTasksContainer);
    // similar function for Template Task card
    renderAddNewTemplateTaskCard(specialTasksContainer);

    if (activeTasks.length === 0) {
        displayNoAvailableWorkMessage(activeTasksContainer);
    } else {
        // Sort tasks by ID in descending order to display the latest tasks first
        activeTasks.sort((a, b) => b.id - a.id);

        activeTasks.forEach(task => {
            const taskCard = document.createElement('div');
            taskCard.classList.add('task-card');
            taskCard.setAttribute('data-task-id', task.id);

            const taskCardHTML = `
                <div class="task-card-header">
                    <div class="task-card-status">ACTIVE</div>
                    <div class="task-card-info">
                        <button class="icon-button info-button" data-task-id="${task.id}" onclick="fetchTaskInfo(${task.id})">
                            <i class="fa-solid fa-circle-info"></i>
                        </button>
                    </div>
                </div>
                <div class="task-card-content">
                    <h3 class="task-card-headline">${task.name}</h3>
                    <p class="task-card-body">${task.description}</p>
                    <p class="task-card-subhead">Created by: ${task.creatorName} ${task.creatorSurname}</p>
                </div>
                <div class="task-card-footer">
                    <button class="task-card-button primary" id="assignBtn${task.id}">
                        Assign Task
                    </button>
                </div>
            `;

            taskCard.innerHTML = taskCardHTML;
            activeTasksContainer.appendChild(taskCard);

            // Event listener for the 'Assign Task' button
            const assignBtn = document.getElementById(`assignBtn${task.id}`);
            assignBtn.addEventListener('click', () => assignTask(task.id));
        });
    }
    
    setupCommentHoverEffect();
}







// Task Assignment Process

// Add event listener for role tags and shift headings
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('role-tag') || e.target.classList.contains('shift-heading')) {
        // Deselect all role tags and shift headings
        const allRoleTags = document.querySelectorAll('.role-tag');
        const allShiftHeadings = document.querySelectorAll('.shift-heading');
        allRoleTags.forEach(tag => tag.classList.remove('selected'));
        allShiftHeadings.forEach(heading => heading.classList.remove('selected'));

        // Select the clicked element
        e.target.classList.add('selected');

        // Store the selected shift value in a variable
        selectedShiftValue = e.target.getAttribute('data-value');
    }
});

// Helper function to update task status to 'IN_PROGRESS'
function updateTaskStatusToInProgress(taskId) {
    const actualUserShift = JSON.parse(sessionStorage.getItem('shift'));
    return fetch(`${endpoint}/tasks`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: taskId,
            status: 'IN_PROGRESS',
            shiftStatus: actualUserShift
        })
    });
}
// Helper function to update the task's shiftStatus
function updateTaskShiftStatus(taskId, selectedValue) {
    const updatedTaskData = {
        id: taskId,
        status: "ACTIVE",
        shiftStatus: selectedValue
    };

    return fetch(`${endpoint}/tasks`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedTaskData),
    });
}
// Helper function to create a comment
function createComment(taskId, comment) {
    const userId = Number(sessionStorage.getItem('userId'));

    return fetch(`${endpoint}/comments`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${sessionStorage.getItem('jwt')}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            text: comment,
            userId: userId,
            taskId: taskId
        })
    });
}
// Helper function to create a UserTask
function createUserTask(taskId, userId) {
    const assigner = Number(sessionStorage.getItem('userId'));

    return fetch(`${endpoint}/usertasks`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            assignerId: assigner,
            userId: userId,
            taskId: taskId
        })
    });
}


function fetchUsersAndCreateShiftContainers() {
    // Send a GET request to fetch the user data from '${endpoint}/users'
    return fetch(`${endpoint}/users`)
        // Once the response is received, parse it as JSON
        .then(response => response.json())
        .then(users => {
            // Get the "shift" and "userId" values from session storage
            const actualUserShift = JSON.parse(sessionStorage.getItem('shift'));
            const actualUserId = Number(sessionStorage.getItem('userId'));

            // Create an empty object to group users by their "shift"
            const usersByShift = {};

            // Loop through the users and group them by their "shift"
            users.forEach(user => {
                if (!usersByShift[user.shift]) {
                    usersByShift[user.shift] = [];
                }
                // Exclude the user whose ID matches the ID in session storage
                if (user.shift === actualUserShift && user.id !== actualUserId) {
                    usersByShift[user.shift].push(user);
                }
            });

            // Create HTML shift containers for the grouped users and headings for all shifts except the actual shift
            const shiftContainers = Object.keys(usersByShift).map(shift => {
                const shiftClass = shift.toLowerCase() + '-shift';
                const shiftId = shift.toLowerCase();
                const shiftDataValue = shift.toUpperCase();

                // Create HTML tags for each user in the group
                const userTags = usersByShift[shift].map(user => {
                    return `<span class="role-tag ${shiftClass}" data-value="${user.id}" data-shift="${user.shift}">${user.name} ${user.surname}</span>`;
                }).join('');

                const shiftHeadingClass = 'shift-heading';

                // Return the HTML structure for the shift container, excluding the heading for the actual shift
                return `
                    <div class="${shiftClass}-container">
                        ${shift !== actualUserShift ? `<h2 class="${shiftHeadingClass}" id="${shiftId}" data-value="${shiftDataValue}">${shiftDataValue} Shift</h2>` : ''}
                        <div class="role-tags">${userTags}</div>
                    </div>
                `;
            }).join('');

            // Return the HTML for all shift containers and headings
            return shiftContainers;
        });
}
function createAndHandleSwal(shiftContainers) {
    return Swal.fire({
        html: shiftContainers,
        input: 'textarea',
        inputPlaceholder: 'Comment Required...',
        showCancelButton: true,
        confirmButtonText: 'Submit',
        cancelButtonText: 'Cancel',
        customClass: {
            title: 'swal2-title',
            content: 'swal2-textarea',
            popup: 'swal2-popup-custom',
            confirmButton: 'swal2-confirm',
            cancelButton: 'swal2-cancel',
        },
        preConfirm: () => {
            const selectedRoleTag = document.querySelector('.role-tag.selected')?.dataset.value;
            const selectedShiftHeading = document.querySelector('.shift-heading.selected')?.getAttribute('data-value');
            let selectedValue = selectedRoleTag || selectedShiftHeading;
            const comment = Swal.getInput().value;

            if (!comment.trim()) {
                Swal.showValidationMessage('Comment cannot be empty.');
                return false;
            }

            if (!selectedValue || selectedValue === null) {
                selectedValue = Number(sessionStorage.getItem('userId'));
            }

            return { selectedValue, comment };
        }
    }).then((result) => {
        // Handle Swal result
        if (result.isConfirmed) {
            return result.value; // Return selectedValue and comment
        } else {
            return null; // Swal was dismissed or canceled
        }
    });
}

// Main function to assign a task
function assignTask(taskId) {
    // Fetch the users from the server and create shiftContainers
    fetchUsersAndCreateShiftContainers().then(shiftContainers => {
        createAndHandleSwal(shiftContainers)
            .then(result => {
                if (result) {
                    const { selectedValue, comment } = result;

                    // Check if selectedValue is a number (user ID) or a string (shift)
                    if (!isNaN(parseInt(selectedValue))) {
                        // If selectedValue is a number, assign task to a user
                        assignTaskToUser(taskId, parseInt(selectedValue), comment);
                    } else {
                        // If selectedValue is a string, assign task to a group (shift)
                        assignTaskToGroup(taskId, selectedValue, comment);
                    }
                } else {
                    // Handle dismissal or cancellation of Swal
                    console.log('User canceled the assignment process.');
                }
            });
    });
}
// Helper function to assign a task to a user
// 1) Update task status to 'IN_PROGRESS'
// 2) Create a comment
// 3) Create a UserTask 
function assignTaskToUser(taskId, userId, comment) {
    console.log('Starting task assignment process...');

    // Step 1: Update task status to 'IN_PROGRESS'
    updateTaskStatusToInProgress(taskId)
        .then(response => {
            if (response.ok) {
                console.log('Task status updated to IN_PROGRESS');

                // Step 2: Create a comment for the task
                return createComment(taskId, comment);
            } else {
                throw new Error('Failed to update task status');
            }
        })
        .then(response => {
            if (response.ok) {
                console.log('Comment added to the task');

                // Step 3: Create a UserTask
                return createUserTask(taskId, userId);
            } else {
                throw new Error('Failed to add comment to task');
            }
        })
        .then(response => {
            if (response.ok) {
                console.log('UserTask created successfully');
                console.log('Task assigned to user with ID:', userId);
                console.log('Comment:', comment);

                // Re-fetch Available Work tasks to reflect the changes
                // fetchAndDisplayAvailableTasks();
                // Also, re-fetch and update the counter for 'Available Work' tasks
                fetchAndUpdateAvailableWorkCount();
                
                // Re-fetch and update the counter for 'New Assigned' tasks
                const actualUserId = Number(sessionStorage.getItem('userId'));
                if ((userId === actualUserId)){
                    fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
                } else {
                    // Re-fetch Available Work tasks to reflect the changes
                    fetchAndDisplayAvailableTasks();
                }
                if ((userId === actualUserId)){
                    // Display a success message to the user
                    Swal.fire({
                        title: 'Self-assignment successful!',
                        icon: 'success',
                        timer: 2500,
                        showConfirmButton: false,
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        allowEnterKey: false,
                        customClass: {
                            title: 'swal2-title',
                            content: 'swal2-textarea',
                            popup: 'swal2-popup',
                            confirmButton: 'swal2-confirm',
                            cancelButton: 'swal2-cancel',
                        }
                    });
                    
                } else {
                    // Display a success message to the user
                    Swal.fire({
                        title: 'Task assigned to the user!',
                        icon: 'success',
                        timer: 2500,
                        showConfirmButton: false,
                        allowOutsideClick: false,
                        allowEscapeKey: false,
                        allowEnterKey: false,
                        customClass: {
                            title: 'swal2-title',
                            content: 'swal2-textarea',
                            popup: 'swal2-popup',
                            confirmButton: 'swal2-confirm',
                            cancelButton: 'swal2-cancel',
                        }
                    });
                }

            } else {
                throw new Error('Failed to create UserTask');
            }
        })
        .catch(error => {
            console.error('Error during task assignment process:', error);
        });
}
// Helper function to assign a task to a group (shift)
// 1) Update task shiftStatus to whatever shift is passed
// 2) Create a comment
function assignTaskToGroup(taskId, selectedValue, comment) {
    console.log('Starting task assignment to group process...');

    // Step 1: Update the task's shiftStatus
    updateTaskShiftStatus(taskId, selectedValue)
        .then(response => {
            if (response.ok) {
                console.log(`Task shift status updated to ${selectedValue}`);

                // Step 2: Create a comment for the task
                return createComment(taskId, comment);
            } else {
                throw new Error('Failed to update task shift status');
            }
        })
        .then(response => {
            if (response.ok) {
                console.log('Comment added to the task');
                console.log('Task assigned to group:', selectedValue);
                console.log('Comment:', comment);

                // Re-fetch Available Work tasks to reflect the changes
                fetchAndDisplayAvailableTasks();
                // Also, re-fetch and update the counter for 'Available Work' tasks
                fetchAndUpdateAvailableWorkCount();

                // Display a success message to the user
                Swal.fire({
                    title: `Task assigned to the group ${selectedValue}!`,
                    icon: 'success',
                    timer: 2500,
                    showConfirmButton: false,
                    allowOutsideClick: false,
                    allowEscapeKey: false,
                    allowEnterKey: false,
                    customClass: {
                        title: 'swal2-title',
                        content: 'swal2-textarea',
                        popup: 'swal2-popup',
                        confirmButton: 'swal2-confirm',
                        cancelButton: 'swal2-cancel',
                    }
                });

            } else {
                throw new Error('Failed to add comment to task');
            }
        })
        .catch(error => {
            console.error('Error during task assignment to group process:', error);
        });
}



// Event listener for DOMContentLoaded to ensure the DOM is fully loaded
document.addEventListener('DOMContentLoaded', function () {
    console.log('🚀 Developed by Maximo Timochenko. Explore more at https://github.com/mtimoxenko ');
    AOS.init(); // Initialize animations

    // Call this function when the page loads or when the relevant session storage items are updated
    updateUserDisplay();

    // Fetch and display the count of tasks based on their status and render assigned tasks
    fetchAndDisplayTasksCount('ASSIGNED', '#newAssignedCount');
    fetchAndDisplayTasksCount('IN_PROGRESS', '#tasksInProgressCount');
    fetchAndDisplayTasksCount('COMPLETED', '#completedTasksCount');
    fetchAndUpdateAvailableWorkCount();

    // Logout button event listener
    const btnCloseApp = document.getElementById('closeApp');
    btnCloseApp.addEventListener('click', function () {
        Swal.fire({
            title: 'Are you leaving?',
            showCancelButton: true,
            confirmButtonText: 'Confirm',
            cancelButtonText: 'Cancel',
            customClass: {
                title: 'swal2-title',
                content: 'swal2-textarea',
                popup: 'swal2-popup',
                confirmButton: 'swal2-confirm',
                cancelButton: 'swal2-cancel',
            }
        }).then((result) => {
            if (result.isConfirmed) {
                sessionStorage.removeItem('jwt');
                sessionStorage.removeItem('role');
                sessionStorage.removeItem('shift');
                sessionStorage.removeItem('userId');
                sessionStorage.removeItem('userName');
                // sessionStorage.clear();
                // localStorage.removeItem('animationPlayed');
                window.location.replace('./index.html');
            }
        });
    });

    // Attach cardClickHandler to each card
    const cards = document.querySelectorAll('.task-list.card');
    cards.forEach(card => {
        card.addEventListener('click', cardClickHandler);
    });

    // Attach event listener to the "Available Work" card
    const availableWorkCard = document.getElementById('activeTasks');
    if (availableWorkCard) {
        availableWorkCard.addEventListener('click', function() {
            // This functions will be triggered when the "Available Work" card is clicked
            // Re-fetch Available Work tasks to reflect the changes
            fetchAndDisplayAvailableTasks();
            // Also, re-fetch and update the counter for 'Available Work' tasks
            fetchAndUpdateAvailableWorkCount();
        });
    }

    // Assuming tasksContainer is defined and accessible here
    // const tasksContainer = document.querySelector('.tasks-container');
    // renderAddNewTaskCard(tasksContainer);

});

