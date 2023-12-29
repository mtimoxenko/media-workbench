package com.mediaworkbench.workbench.repository;

import com.mediaworkbench.workbench.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITaskRepository extends JpaRepository <Task, Long> {
}
