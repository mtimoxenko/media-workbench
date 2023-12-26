package com.dentalcura.webapp.repository;

import com.dentalcura.webapp.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDentistRepository extends JpaRepository <Comment, Long> {
}
