if (!sessionStorage.jwt || sessionStorage.jwt != 1 ) {
  location.replace('./index.html');
}


window.addEventListener('load', function () {
  /* ------------------------- AOS lib. init -------------------------------- */
  AOS.init();

  // const token = JSON.parse(localStorage.jwt);

  const endpointDate = './json/turno.json'
  const endpointDentist = 'http://localhost:8080/dentist'
  const endpointPatient = 'http://localhost:8080/patient'
  const endpointAppointment = 'http://localhost:8080/appointment'  

  const formAddAppointment = document.querySelector('.new-appointment')
  const btnCloseApp = document.querySelector('#closeApp')

  getUserName()
  getAppointment()
  


  function renderPatient(){
    const selectPatient = document.getElementById('select-patient')

    fetch(endpointPatient)
      .then(response=>response.json())
      .then(data=>{
        console.log(data);
        data.forEach(patient=>{
          selectPatient.innerHTML+=`
          <div><input type="radio" name="patient" id="${patient.id}">
          <label for="${patient.id}">${patient.name} ${patient.surname}</label>
          </div>`
        })
      })
  }
  renderPatient()


  function renderDentist(){
    const selectDentist = document.getElementById('select-dentist')

    fetch(endpointDentist)
      .then(response=>response.json())
      .then(data=>{
        console.log(data);
        data.forEach(dentist=>{
          selectDentist.innerHTML+=`
          <div><input type="radio" name="dentist" id="${dentist.id}">
          <label for="${dentist.id}">${dentist.name} ${dentist.surname}</label>
          </div>`
        })
      })
  }
  renderDentist()


  function dateCatch(){
    const year = document.getElementById('year').value
    const month = document.getElementById('month').value
    const day = document.getElementById('day').value
    const time = document.getElementById('time').value

    return year+'/'+month+'/'+day+' - '+time
  }



    /* -------------------------------------- */
    /*           [1] FUNCTION: Logout         */
    /* -------------------------------------- */

  btnCloseApp.addEventListener('click', function () {

    Swal.fire({
      title: 'Are you leaving?',
      icon: 'question',
      iconColor: '#545454',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Confirm',
      cancelButtonText: 'Cancel',
      background: 'rgba(255, 255, 255, .9)'
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.clear()
        location.replace('./index.html')
      }
    })

  })

  /* ------------------------------------------------------------------ */
  /*                 [2] FUNCTION: Get user name [GET]                */
  /* ------------------------------------------------------------------ */

  function getUserName() {

    const name = sessionStorage.getItem('userName')

    const userName = document.querySelector('.user-info p')

    userName.innerText = JSON.parse(name)

  //   const settings = {
  //     method: 'GET',
  //     headers: {
  //       authorization: token
  //     }
  //   };

  //   fetch(endpointGetUser, settings)
  //     .then(response => response.json())
  //     .then(data => {
  //       const usrName = document.querySelector('.user-info p');
  //       usrName.innerText = data.firstName;
  //     })
  //     .catch(error => console.log(error));
  }


  /* ------------------------------------------------------------------- */
  /*                    [3] FUNCTION: Add Appointment [POST]                    */
  /* ------------------------------------------------------------------- */

  formAddAppointment.addEventListener('submit', function (event) {
    event.preventDefault()

    const dentist = document.querySelectorAll('[name=dentist]')
    const patient = document.querySelectorAll('[name=patient]')
    const date = dateCatch()

    const payload = {
      date: date,
      patient: {id:""}, 
      dentist: {id:""} 
    }

    dentist.forEach(dentist=>{
      if(dentist.checked){
        payload.dentist.id = dentist.id
      }
    })

    patient.forEach(patient=>{
      if(patient.checked){
        payload.patient.id = patient.id
      }
    })

    console.log(payload)

    const settings = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    }
    
    
    if(payload.date.includes('000') || payload.patient.id == '' || payload.dentist.id == ''){
      alert('You must select patient, dentist and date-time')
    }
    else{
      fetch(endpointAppointment, settings)
      .then(response => {
          if (response.ok) {
              response.json()
              getAppointment()            
          }
          else{
              return response.text().then((errorMessage) => {
              throw new Error(errorMessage)
              })
          }
      })
      .catch(error => {
          console.error(error.message)
          console.log(error.message)
          errorResponse(formAddAppointment, error.message)
      })
      formAddAppointment.reset()      
    }

  })


  /* ----------------------------------------------------------- */
  /*                  [4] FUNCTION: Render Appointment           */
  /* ----------------------------------------------------------- */

  function getAppointment(){
    fetch(endpointAppointment)
    .then(response=>response.json())
    .then(data=>{
      console.log(data)
      renderAppointment(data)
      searchAppointmentId()
    })
  }

  function renderAppointment(list){

    const appointmentToDo = document.querySelector('#appointment-list')
    const patient = document.getElementById('patientLabel')
    const dentist = document.getElementById('dentistLabel')

    appointmentToDo.innerHTML = ""
    patient.innerText = "/"
    dentist.innerText = "/"    

      list.forEach(appointment=>{
        appointmentToDo.innerHTML+=`
        <li><div class='ps'><p>${appointment.id} - ${appointment.date}</p> <p id='centerp'>${appointment.dentist.surname}, ${appointment.dentist.name}</p> <p id='finalp'>${appointment.patient.surname}, ${appointment.patient.name}</p></div>
        </li>`
      })
  }


  /* -------------------------------------------------------------------------- */
  /*                     [5] FUNCTION: Search appointment [GET]                 */
  /* -------------------------------------------------------------------------- */

  function searchAppointmentId(){

    const searchForm = document.querySelector('.appointment-search')
    const patient = document.getElementById('patientLabel')
    const dentist = document.getElementById('dentistLabel')
    const date = document.getElementById('inputDate')

    const searchAppointmentId = document.getElementById('inputAppointmentId')


    searchForm.addEventListener('submit', function(e){
      e.preventDefault()

      const id = searchAppointmentId.value
      const url = `${endpointAppointment}/${id}`
      const updateButton = document.querySelector('.update-button')
      const deleteButton = document.querySelector('#delete-button')

      const settings = {
        method: 'GET',
      }

      fetch(url, settings)
      .then(response=>response.json())
      .then(data=>{
        console.log(data)
            patient.innerText = data.patient.surname + (', ') + data.patient.name
            dentist.innerText = data.dentist.surname + (', ') + data.dentist.name
            date.value = data.date
            updateButton.disabled = false
            updateAppointment()
            deleteButton.disabled = false
            appointmentDelete()
      })
      .catch(err=>{
        patient.innerText = '/'
        dentist.innerText = '/'
        date.value = ''
        updateButton.disabled = true
        deleteButton.disabled = true
      })
      const bugBox = document.querySelector('#errores')
      bugBox.remove()
    })
  }


  /* -------------------------------------------------------------------------- */
  /*                     [6] FUNCTION: Update appointment [PUT]                 */
  /* -------------------------------------------------------------------------- */


  function updateAppointment(){

    const updateform = document.querySelector('.appointment-update')
    const searchForm = document.querySelector('.appointment-search')

    const updateButton = document.querySelector('.update-button')
    const deleteButton = document.querySelector('#delete-button')
    const appointmentId = document.getElementById('inputAppointmentId')

    const date = document.getElementById('inputDate')


    updateButton.addEventListener('click', function(e){
      e.preventDefault()

      const id = appointmentId.value
      const url = `${endpointAppointment}/${id}`

      console.log(id)

      const payload = {
        date: date.value
      }

      const settings = {
        method: 'PUT',
        headers: {
          "Content-type": "application/json"
        },
        body: JSON.stringify(payload)
      }

      if(payload.date == ''){
        errorMessage(formAddAppointment)
      }
      else{
        fetch(url, settings)
        .then(response => {
          console.log(response.status)
          getAppointment()
          deleteButton.disabled = true
          const bugBox = document.querySelector('#errores')
          bugBox.remove()          
        })
        updateform.reset()
        searchForm.reset()
      }
    })
  }


  /* -------------------------------------------------------------------------- */
  /*                     [6] FUNCTION: Delete appointment [DELETE]              */
  /* -------------------------------------------------------------------------- */

  function appointmentDelete() {

    const updateform = document.querySelector('.appointment-update')
    const searchForm = document.querySelector('.appointment-search')

    const deleteButton = document.querySelector('#delete-button')
    const appointmentId = document.getElementById('inputAppointmentId')


      deleteButton.addEventListener('click', function (e){
          e.preventDefault()

          const id = appointmentId.value
          const url = `${endpointAppointment}/${id}`
    
          console.log(id)

          const settings = {
            method: 'DELETE'
          }

          Swal.fire({
            title: `Confirm delete patient Id: ${id}`,
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Confirm',
            cancelButtonText: 'Cancel',
            background: 'rgba(0, 0, 0, .8)'
          })
          .then((result) => {
            if (result.isConfirmed) {
              fetch(url, settings)
              .then(response => {
                console.log(response.status);
                getAppointment()
              })
              updateform.reset()
              searchForm.reset()
              deleteButton.disabled = true
            }
          })
      })
  }

})
