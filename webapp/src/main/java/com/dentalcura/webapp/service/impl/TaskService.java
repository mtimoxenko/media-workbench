package com.dentalcura.webapp.service.impl;



import com.dentalcura.webapp.dto.task.CreateTaskRequest;
import com.dentalcura.webapp.dto.task.UpdateTaskRequest;
import com.dentalcura.webapp.dto.task.TaskResponse;

import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.model.Task;
import com.dentalcura.webapp.model.UserTask;
import com.dentalcura.webapp.repository.ITaskRepository;
import com.dentalcura.webapp.service.ITaskService;
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
public class TaskService implements ITaskService {

    private final static Logger LOGGER = Logger.getLogger(TaskService.class);

    @Autowired
    private ITaskRepository taskRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertTask(CreateTaskRequest createTaskRequest) {
//        if (isLicenseNumberDuplicated(createTaskRequest.licenseNumber())) {
//            throw new DuplicateNiNumberException("License number [" + createTaskRequest.licenseNumber() + "] is already in use.");
//        }
//
//        Comment comment = mapper.convertValue(createTaskRequest, Comment.class);
//        taskRepository.save(comment);
        LOGGER.info("New comment was registered [ ]");
    }

    @Override
    public List<TaskResponse> selectAllTask() {
        List<Task> comments = taskRepository.findAll();

//        List<TaskResponse> taskResponses = new ArrayList<>();
//
//
//        for(Comment comment : comments){
//            List<AppointmentResponseToTask> appointmentResponses = new ArrayList<>();
//            for(UserTask userTask : comment.getAppointments()){
//                appointmentResponses.add(
//                        new AppointmentResponseToTask(
//                                userTask.getId(),
//                                userTask.getDate(),
//                                new PatientResponseToTask(
//                                        userTask.getTask().getName(),
//                                        userTask.getTask().getSurname()
//                                )
//                        )
//                );
//            }
//            taskResponses.add(
//                    new TaskResponse(
//                            comment.getId(),
//                            comment.getName(),
//                            comment.getSurname(),
//                            comment.getLicenseNumber(),
//                            appointmentResponses
//                            ));
//
//        }
         
//        return taskResponses;
        return null;
    }

    @Override
    public TaskResponse selectTaskByID(Long id) {
        if (!taskRepository.existsById(id))
            throw new CustomNotFoundException("Comment id [" + id + "] not found");

        Optional<Task> optionalTask = taskRepository.findById(id);


        if(optionalTask.isPresent()) {
            Task comment = optionalTask.get();
//            List<AppointmentResponseToTask> appointmentResponses = new ArrayList<>();
//
//            for(UserTask userTask : comment.getAppointments()){
//
//
//                appointmentResponses.add(
//                        new AppointmentResponseToTask(
//                                userTask.getId(),
//                                userTask.getDate(),
//                                new PatientResponseToTask(
//                                        userTask.getTask().getName(),
//                                        userTask.getTask().getSurname()
//                                )
//                        )
//                );
//            }
//
//            return new TaskResponse(
//                    comment.getId(),
//                    comment.getName(),
//                    comment.getSurname(),
//                    comment.getLicenseNumber(),
//                    appointmentResponses
//            );

        }
            return null;
    }

    @Override
    public void updateTaskByID(Long id, UpdateTaskRequest updateTaskRequest) {
        if (!taskRepository.existsById(id))
            throw new CustomNotFoundException("Comment id [" + id + "] not found");

        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
//            Task comment = optionalTask.get();
//            LOGGER.info("Request to update comment id [" + id + "]");
//
//            comment.setName(updateTaskRequest.name());
//            comment.setSurname(updateTaskRequest.surname());
//
//            taskRepository.save(comment);
            LOGGER.info("Comment updated to []");
        }
    }

    @Override
    public void deleteTaskByID(Long id) {
        if (!taskRepository.existsById(id))
            throw new CustomNotFoundException("Comment id [" + id + "] not found");

        taskRepository.deleteById(id);
        LOGGER.info("Comment deleted from DB");
    }


    private boolean isLicenseNumberDuplicated(Integer licNum){
        List<Task> comments = taskRepository.findAll();

//        for(Comment comment : comments){
//            if (comment.getLicenseNumber().equals(licNum)) {
//                isDuplicated = true;
//                break;
//            }
//        }

        return false;
    }

}
