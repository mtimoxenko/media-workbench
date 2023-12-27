package com.nocme.workbench.controller;


import com.nocme.workbench.dto.comment.CreateCommentRequest;
import com.nocme.workbench.dto.comment.CommentResponse;
import com.nocme.workbench.dto.comment.UpdateCommentRequest;
import com.nocme.workbench.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getCommentAll() {
        return new ResponseEntity<>(commentService.selectAllComment(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long id) {
        CommentResponse commentResponse = commentService.selectCommentByID(id);

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("comment_created", "true");  // Adding a custom header
        String message = "UserTask created successfully!";

        commentService.insertComment(createCommentRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateComment(@PathVariable Long id, @RequestBody UpdateCommentRequest updateCommentRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("comment_updated", "true");  // Adding a custom header
        String message = "UserTask updated successfully!";

        commentService.updateCommentByID(id, updateCommentRequest);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add("comment_deleted", "true");  // Adding a custom header
        String message = "UserTask deleted successfully!";

        commentService.deleteCommentByID(id);
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(message);
    }

}
