package com.nocme.workbench.controller;

import com.nocme.workbench.dto.comment.CreateCommentRequest;
import com.nocme.workbench.dto.comment.UpdateCommentRequest;
import com.nocme.workbench.dto.comment.CommentResponse;
import com.nocme.workbench.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.selectAllComment();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        CommentResponse commentResponse = commentService.selectCommentByID(id);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        commentService.insertComment(createCommentRequest);
        String message = "Comment created successfully!";
        return ResponseEntity.ok().body(message);
    }

    @PutMapping
    public ResponseEntity<String> updateComment(@RequestBody UpdateCommentRequest updateCommentRequest) {
        commentService.updateCommentByID(updateCommentRequest);
        String message = "Comment updated successfully!";
        return ResponseEntity.ok().body(message);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteCommentByID(id);
        String message = "Comment deleted successfully!";
        return ResponseEntity.ok().body(message);
    }
}
