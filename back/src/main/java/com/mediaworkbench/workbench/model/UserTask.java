package com.mediaworkbench.workbench.model;

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
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_task_status", nullable = false)
    private UserTaskStatus userTaskStatus = UserTaskStatus.ASSIGNED; // Default to ASSIGNED


    @Column(name = "assignment_date", nullable = false)
    private LocalDateTime assignmentDate;

    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    private User assigner;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
