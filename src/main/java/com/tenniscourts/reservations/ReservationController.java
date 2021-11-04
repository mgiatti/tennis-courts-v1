package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.guests.GuestDTO;
import io.swagger.annotations.*;
import jdk.jfr.SettingDefinition;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/reservation")
@RestController
@Api(tags="Reservation")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    @ApiOperation("Book a new tennis court reservation")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Tennis court successfully booked") })
    public ResponseEntity<Void> bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping
    @ApiOperation("Find past reservations by passing start date, end date and status")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Reservations found") })
    public ResponseEntity<List<ReservationDTO>>  findReservations(ReservationHistoryRequestDTO reservationHistoryRequestDTO) {
        return ResponseEntity.ok(reservationService.reservationHistory(reservationHistoryRequestDTO));
    }

    @GetMapping("/{reservationId}")
    @ApiOperation("Find a tennis court reservation by given their reservation id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Found reservation") })
    public ResponseEntity<ReservationDTO> findReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @DeleteMapping("/{reservationId}")
    @ApiOperation("Cancel a tennis court reservation by given their reservation id")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Reservation canceled successfully") })
    public ResponseEntity<ReservationDTO> cancelReservation(Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping("/{reservationId}")
    @ApiOperation("Reschedule a reservation")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Reservation reschedule successfully") })
    public ResponseEntity<ReservationDTO> rescheduleReservation(@RequestBody RescheduleReservationRequestDTO rescheduleReservationRequestDTO) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(rescheduleReservationRequestDTO));
    }
}
