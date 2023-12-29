package com.mediaworkbench.workbench.repository;

import com.mediaworkbench.workbench.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

    // Finds a user by their email.
    Optional<User> findByEmail(String email);

    // Finds a user by their email, excluding a specific user ID.
    Optional<User> findByEmailAndIdNot(String email, Long id);
}
