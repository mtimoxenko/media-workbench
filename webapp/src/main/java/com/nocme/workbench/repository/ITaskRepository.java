package com.nocme.workbench.repository;

import com.nocme.workbench.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskRepository extends JpaRepository <Task, Long> {
}
