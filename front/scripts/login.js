window.addEventListener('load', function () {
    console.log('🚀 Developed by Maximo Timochenko. Explore more at https://github.com/fr3m3n ');


    const form = document.querySelector('form');
    const email = document.querySelector('#inputEmail');
    const password = document.querySelector('#inputPassword');
    const endpointLogin = 'http://184.73.218.172:8080/users/login';

    form.addEventListener('submit', function (event) {
        event.preventDefault();

        if (validateInput(email, password)) {
            const payload = {
                email: email.value,
                password: password.value
            };

            loginUser(payload);
        }
    });

    function validateInput(email, password) {
        if (email.value.trim() === '' || password.value.trim() === '') {
            displayMessage('Please complete all fields correctly.', true);
            return false;
        }
        return true;
    }

    function loginUser(payload) {
        fetch(endpointLogin, {
            method: 'POST',
            headers: {'Content-type': 'application/json; charset=UTF-8'},
            body: JSON.stringify(payload)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Invalid credentials or server error');
                });
            }
            return response.json();
        })
        .then(data => {
            handleLoginSuccess(data);
        })
        .catch(err => {
            displayMessage(err.message, true);
        });
    }

    function handleLoginSuccess(data) {
        displayMessage('Login successful. Redirecting...', false);
    
        // Store the user name and token in sessionStorage
        sessionStorage.setItem('userName', JSON.stringify(data.userName));
        sessionStorage.setItem('jwt', JSON.stringify(data.token));
        sessionStorage.setItem('userId', JSON.stringify(data.userId));
        sessionStorage.setItem('shift', JSON.stringify(data.shift));
        sessionStorage.setItem('role', JSON.stringify(data.role));
    

        // Set a delay before redirecting to allow the user to see the success message
        setTimeout(() => {
            const redirectUrl = data.token === 33 ? './admin.html' : './tasks.html';
            location.replace(redirectUrl);
        }, 1700); // Redirect after 1 second
    }
    

    function displayMessage(message, isError = false) {
        const messageBox = document.querySelector('#errores') || createMessageBox();
        messageBox.innerHTML = `<p><small>${message}</small></p>`;
        if (isError) {
            messageBox.style.backgroundColor = 'rgba(255, 0, 0, 0.3)';
        } else {
            messageBox.style.backgroundColor = 'rgba(0, 255, 0, 0.3)';
        }
    }

    function createMessageBox() {
        const divTemplate = document.createElement('div');
        divTemplate.setAttribute('id', 'errores');
        divTemplate.style = 'padding:.5em 1em;color: white;margin-top: 1em;';
        form.appendChild(divTemplate);
        return divTemplate;
    }


});
