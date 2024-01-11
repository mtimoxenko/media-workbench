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
@Table(name = "user_task")
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the UserTask.", example = "101", required = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_task_status", nullable = false)
    @Schema(description = "The status of the task as it relates to the user.", example = "ASSIGNED", required = true)
    private UserTaskStatus userTaskStatus = UserTaskStatus.ASSIGNED;

    @Column(name = "assignment_date", nullable = false)
    @Schema(description = "The date and time when the task was assigned to the user.", example = "2023-07-21T15:03:00", required = true)
    private LocalDateTime assignmentDate;

    @PrePersist
    protected void onCreate() {
        assignmentDate = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    @Schema(description = "The user who assigned the task.", required = true)
    private User assigner;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user to whom the task is assigned.", required = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @Schema(description = "The task that is assigned to the user.", required = true)
    private Task task;
}
