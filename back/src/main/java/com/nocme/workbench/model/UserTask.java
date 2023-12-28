package com.nocme.workbench.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_task") // Especifica el nombre de la tabla en la base de datos
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_task_completed", nullable = false)
    private Boolean isTaskCompleted;
    @Column(name = "assignment_date", nullable = false)
    private LocalDateTime assignmentDate;

    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    private User assigner;  // Changed to User entity mapping

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
