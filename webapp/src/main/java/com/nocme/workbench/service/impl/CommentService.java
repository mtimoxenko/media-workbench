package com.nocme.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nocme.workbench.dto.comment.CreateCommentRequest;
import com.nocme.workbench.dto.comment.UpdateCommentRequest;
import com.nocme.workbench.dto.comment.CommentResponse;
import com.nocme.workbench.model.Comment;
import com.nocme.workbench.model.Task;
import com.nocme.workbench.model.User;
import com.nocme.workbench.repository.ICommentRepository;
import com.nocme.workbench.repository.ITaskRepository;
import com.nocme.workbench.repository.IUserRepository;
import com.nocme.workbench.service.ICommentService;
import com.nocme.workbench.utils.exceptions.CustomNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService {

    private static final Logger LOGGER = Logger.getLogger(CommentService.class);

    private final ICommentRepository commentRepository;
    private final IUserRepository userRepository;
    private final ITaskRepository taskRepository;
    private final ObjectMapper mapper;

    @Autowired
    public CommentService(ICommentRepository commentRepository,
                          IUserRepository userRepository,
                          ITaskRepository taskRepository,
                          ObjectMapper mapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.mapper = mapper;
    }

    @Override
    public void insertComment(CreateCommentRequest createCommentRequest) {
        User user = userRepository.findById(createCommentRequest.userId())
                .orElseThrow(() -> new CustomNotFoundException("User id [" + createCommentRequest.userId() + "] not found"));
        Task task = taskRepository.findById(createCommentRequest.taskId())
                .orElseThrow(() -> new CustomNotFoundException("Task id [" + createCommentRequest.taskId() + "] not found"));

        Comment comment = new Comment();
        comment.setText(createCommentRequest.text());
        comment.setTimestamp(createCommentRequest.timestamp());
        comment.setUser(user);
        comment.setTask(task);

        commentRepository.save(comment);
        LOGGER.info("New comment was added to task [" + task.getName() + "] by user [" + user.getName() + "]");
    }

    @Override
    public List<CommentResponse> selectAllComment() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        comment.getTask().getId())) // Map the task ID as well
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse selectCommentByID(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Comment id [" + id + "] not found"));
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getTask().getId()); // Include the task ID in the response
    }

    @Override
    public void updateCommentByID(UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(updateCommentRequest.id())
                .orElseThrow(() -> new CustomNotFoundException("Comment id [" + updateCommentRequest.id() + "] not found"));

        comment.setText(updateCommentRequest.text());
        comment.setTimestamp(LocalDateTime.now()); // Update timestamp to the current time
        commentRepository.save(comment);
        LOGGER.info("Comment id [" + updateCommentRequest.id() + "] updated successfully!");
    }

    @Override
    @Transactional
    public void deleteCommentByID(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CustomNotFoundException("Comment id [" + id + "] not found");
        }
        commentRepository.deleteById(id);
        LOGGER.info("Comment with id [" + id + "] successfully deleted.");
    }
}
