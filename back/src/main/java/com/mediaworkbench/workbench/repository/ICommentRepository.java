package com.mediaworkbench.workbench.repository;

import com.mediaworkbench.workbench.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICommentRepository extends JpaRepository <Comment, Long> {
}
