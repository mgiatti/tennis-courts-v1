package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(LocalDateTime startDate, LocalDateTime endDate);

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);
}