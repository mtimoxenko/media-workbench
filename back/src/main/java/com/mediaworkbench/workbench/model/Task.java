package com.mediaworkbench.workbench.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the Task.", example = "1", required = true)
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Name of the Task.", example = "Implement OAuth", required = true)
    private String name;

    @Column(nullable = false)
    @Schema(description = "Detailed description of the Task.", example = "Implement OAuth authentication flow for the application.", required = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Schema(description = "Current status of the Task.", example = "ACTIVE", required = true)
    private TaskStatus status = TaskStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TaskCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_status")
    private ShiftStatus shiftStatus;

    @Column(name = "creation_date")
    @Schema(description = "Date and time when the Task was created.", example = "2023-07-21T15:03:00", required = true)
    private LocalDateTime creationDate;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    }

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    @Schema(description = "The user who created the Task.", required = true)
    private User creator;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "List of user tasks associated with this Task.")
    private List<UserTask> userTasks;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "List of comments associated with this Task.")
    private List<Comment> comments;
}
