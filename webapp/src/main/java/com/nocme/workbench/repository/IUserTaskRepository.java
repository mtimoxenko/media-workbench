package com.nocme.workbench.repository;

import com.nocme.workbench.model.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserTaskRepository extends JpaRepository <UserTask, Long> {
}
