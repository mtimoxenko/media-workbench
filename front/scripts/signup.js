window.addEventListener('load', function () {
    const form = document.querySelector('form');
    const endpoint = 'http://localhost:8080/users';

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const name = document.querySelector('#inputName').value.trim();
        const surname = document.querySelector('#inputSurname').value.trim();
        const email = document.querySelector('#inputEmail').value.trim();
        const password = document.querySelector('#inputPassword').value.trim();
        const isAdmin = document.querySelector('[name=role]')?.checked || false;

        if (validateInput(name, surname, email, password)) {
            submitForm(name, surname, email, password, isAdmin);
        }
    });

    function validateInput(name, surname, email, password) {
        if (!name || !surname || !email || !password) {
            displayMessage('All fields are required.', true);
            return false;
        }

        if (!/\S+@\S+\.\S+/.test(email)) {
            displayMessage('Invalid email format.', true);
            return false;
        }

        return true;
    }

    function submitForm(name, surname, email, password, isAdmin) {
        const payload = { name, surname, email, password, isAdmin };
    
        fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json; charset=UTF-8' },
            body: JSON.stringify(payload)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.json(); // Expecting JSON response now
        })
        .then(loginUserResponse => {
            // Store the response details in sessionStorage
            sessionStorage.setItem('userName', JSON.stringify(loginUserResponse.userName));
            sessionStorage.setItem('jwt', JSON.stringify(loginUserResponse.token));
            sessionStorage.setItem('userId', JSON.stringify(loginUserResponse.userId));
    
            // Display success message and redirect
            Swal.fire({
                title: 'User created successfully!',
                text: 'Please wait, redirecting...',
                icon: 'success',
                showConfirmButton: false,
                timer: 3500,
            }).then(() => {
                window.location.href = './tasks.html'; // Redirect based on user role if necessary
            });
        })
        .catch(error => {
            displayMessage(error.message, true);
        });
    }
    

    function displayMessage(message, isError) {
        // Remove any existing error message
        const existingError = document.querySelector('.error-message');
        if (existingError) {
            existingError.remove();
        }
    
        if (isError) {
            // Create a new error message element
            const errorMessage = document.createElement('div');
            errorMessage.className = 'error-message';
            errorMessage.innerHTML = `<p><small>${message}</small></p>`;
    
            // Insert the error message after the form
            const form = document.querySelector('form');
            form.insertAdjacentElement('afterend', errorMessage);
        } else {
            // Use SweetAlert for success messages with customized text and no "OK" button
            Swal.fire({
                title: message,
                text: 'Please wait, redirecting...',
                icon: 'success',
                showConfirmButton: false,
                timer: 3500,
            }).then(() => {
                window.location.href = './tasks.html';
            });
        }
    }
    
    

    function createMessageBox() {
        const messageBox = document.createElement('div');
        messageBox.setAttribute('id', 'messageBox');
        messageBox.className = 'message-box';
        form.appendChild(messageBox);
        return messageBox;
    }
});
