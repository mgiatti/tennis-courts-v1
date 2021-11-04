package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/schedule")
@RestController
@Api(tags="Scheduler")
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    @ApiOperation("Add schedule to a tennis court")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Tennis court successfully scheduled") })
    public ResponseEntity<Void> addScheduleTennisCourt(CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO).getId())).build();
    }

    @GetMapping("/bydate")
    @ApiOperation("Find a tennis court schedule by date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Schedule found"),
            @ApiResponse(code = 404, message = "Schedule not found")
    })
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(FindScheduleRequestDTO findScheduleRequestDTO) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(findScheduleRequestDTO));
    }

    @GetMapping("/availableSlots")
    @ApiOperation("Find time slots available in a schedule date")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Schedule found"),
            @ApiResponse(code = 404, message = "Schedule not found")
    })
    public ResponseEntity<List<ScheduleDTO>> findScheduleWithAvailableSlots(LocalDate date) {
        return ResponseEntity.ok(scheduleService.findScheduleWithAvailableSlots(date));
    }

    @GetMapping("/{scheduleId}")
    @ApiOperation("Find a schedule by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Schedule found"),
            @ApiResponse(code = 404, message = "Schedule not found")
    })
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
