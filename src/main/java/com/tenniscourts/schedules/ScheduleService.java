package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.reservations.ReservationDTO;
import com.tenniscourts.reservations.ReservationHistoryRequestDTO;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.reservations.ReservationStatus;
import com.tenniscourts.tenniscourts.TennisCourt;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private final TennisCourtRepository tennisCourtRepository;

    private final ReservationService reservationService;

    public ScheduleDTO addSchedule(CreateScheduleRequestDTO createScheduleRequestDTO) {
        TennisCourt tennisCourt = tennisCourtRepository.findById(createScheduleRequestDTO.getTennisCourtId()).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found");
        });
        LocalDateTime startDateTime = createScheduleRequestDTO.getStartDateTime();

        Schedule schedule = Schedule.builder()
                .tennisCourt(tennisCourt)
                .startDateTime(startDateTime)
                .endDateTime(startDateTime.plusHours(1))
                .build();

        return scheduleMapper.map(scheduleRepository.saveAndFlush(schedule));
    }

    public List<ScheduleDTO> findSchedulesByDates(FindScheduleRequestDTO findScheduleRequestDTO) {
        if(findScheduleRequestDTO.getStartDate().isAfter(findScheduleRequestDTO.getEndDate())){
            throw new IllegalArgumentException("The end date is starting before the start date");
        }
        LocalDateTime startDate = LocalDateTime.of(findScheduleRequestDTO.getStartDate(), LocalTime.of(0, 0));
        LocalDateTime endDate = LocalDateTime.of(findScheduleRequestDTO.getEndDate(), LocalTime.of(23, 59));
        return scheduleMapper.map(scheduleRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDate, endDate));
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });
    }

    public List<ScheduleDTO> findScheduleWithAvailableSlots(LocalDate date){
        LocalDateTime startDate = LocalDateTime.of(date, LocalTime.of(0, 0));
        LocalDateTime endDate = LocalDateTime.of(date, LocalTime.of(23, 59));
        ReservationHistoryRequestDTO reservationHistoryRequestDTO = ReservationHistoryRequestDTO.builder()
                .startDate(date)
                .endDate(date)
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .build();
        List<ReservationDTO> listReservationReady = reservationService.reservationHistory(reservationHistoryRequestDTO);
        List<Schedule> listSchedule = scheduleRepository.findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqual(startDate, endDate);

        return scheduleMapper.map(listSchedule.stream().filter((schedule) -> {
            boolean scheduleAlreadyReserved = listReservationReady.stream().anyMatch(reservation -> reservation.getSchedule().getId() == schedule.getId());
            return !scheduleAlreadyReserved;
        }).collect(Collectors.toList()));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
