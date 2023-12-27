package com.dentalcura.webapp.service;

import com.dentalcura.webapp.dto.comment.CreateCommentRequest;
import com.dentalcura.webapp.dto.comment.UpdateCommentRequest;
import com.dentalcura.webapp.dto.comment.CommentResponse;

import java.util.List;


public interface ICommentService {

    void insertComment(CreateCommentRequest createCommentRequest);
    List<CommentResponse> selectAllComment();
    CommentResponse selectCommentByID(Long id);
    void updateCommentByID(Long id, UpdateCommentRequest updateCommentRequest);
    void deleteCommentByID(Long id);

}
