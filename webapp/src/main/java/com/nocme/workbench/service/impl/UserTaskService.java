package com.dentalcura.webapp.service.impl;


import com.dentalcura.webapp.dto.usertask.CreateUserTaskRequest;
import com.dentalcura.webapp.dto.usertask.UpdateUserTaskRequest;
import com.dentalcura.webapp.dto.usertask.UserTaskResponse;
import com.dentalcura.webapp.model.Task;
import com.dentalcura.webapp.model.UserTask;
import com.dentalcura.webapp.repository.IUserTaskRepository;
import com.dentalcura.webapp.service.IUserTaskService;
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
public class UserTaskService implements IUserTaskService {

    private final static Logger LOGGER = Logger.getLogger(UserTaskService.class);

    @Autowired
    private IUserTaskRepository userTaskRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertUserTask(CreateUserTaskRequest createUserTaskRequest) {
//        if (isNiNumberDuplicated(createUserTaskRequest.niNumber())) {
//            throw new DuplicateNiNumberException("NI Number [" + createUserTaskRequest.niNumber() + "] is already in use.");
//        }
//
//        Task task = mapper.convertValue(createUserTaskRequest, Task.class);
//        Address address = task.getAddress();
//
//        address.setTask(task);
//        task.setAddress(address);
//
//        userTaskRepository.save(task);
//        LOGGER.info("New userTask was registered [" + task.getName() + " " + task.getSurname() + "]");
    }

    @Override
    public List<UserTaskResponse> selectAllUserTask() {
//        List<UserTask> tasks = userTaskRepository.findAll();
//        List<UserTaskResponse> userTaskResponses = new ArrayList<>();
//
//        for(UserTask task : tasks){
//            userTaskResponses.add(
//                    new UserTaskResponse(
//                            task.getId(),
//                            task.getName(),
//                            task.getSurname(),
//                            task.getNiNumber(),
//                            new AddressResponse(
//                                    task.getAddress().getStreetName(),
//                                    task.getAddress().getStreetNumber(),
//                                    task.getAddress().getFloor(),
//                                    task.getAddress().getDepartment()
//                            )
//                    )
//            );
//        }
         
//        return userTaskResponses;
        return null;
    }

    @Override
    public UserTaskResponse selectUserTaskByID(Long id) {
//        if (!userTaskRepository.existsById(id))
//            throw new CustomNotFoundException("UserTask id [" + id + "] not found");
//
//        Optional<UserTask> optionalUserTask = userTaskRepository.findById(id);
//
//        if(optionalUserTask.isPresent()) {
//            Task task = optionalUserTask.get();
//            Address address = task.getAddress();
//
//            return new UserTaskResponse(
//                    task.getId(),
//                    task.getName(),
//                    task.getSurname(),
//                    task.getNiNumber(),
//                    new AddressResponse(
//                            address.getStreetName(),
//                            address.getStreetNumber(),
//                            address.getFloor(),
//                            address.getDepartment()
//                            )
//                    );
//        }

        return null;
    }

    @Override
    public void updateUserTaskByID(Long id, UpdateUserTaskRequest updateUserTaskRequest) {
//        if (!userTaskRepository.existsById(id))
//            throw new CustomNotFoundException("UserTask id [" + id + "] not found");
//
//        Optional<UserTask> optionalUserTask = userTaskRepository.findById(id);
//
//        if (optionalUserTask.isPresent()) {
//            Task task = optionalUserTask.get();
//            LOGGER.info("Request to update userTask id [" + id + "]");
//
//            Address address = task.getAddress();
//
//
//            task.setName(updateUserTaskRequest.name());
//            task.setSurname(updateUserTaskRequest.surname());
//
//            address.setStreetName(updateUserTaskRequest.address().getStreetName());
//            address.setStreetNumber(updateUserTaskRequest.address().getStreetNumber());
//            address.setFloor(updateUserTaskRequest.address().getFloor());
//            address.setDepartment(updateUserTaskRequest.address().getDepartment());
//
//            address.setTask(task);
//            task.setAddress(address);
//
//            userTaskRepository.save(task);
//            LOGGER.info("UserTask updated to [" + task.getName() + " " + task.getSurname() + "]");
//
//        }
    }

    @Override
    public void deleteUserTaskByID(Long id) {
        if (!userTaskRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");

        userTaskRepository.deleteById(id);
        LOGGER.info("UserTask deleted from DB");
    }



    private boolean isNiNumberDuplicated(Integer niNum){
//        List<UserTask> tasks = userTaskRepository.findAll();
//        boolean isDuplicated = false;
//
//        for(Task task : tasks){
//            if (task.getNiNumber().equals(niNum)) {
//                isDuplicated = true;
//                break;
//            }
//        }

//        return isDuplicated;
        return false;
    }
}
