package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.AlreadyExistsEntityException;
import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.Guest;
import com.tenniscourts.guests.GuestRepository;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    private final GuestRepository guestRepository;

    private final ScheduleRepository scheduleRepository;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        Guest guest = guestRepository.findById(createReservationRequestDTO.getGuestId()).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Guest not found.");
        });

        Schedule schedule = scheduleRepository.findById(createReservationRequestDTO.getScheduleId()).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Schedule not found.");
        });

        List<Reservation> reservations = reservationRepository.findBySchedule_Id(schedule.getId());
        reservations.forEach((reservation -> {
            if(ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())){
                throw new AlreadyExistsEntityException(String.format( "%s tennis court already reserved for the time %s specified",
                        schedule.getTennisCourt().getName(),schedule.getStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))));
            }
        }));

        Reservation reservation = Reservation.builder()
                .guest(guest)
                .schedule(schedule)
                .value(BigDecimal.valueOf(10))
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .build();
        return reservationMapper.map(reservationRepository.saveAndFlush(reservation));
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public List<ReservationDTO> reservationHistory(ReservationHistoryRequestDTO reservationHistoryRequestDTO){
        LocalDateTime startDate = LocalDateTime.of(reservationHistoryRequestDTO.getStartDate(), LocalTime.of(0, 0));
        LocalDateTime endDate = LocalDateTime.of(reservationHistoryRequestDTO.getEndDate(), LocalTime.of(23, 59));
        return reservationMapper.map(reservationRepository.findByReservationStatusAndSchedule_StartDateTimeGreaterThanEqualAndSchedule_EndDateTimeLessThanEqual(reservationHistoryRequestDTO.getReservationStatus(), startDate, endDate));
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    /**
     *  As a Tennis Court Admin, I want to keep 25% of the reservation fee
     *  if the User cancels or reschedules between 12:00 and 23:59 hours in advance,
     *  50% between 2:00 and 11:59 in advance, and 75% between 0:01 and 2:00 in advance
     *
     */
    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());
        long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        }

        if (hours >= 12 && hours <= 23) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.75));
        }

        if (hours >= 2 && hours <= 11) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.50));
        }

        if (hours >= 0 && hours < 2) {
            return reservation.getValue().multiply(BigDecimal.valueOf(0.25));
        }

        return BigDecimal.ZERO;
    }

    @Transactional(rollbackOn = Exception.class)
    public ReservationDTO rescheduleReservation(RescheduleReservationRequestDTO rescheduleReservationRequestDTO) {
        Reservation previousReservation = reservationMapper.map(findReservation(rescheduleReservationRequestDTO.getReservationId()));
        if (rescheduleReservationRequestDTO.getScheduleId().equals(previousReservation.getSchedule().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }

        previousReservation = cancel(rescheduleReservationRequestDTO.getReservationId());
        previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
        reservationRepository.save(previousReservation);

        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(rescheduleReservationRequestDTO.getScheduleId())
                .build());
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        return newReservation;
    }


}
