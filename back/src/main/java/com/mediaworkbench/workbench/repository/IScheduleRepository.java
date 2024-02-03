package com.mediaworkbench.workbench.repository;

import com.mediaworkbench.workbench.model.Schedule;
import com.mediaworkbench.workbench.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByUserAndDate(User user, LocalDate date);

}
