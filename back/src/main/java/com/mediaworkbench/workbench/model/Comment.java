package com.mediaworkbench.workbench.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Comment.", example = "100", required = true)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Content of the Comment.", example = "This is a comment on the task.", required = true)
    private String text;

    @Column(nullable = false)
    @Schema(description = "Timestamp when the Comment was posted.", example = "2023-07-21T15:03:00", required = true)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user who posted the Comment.", required = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @Schema(description = "The task to which this Comment is associated.", required = true)
    private Task task;
}
