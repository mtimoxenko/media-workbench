package com.mediaworkbench.workbench.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaworkbench.workbench.repository.ICommentRepository;
import com.mediaworkbench.workbench.repository.ITaskRepository;
import com.mediaworkbench.workbench.repository.IUserRepository;
import com.mediaworkbench.workbench.dto.comment.CreateCommentRequest;
import com.mediaworkbench.workbench.dto.comment.UpdateCommentRequest;
import com.mediaworkbench.workbench.dto.comment.CommentResponse;
import com.mediaworkbench.workbench.model.Comment;
import com.mediaworkbench.workbench.model.Task;
import com.mediaworkbench.workbench.model.User;
import com.mediaworkbench.workbench.service.ICommentService;
import com.mediaworkbench.workbench.utils.exceptions.CustomNotFoundException;
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

    @Autowired
    private final ICommentRepository commentRepository;
    @Autowired
    private final IUserRepository userRepository;
    @Autowired
    private final ITaskRepository taskRepository;
    @Autowired
    private ObjectMapper mapper;


    @Autowired
    public CommentService(ICommentRepository commentRepository,
                          IUserRepository userRepository,
                          ITaskRepository taskRepository,
                          ObjectMapper mapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
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
        LOGGER.info("New comment was added to task [" + task.getName() + "] by user [" + user.getName() + " " + user.getSurname() + "]");
    }

    @Override
    public List<CommentResponse> selectAllComment() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(this::mapCommentToCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse selectCommentByID(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Comment id [" + id + "] not found"));
        return mapCommentToCommentResponse(comment);
    }

    private CommentResponse mapCommentToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getTimestamp(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getUser().getSurname()
        );
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
