package com.mediaworkbench.workbench.controller;

import com.mediaworkbench.workbench.dto.comment.CreateCommentRequest;
import com.mediaworkbench.workbench.dto.comment.UpdateCommentRequest;
import com.mediaworkbench.workbench.dto.comment.CommentResponse;
import com.mediaworkbench.workbench.service.ICommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comments", description = "Endpoints for comments management")
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Get All Comments", description = "Returns a list of all comments")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentResponse.class)))
    @GetMapping()
    public ResponseEntity<List<CommentResponse>> getAllComments() {
        List<CommentResponse> comments = commentService.selectAllComment();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @Operation(summary = "Get a Comment by ID", description = "Returns a single comment by ID")
    @ApiResponse(responseCode = "200", description = "Comment found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommentResponse.class)))
    @ApiResponse(responseCode = "404", description = "Comment not found")
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getComment(
            @Parameter(description = "ID of the comment to be obtained", required = true) @PathVariable Long id) {
        CommentResponse commentResponse = commentService.selectCommentByID(id);
        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @Operation(summary = "Create a Comment", description = "Creates a new comment")
    @ApiResponse(responseCode = "200", description = "Comment created successfully")
    @PostMapping
    public ResponseEntity<String> createComment(
            @Parameter(description = "Comment object to be created", required = true) @RequestBody CreateCommentRequest createCommentRequest) {
        commentService.insertComment(createCommentRequest);
        return ResponseEntity.ok("Comment created successfully!");
    }

    @Operation(summary = "Update a Comment", description = "Updates an existing comment")
    @ApiResponse(responseCode = "200", description = "Comment updated successfully")
    @PutMapping
    public ResponseEntity<String> updateComment(
            @Parameter(description = "Comment object to be updated", required = true) @RequestBody UpdateCommentRequest updateCommentRequest) {
        commentService.updateCommentByID(updateCommentRequest);
        return ResponseEntity.ok("Comment updated successfully!");
    }

    @Operation(summary = "Delete a Comment", description = "Deletes a comment by ID")
    @ApiResponse(responseCode = "200", description = "Comment deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @Parameter(description = "ID of the comment to be deleted", required = true) @PathVariable Long id) {
        commentService.deleteCommentByID(id);
        return ResponseEntity.ok("Comment deleted successfully!");
    }
}
