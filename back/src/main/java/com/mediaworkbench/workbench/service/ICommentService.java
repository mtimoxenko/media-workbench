package com.mediaworkbench.workbench.service;

import com.mediaworkbench.workbench.dto.comment.CreateCommentRequest;
import com.mediaworkbench.workbench.dto.comment.UpdateCommentRequest;
import com.mediaworkbench.workbench.dto.comment.CommentResponse;

import java.util.List;


public interface ICommentService {

    void insertComment(CreateCommentRequest createCommentRequest);
    List<CommentResponse> selectAllComment();
    CommentResponse selectCommentByID(Long id);
    void updateCommentByID(UpdateCommentRequest updateCommentRequest);
    void deleteCommentByID(Long id);

}
