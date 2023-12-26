package com.dentalcura.webapp.service.impl;


import com.dentalcura.webapp.dto.appointment.CreateAppointmentRequest;
import com.dentalcura.webapp.dto.appointment.UpdateAppointmentRequest;
import com.dentalcura.webapp.dto.appointment.AppointmentResponse;
import com.dentalcura.webapp.dto.dentist.DentistResponseToAppointment;
import com.dentalcura.webapp.dto.patient.PatientResponseToAppointment;
import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.model.UserTask;
import com.dentalcura.webapp.model.Task;
import com.dentalcura.webapp.repository.IAppointmentRepository;
import com.dentalcura.webapp.service.IAppointmentService;
import com.dentalcura.webapp.utils.exceptions.CustomNotFoundException;
import com.dentalcura.webapp.utils.exceptions.DuplicateAppointmentException;
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
public class AppointmentService implements IAppointmentService {

    private final static Logger LOGGER = Logger.getLogger(DentistService.class);

    @Autowired
    private IAppointmentRepository appointmentRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertAppointment(CreateAppointmentRequest createAppointmentRequest) {
        if (isAppointmentDuplicated(
                createAppointmentRequest.date(),
                createAppointmentRequest.task().getId(),
                createAppointmentRequest.comment().getId()))
        {throw new DuplicateAppointmentException("UserTask already exists.");}

        UserTask userTask = mapper.convertValue(createAppointmentRequest, UserTask.class);
        appointmentRepository.save(userTask);
        LOGGER.info("New userTask was registered [" +createAppointmentRequest.date()+ "]");
    }

    @Override
    public List<AppointmentResponse> selectAllAppointment() {
        List<UserTask> userTasks = appointmentRepository.findAll();
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();

        for(UserTask userTask : userTasks){
            appointmentResponses.add(
                    new AppointmentResponse(
                        userTask.getId(),
                        userTask.getDate(),
                        new PatientResponseToAppointment(
                                userTask.getTask().getName(),
                                userTask.getTask().getSurname()
                        ),
                        new DentistResponseToAppointment(
                                userTask.getDentist().getName(),
                                userTask.getDentist().getSurname()
                        )));
        }
         
        return appointmentResponses;
    }

    @Override
    public AppointmentResponse selectAppointmentByID(Long id) {
        if (!appointmentRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");

        Optional<UserTask> optionalAppointment = appointmentRepository.findById(id);

        if (optionalAppointment.isPresent()) {
            UserTask userTask = optionalAppointment.get();
            Task task = userTask.getTask();
            Comment comment = userTask.getDentist();

            return new AppointmentResponse(
                    userTask.getId(),
                    userTask.getDate(),
                    new PatientResponseToAppointment(
                            task.getName(),
                            task.getSurname()
                    ),
                    new DentistResponseToAppointment(
                            comment.getName(),
                            comment.getSurname()
                    )
            );

        }

        return null;
    }

    @Override
    public void updateAppointmentByID(Long id, UpdateAppointmentRequest updateAppointmentRequest) {
        if (!appointmentRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");


        Optional<UserTask> optionalAppointment = appointmentRepository.findById(id);

        if(optionalAppointment.isPresent()) {

            UserTask userTask = optionalAppointment.get();
            LOGGER.info("Request to update userTask id [" + id + "]");

            if (isAppointmentDuplicated(
                    updateAppointmentRequest.date(),
                    userTask.getTask().getId(),
                    userTask.getDentist().getId()))
            {throw new DuplicateAppointmentException("UserTask already exists.");}
            else {
                userTask.setDate(updateAppointmentRequest.date());
                appointmentRepository.save(userTask);
                LOGGER.info("UserTask updated to [" + userTask.getDate() + "]");
            }
        }

    }

    @Override
    public void deleteAppointmentByID(Long id) {
        if (!appointmentRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");

        appointmentRepository.deleteById(id);
        LOGGER.info("UserTask deleted from DB");
    }

    private boolean isAppointmentDuplicated(String date, Long patientId, Long dentistId){
        List<UserTask> userTasks = appointmentRepository.findAll();
        boolean isDuplicated = false;

        for(UserTask userTask : userTasks){
            if (
                    userTask.getDate().equals(date) &&
                    userTask.getDentist().getId().equals(dentistId) &&
                    userTask.getTask().getId().equals(patientId)
            ) {
                isDuplicated = true;
                break;
            }
        }

        return isDuplicated;
    }

}
