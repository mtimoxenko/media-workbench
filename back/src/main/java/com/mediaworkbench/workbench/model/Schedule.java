package com.mediaworkbench.workbench.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.DayOfWeek; // This is now using the java.time.DayOfWeek

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek; // Using java.time.DayOfWeek

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    @Column(name = "is_working_day", nullable = false)
    private Boolean isWorkingDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private UserRoleStatus role;
}
