package com.dentalcura.webapp.service.impl;

import com.dentalcura.webapp.dto.dentist.CreateDentistRequest;
import com.dentalcura.webapp.dto.dentist.UpdateDentistRequest;
import com.dentalcura.webapp.model.Comment;
import com.dentalcura.webapp.repository.ITaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    ITaskRepository dentistRepository;

    @Autowired
    ObjectMapper mapper;

    CommentServiceTest() {
    }

    @Test
    void insertDentist() {

        CreateDentistRequest createDentistRequest = new CreateDentistRequest(
                "testName",
                "testSurname",
                1111
        );

        Comment comment = mapper.convertValue(createDentistRequest, Comment.class);

        dentistRepository.save(comment);

        List<Comment> comments = dentistRepository.findAll();

        boolean found = false;

        for (Comment findComment : comments) {
            if (comments.getLicenseNumber().equals(findComment.getLicenseNumber())) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void updateDentistByID() {

        CreateDentistRequest createDentistRequest = new CreateDentistRequest(
                "testName",
                "testSurname",
                1111
        );

        Comment comment = mapper.convertValue(createDentistRequest, Comment.class);
        dentistRepository.save(comment);

        UpdateDentistRequest updateDentistRequest = new UpdateDentistRequest(
                "testNameUpdate",
                "testSurnameUpdate"
        );

        Comment commentUpdate = mapper.convertValue(updateDentistRequest, Comment.class);
        commentUpdate.setId(1L);
        dentistRepository.save(commentUpdate);

        Optional<Comment> optionalDentist = dentistRepository.findById(1L);
        Comment newComment = null;

        if(optionalDentist.isPresent())
            newComment = optionalDentist.get();


        assert newComment != null;
        assertEquals(commentUpdate.getLicenseNumber(), newComment.getLicenseNumber());
    }

    @Test
    void deleteDentistByID() {

        CreateDentistRequest createDentistRequest = new CreateDentistRequest(
                "testName",
                "testSurname",
                2222
        );

        Comment commentToDelete = mapper.convertValue(createDentistRequest, Comment.class);
        dentistRepository.save(commentToDelete);

        dentistRepository.deleteById(4L);

        List<Comment> comments = dentistRepository.findAll();

        boolean found = false;

        for (Comment findComment : comments) {
            if (findComment.equals(commentToDelete)) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }
}