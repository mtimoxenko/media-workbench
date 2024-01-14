window.addEventListener('load', function () {
    const form = document.querySelector('form');
    const endpoint = 'http://localhost:8080/users';

    // Attach click event listeners to role tags
    document.querySelectorAll('.role-tag').forEach(tag => {
        tag.addEventListener('click', function() {
            // Remove the 'selected' class from all tags
            document.querySelectorAll('.role-tag').forEach(t => t.classList.remove('selected'));

            // Add the 'selected' class to the clicked tag
            this.classList.add('selected');

            // Update the hidden input value
            document.getElementById('inputRole').value = this.getAttribute('data-value');
        });
    });

    // Attach click event listeners to shift tags
    document.querySelectorAll('.shift-tag').forEach(tag => {
        tag.addEventListener('click', function() {
            // Remove the 'selected' class from all shift tags
            document.querySelectorAll('.shift-tag').forEach(t => t.classList.remove('selected'));

            // Add the 'selected' class to the clicked shift tag
            this.classList.add('selected');

            // Update the hidden input value
            document.getElementById('inputShift').value = this.getAttribute('data-value');
        });
    });

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        const name = document.querySelector('#inputName').value.trim();
        const surname = document.querySelector('#inputSurname').value.trim();
        const email = document.querySelector('#inputEmail').value.trim();
        const password = document.querySelector('#inputPassword').value.trim();
        // Retrieve the values from the hidden inputs
        const shift = document.getElementById('inputShift').value || null;
        const rol = document.getElementById('inputRole').value || null;

        if (validateInput(name, surname, email, password, shift, rol)) {
            submitForm(name, surname, email, password, shift, rol);
        }
    });


    function validateInput(name, surname, email, password, shift, rol) {
        // Check for empty required fields
        if (!name || !surname || !email || !password || !shift) {
            displayMessage('All fields except Role are required.', true);
            return false;
        }
    
        // Validate email format
        if (!/\S+@\S+\.\S+/.test(email)) {
            displayMessage('Invalid email format.', true);
            return false;
        }
    
        // If rol is provided, ensure it's a valid enum option
        const validRoles = ['ATTENDANT', 'REPORTER', 'HELPER', '']; // '' represents no selection
        if (rol && !validRoles.includes(rol)) {
            displayMessage('Invalid role selected.', true);
            return false;
        }
    
        // Ensure shift is a valid enum option
        const validShifts = ['DAY', 'EVENING', 'NIGHT'];
        if (!validShifts.includes(shift)) {
            displayMessage('Invalid shift selected.', true);
            return false;
        }
    
        return true;
    }
    

    function submitForm(name, surname, email, password, shift, rol) {
        // Include shift and rol in the payload, handling rol being optional
        const payload = {
            name,
            surname,
            email,
            password,
            shift,
            // Include rol only if it's not null
            ...(rol && { rol })
        };
    
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
            sessionStorage.setItem('shift', JSON.stringify(loginUserResponse.shift));
            sessionStorage.setItem('rol', JSON.stringify(loginUserResponse.rol));
    
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
