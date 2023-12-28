/* ---------------------------------- texto --------------------------------- */
function validaText(text) {
    
}

function normalizeText(text) {
    
}

/* ---------------------------------- email --------------------------------- */
function validateEmail(email) {
    
}

function normalizarEmail(email) {
    
}

/* -------------------------------- password -------------------------------- */
function validatePass(password) {
    
}




/* ------------------------------ errorMessages ----------------------------- */

function errorMessage(article){
    const bugBox = document.querySelector('#errores')

    if (bugBox) {
        bugBox.remove()
    }

    const divTemplate = document.createElement('div')
    divTemplate.setAttribute('id', 'errores')
    divTemplate.style = "background:rgba(255, 0, 0, 0.3);padding:.5em 1em;color: white;margin-top: 1em;"
    divTemplate.innerHTML += `<p><small>You must complete data correctly, without leaving empty fields or spaces</small></p>`
    article.appendChild(divTemplate)
}

function errorResponse(article, info){
    const bugBox = document.querySelector('#errores')

    if (bugBox) {
        bugBox.remove()
    }

    const divTemplate = document.createElement('div')
    divTemplate.setAttribute('id', 'errores')
    divTemplate.style = "background:rgba(255, 0, 0, 0.3);padding:.5em 1em;color: white;margin-top: 1em;"
    divTemplate.innerHTML = `<p><small>${info}</small></p>`
    article.appendChild(divTemplate)
}


