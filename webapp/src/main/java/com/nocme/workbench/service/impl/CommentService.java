package com.dentalcura.webapp.service.impl;


import com.dentalcura.webapp.dto.comment.CreateCommentRequest;
import com.dentalcura.webapp.dto.comment.UpdateCommentRequest;
import com.dentalcura.webapp.dto.comment.CommentResponse;
import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.model.UserTask;
import com.dentalcura.webapp.model.Task;
import com.dentalcura.webapp.repository.ICommentRepository;
import com.dentalcura.webapp.service.ICommentService;
import com.dentalcura.webapp.utils.exceptions.CustomNotFoundException;
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
public class CommentService implements ICommentService {

    private final static Logger LOGGER = Logger.getLogger(TaskService.class);

    @Autowired
    private ICommentRepository commentRepository;

    @Autowired
    ObjectMapper mapper;

    @Override
    public void insertComment(CreateCommentRequest createCommentRequest) {
//        if (isCommentDuplicated(
//                createCommentRequest.date(),
//                createCommentRequest.task().getId(),
//                createCommentRequest.comment().getId()))
//        {throw new DuplicateCommentException("UserTask already exists.");}

//        UserTask userTask = mapper.convertValue(createCommentRequest, UserTask.class);
//        commentRepository.save(userTask);
        LOGGER.info("New userTask was registered []");
    }

    @Override
    public List<CommentResponse> selectAllComment() {
        List<Comment> userTasks = commentRepository.findAll();
        List<CommentResponse> commentResponses = new ArrayList<>();

        for(Comment userTask : userTasks){
//            commentResponses.add(
//                    new CommentResponse(
//                        userTask.getId(),
//                        userTask.getDate(),
//                        new PatientResponseToComment(
//                                userTask.getTask().getName(),
//                                userTask.getTask().getSurname()
//                        ),
//                        new DentistResponseToComment(
//                                userTask.getDentist().getName(),
//                                userTask.getDentist().getSurname()
//                        )));
        }
         
        return commentResponses;
    }

    @Override
    public CommentResponse selectCommentByID(Long id) {
        if (!commentRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");

        Optional<Comment> optionalComment = commentRepository.findById(id);

        if (optionalComment.isPresent()) {
//            Comment userTask = optionalComment.get();
//            Task task = userTask.getTask();
//            Comment comment = userTask.getDentist();
//
//            return new CommentResponse(
//                    userTask.getId(),
//                    userTask.getDate(),
//                    new PatientResponseToComment(
//                            task.getName(),
//                            task.getSurname()
//                    ),
//                    new DentistResponseToComment(
//                            comment.getName(),
//                            comment.getSurname()
//                    )
//            );

        }

        return null;
    }

    @Override
    public void updateCommentByID(Long id, UpdateCommentRequest updateCommentRequest) {
        if (!commentRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");


        Optional<Comment> optionalComment = commentRepository.findById(id);

        if(optionalComment.isPresent()) {

            Comment userTask = optionalComment.get();
            LOGGER.info("Request to update userTask id [" + id + "]");

//            if (isCommentDuplicated(
//                    updateCommentRequest.date(),
//                    userTask.getTask().getId(),
//                    userTask.getDentist().getId()))
//            {throw new DuplicateCommentException("UserTask already exists.");}
//            else {
//                userTask.setDate(updateCommentRequest.date());
//                commentRepository.save(userTask);
//                LOGGER.info("UserTask updated to [" + userTask.getDate() + "]");
//            }
        }

    }

    @Override
    public void deleteCommentByID(Long id) {
        if (!commentRepository.existsById(id))
            throw new CustomNotFoundException("UserTask id [" + id + "] not found");

        commentRepository.deleteById(id);
        LOGGER.info("UserTask deleted from DB");
    }

    private boolean isCommentDuplicated(String date, Long patientId, Long dentistId){
        List<Comment> userTasks = commentRepository.findAll();
        boolean isDuplicated = false;

//        for(Comment userTask : userTasks){
//            if (
//                    userTask.getDate().equals(date) &&
//                    userTask.getDentist().getId().equals(dentistId) &&
//                    userTask.getTask().getId().equals(patientId)
//            ) {
//                isDuplicated = true;
//                break;
//            }
//        }

        return isDuplicated;
    }

}
