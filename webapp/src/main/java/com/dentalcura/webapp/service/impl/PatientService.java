package com.dentalcura.webapp.service.impl;

import com.dentalcura.webapp.dto.address.AddressResponse;
import com.dentalcura.webapp.dto.patient.CreatePatientRequest;
import com.dentalcura.webapp.dto.patient.UpdatePatientRequest;
import com.dentalcura.webapp.dto.patient.PatientResponse;
import com.dentalcura.webapp.model.Task;
import com.dentalcura.webapp.repository.IUserTaskRepository;
import com.dentalcura.webapp.service.IPatientService;
import com.dentalcura.webapp.utils.exceptions.CustomNotFoundException;
import com.dentalcura.webapp.utils.exceptions.DuplicateNiNumberException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Getter @Setter
@Service
public class PatientService implements IPatientService {

    private final static Logger LOGGER = Logger.getLogger(PatientService.class);

    @Autowired
    private IUserTaskRepository patientRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertPatient(CreatePatientRequest createPatientRequest) {
        if (isNiNumberDuplicated(createPatientRequest.niNumber())) {
            throw new DuplicateNiNumberException("NI Number [" + createPatientRequest.niNumber() + "] is already in use.");
        }

        Task task = mapper.convertValue(createPatientRequest, Task.class);
        Address address = task.getAddress();

        address.setTask(task);
        task.setAddress(address);

        patientRepository.save(task);
        LOGGER.info("New patient was registered [" + task.getName() + " " + task.getSurname() + "]");
    }

    @Override
    public List<PatientResponse> selectAllPatient() {
        List<Task> tasks = patientRepository.findAll();
        List<PatientResponse> patientResponses = new ArrayList<>();

        for(Task task : tasks){
            patientResponses.add(
                    new PatientResponse(
                            task.getId(),
                            task.getName(),
                            task.getSurname(),
                            task.getNiNumber(),
                            new AddressResponse(
                                    task.getAddress().getStreetName(),
                                    task.getAddress().getStreetNumber(),
                                    task.getAddress().getFloor(),
                                    task.getAddress().getDepartment()
                            )
                    )
            );
        }
         
        return patientResponses;
    }

    @Override
    public PatientResponse selectPatientByID(Long id) {
        if (!patientRepository.existsById(id))
            throw new CustomNotFoundException("Patient id [" + id + "] not found");

        Optional<Task> optionalPatient = patientRepository.findById(id);

        if(optionalPatient.isPresent()) {
            Task task = optionalPatient.get();
            Address address = task.getAddress();

            return new PatientResponse(
                    task.getId(),
                    task.getName(),
                    task.getSurname(),
                    task.getNiNumber(),
                    new AddressResponse(
                            address.getStreetName(),
                            address.getStreetNumber(),
                            address.getFloor(),
                            address.getDepartment()
                            )
                    );
        }

        return null;
    }

    @Override
    public void updatePatientByID(Long id, UpdatePatientRequest updatePatientRequest) {
        if (!patientRepository.existsById(id))
            throw new CustomNotFoundException("Patient id [" + id + "] not found");

        Optional<Task> optionalPatient = patientRepository.findById(id);

        if (optionalPatient.isPresent()) {
            Task task = optionalPatient.get();
            LOGGER.info("Request to update patient id [" + id + "]");

            Address address = task.getAddress();


            task.setName(updatePatientRequest.name());
            task.setSurname(updatePatientRequest.surname());

            address.setStreetName(updatePatientRequest.address().getStreetName());
            address.setStreetNumber(updatePatientRequest.address().getStreetNumber());
            address.setFloor(updatePatientRequest.address().getFloor());
            address.setDepartment(updatePatientRequest.address().getDepartment());

            address.setTask(task);
            task.setAddress(address);

            patientRepository.save(task);
            LOGGER.info("Patient updated to [" + task.getName() + " " + task.getSurname() + "]");

        }
    }

    @Override
    public void deletePatientByID(Long id) {
        if (!patientRepository.existsById(id))
            throw new CustomNotFoundException("Patient id [" + id + "] not found");

        patientRepository.deleteById(id);
        LOGGER.info("Patient deleted from DB");
    }



    private boolean isNiNumberDuplicated(Integer niNum){
        List<Task> tasks = patientRepository.findAll();
        boolean isDuplicated = false;

        for(Task task : tasks){
            if (task.getNiNumber().equals(niNum)) {
                isDuplicated = true;
                break;
            }
        }

        return isDuplicated;
    }
}
