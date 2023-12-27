package com.nocme.workbench.repository;

import com.nocme.workbench.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository <Comment, Long> {
}
