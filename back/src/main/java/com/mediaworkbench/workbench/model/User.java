package com.mediaworkbench.workbench.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr") // Especifica el nombre de la tabla en la base de datos
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the User.", example = "1", required = true)
    private Long id;

    @Schema(description = "First name of the User.", example = "John", required = true)
    private String name;

    @Schema(description = "Surname of the User.", example = "Doe", required = true)
    private String surname;

    @Column(nullable = false)
    @Schema(description = "Email address of the User.", example = "john.doe@example.com", required = true)
    private String email;

    @Column(nullable = false)
    @Schema(description = "Password for the User account.", example = "password123", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Schema(description = "Rol related to tasks of the user", example = "ATTENDANT", required = true)
    private UserRoleStatus role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Shift user work in", example = "EVENING", required = true)
    private ShiftStatus shift;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Flag to indicate if the User is active.", example = "true")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "List of tasks created by the User.")
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "List of tasks assigned to the User.")
    private List<UserTask> assignedTasks;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "List of comments made by the User.")
    private List<Comment> comments;

}
