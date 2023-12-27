package com.dentalcura.webapp.model;

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
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;

    @Column(name = "creation_date") // Especifica el nombre de la columna en la base de datos
    private LocalDateTime creationDate;
    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;  // Changed to User entity mapping

    @OneToMany(mappedBy = "task")
    private List<UserTask> userTasks;

}
