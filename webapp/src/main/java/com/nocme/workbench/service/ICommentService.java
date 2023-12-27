package com.nocme.workbench.service;

import com.nocme.workbench.dto.comment.CreateCommentRequest;
import com.nocme.workbench.dto.comment.UpdateCommentRequest;
import com.nocme.workbench.dto.comment.CommentResponse;

import java.util.List;


public interface ICommentService {

    void insertComment(CreateCommentRequest createCommentRequest);
    List<CommentResponse> selectAllComment();
    CommentResponse selectCommentByID(Long id);
    void updateCommentByID(Long id, UpdateCommentRequest updateCommentRequest);
    void deleteCommentByID(Long id);

}
