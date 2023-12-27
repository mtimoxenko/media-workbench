package com.nocme.workbench.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;
    private String name;
    private String surname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @OneToMany(mappedBy = "creator")
    @JsonIgnore /*
    handle potential infinite recursion problems
    that can occur during JSON serialization/deserialization
    when you have bi-directional relationships in your entity classes.
    */
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UserTask> assignedTasks;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Comment> comments;

}
