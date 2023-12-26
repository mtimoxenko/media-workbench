package com.dentalcura.webapp.service.impl;


import com.dentalcura.webapp.dto.appointment.AppointmentResponseToDentist;
import com.dentalcura.webapp.dto.dentist.CreateDentistRequest;
import com.dentalcura.webapp.dto.dentist.UpdateDentistRequest;
import com.dentalcura.webapp.dto.dentist.DentistResponse;
import com.dentalcura.webapp.dto.patient.PatientResponseToDentist;
import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.model.UserTask;
import com.dentalcura.webapp.repository.ITaskRepository;
import com.dentalcura.webapp.service.IDentistService;
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
public class DentistService implements IDentistService {

    private final static Logger LOGGER = Logger.getLogger(DentistService.class);

    @Autowired
    private ITaskRepository dentistRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertDentist(CreateDentistRequest createDentistRequest) {
        if (isLicenseNumberDuplicated(createDentistRequest.licenseNumber())) {
            throw new DuplicateNiNumberException("License number [" + createDentistRequest.licenseNumber() + "] is already in use.");
        }

        Comment comment = mapper.convertValue(createDentistRequest, Comment.class);
        dentistRepository.save(comment);
        LOGGER.info("New comment was registered [" + comment.getName() + " " + comment.getSurname() + "]");
    }

    @Override
    public List<DentistResponse> selectAllDentist() {
        List<Comment> comments = dentistRepository.findAll();

        List<DentistResponse> dentistResponses = new ArrayList<>();


        for(Comment comment : comments){
            List<AppointmentResponseToDentist> appointmentResponses = new ArrayList<>();
            for(UserTask userTask : comment.getAppointments()){
                appointmentResponses.add(
                        new AppointmentResponseToDentist(
                                userTask.getId(),
                                userTask.getDate(),
                                new PatientResponseToDentist(
                                        userTask.getTask().getName(),
                                        userTask.getTask().getSurname()
                                )
                        )
                );
            }
            dentistResponses.add(
                    new DentistResponse(
                            comment.getId(),
                            comment.getName(),
                            comment.getSurname(),
                            comment.getLicenseNumber(),
                            appointmentResponses
                            ));

        }
         
        return dentistResponses;
    }

    @Override
    public DentistResponse selectDentistByID(Long id) {
        if (!dentistRepository.existsById(id))
            throw new CustomNotFoundException("Comment id [" + id + "] not found");

        Optional<Comment> optionalDentist = dentistRepository.findById(id);


        if(optionalDentist.isPresent()) {
            Comment comment = optionalDentist.get();
            List<AppointmentResponseToDentist> appointmentResponses = new ArrayList<>();

            for(UserTask userTask : comment.getAppointments()){


                appointmentResponses.add(
                        new AppointmentResponseToDentist(
                                userTask.getId(),
                                userTask.getDate(),
                                new PatientResponseToDentist(
                                        userTask.getTask().getName(),
                                        userTask.getTask().getSurname()
                                )
                        )
                );
            }

            return new DentistResponse(
                    comment.getId(),
                    comment.getName(),
                    comment.getSurname(),
                    comment.getLicenseNumber(),
                    appointmentResponses
            );

        }
            return null;
    }

    @Override
    public void updateDentistByID(Long id, UpdateDentistRequest updateDentistRequest) {
        if (!dentistRepository.existsById(id))
            throw new CustomNotFoundException("Comment id [" + id + "] not found");

        Optional<Comment> optionalDentist = dentistRepository.findById(id);

        if (optionalDentist.isPresent()) {
            Comment comment = optionalDentist.get();
            LOGGER.info("Request to update comment id [" + id + "]");

            comment.setName(updateDentistRequest.name());
            comment.setSurname(updateDentistRequest.surname());

            dentistRepository.save(comment);
            LOGGER.info("Comment updated to [" + comment.getName() + " " + comment.getSurname() + "]");
        }
    }

    @Override
    public void deleteDentistByID(Long id) {
        if (!dentistRepository.existsById(id))
            throw new CustomNotFoundException("Comment id [" + id + "] not found");

        dentistRepository.deleteById(id);
        LOGGER.info("Comment deleted from DB");
    }


    private boolean isLicenseNumberDuplicated(Integer licNum){
        List<Comment> comments = dentistRepository.findAll();
        boolean isDuplicated = false;

        for(Comment comment : comments){
            if (comment.getLicenseNumber().equals(licNum)) {
                isDuplicated = true;
                break;
            }
        }

        return isDuplicated;
    }

}
