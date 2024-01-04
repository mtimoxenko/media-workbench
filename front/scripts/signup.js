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
            return response.text();
        })
        .then(message => {
            displayMessage(message, false);
            sessionStorage.setItem('userName', JSON.stringify(name));
            sessionStorage.setItem('jwt', JSON.stringify(isAdmin ? 33 : 1));
            setTimeout(() => {
                window.location.href = './tasks.html';
            }, 2000);
        })
        .catch(error => {
            displayMessage(error.message, true);
        });
    }

    function displayMessage(message, isError) {
        const messageBox = document.querySelector('#messageBox') || createMessageBox();
        messageBox.innerHTML = `<p><small>${message}</small></p>`;
        messageBox.className = isError ? 'error-message' : 'success-message';
    }

    function createMessageBox() {
        const messageBox = document.createElement('div');
        messageBox.setAttribute('id', 'messageBox');
        messageBox.className = 'message-box';
        form.appendChild(messageBox);
        return messageBox;
    }
});
