package com.mediaworkbench.workbench.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Unique identifier of the Schedule.", example = "100", required = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "The user associated with this schedule.", required = true)
    private User user;

    @Column(nullable = false)
    @Schema(description = "Date of the schedule.", example = "2023-08-15", required = true)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    @Schema(description = "Day of the week for the schedule.", example = "MONDAY", required = true)
    private DayOfWeek dayOfWeek; // Using java.time.DayOfWeek

    @Column(name = "day_number", nullable = false)
    @Schema(description = "Day number of the schedule.", example = "7", required = true)
    private Integer dayNumber;

    @Column(name = "is_working_day", nullable = false)
    @Schema(description = "Indicates if it's a working day.", example = "true", required = true)
    private Boolean isWorkingDay;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    @Schema(description = "Role associated with the schedule.", example = "REPORTER", required = false)
    private UserRoleStatus role;
}
