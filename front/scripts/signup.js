window.addEventListener('load', function () {

    const form = document.querySelector('form')
    const endpoint = 'http://localhost:8080/user'

    const name = document.querySelector('#inputName')
    const surname = document.querySelector('#inputSurname')
    const email = document.querySelector('#inputEmail')
    const pass = document.querySelector('#inputPassword')
    const admin = document.querySelector('[name=role]')


    form.addEventListener('submit', function (event) {
        event.preventDefault()

        const payload = {
            name: name.value,
            surname: surname.value,
            email: email.value,
            password: pass.value,
            admin: false
        }

        if(admin.checked){
            payload.admin = true
        }

        console.log(payload)

        const config = {
            method: 'POST',
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
            body: JSON.stringify(payload),   
        }


        if (payload.name == '' || payload.name.includes(' ') || !isNaN(payload.name)) {
            errorMessage(form)
        }
        else if(payload.surname == '' || payload.surname.includes(' ') || !isNaN(payload.surname)){
            errorMessage(form)
        }
        else if(payload.email == '' || payload.email.includes(' ') || !payload.email.includes('@')){
            errorMessage(form)
        }
        else if (payload.password == '' || payload.password.includes(' ')) {
            errorMessage(form)
        }
        else{
            fetch(endpoint, config)
                .then((response) => {
                    if (response.ok) {

                        response.json()
                        sessionStorage.setItem('userName', JSON.stringify(payload.name))
                        

                        if(payload.admin){
                            sessionStorage.setItem('jwt', JSON.stringify(33))
                            location.replace('./admin.html')
                        }
                        else if(!payload.admin){    
                            sessionStorage.setItem('jwt', JSON.stringify(1))              
                            location.replace('./tasks.html')
                        }     
                    }
                    else {
                        return response.text().then((errorMessage) => {
                            throw new Error(errorMessage)
                        })
                    }
                })

                .catch(error => {
                    console.error(error.message)
                    console.log(error.message)
                    errorResponse(form, error.message)                    
                })

            form.reset()            
        }

    })



})
