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
    @Schema(description = "Password for the User account.", example = "password", required = true, accessMode = Schema.AccessMode.WRITE_ONLY)
    private String password;

    @Column(name = "is_admin", nullable = false)
    @Schema(description = "Flag to indicate if the User is an admin.", example = "true")
    private Boolean isAdmin = false;

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
