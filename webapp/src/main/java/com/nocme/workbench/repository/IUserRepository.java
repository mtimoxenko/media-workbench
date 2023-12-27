package com.nocme.workbench.repository;

import com.nocme.workbench.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository <User, Long> {
}
