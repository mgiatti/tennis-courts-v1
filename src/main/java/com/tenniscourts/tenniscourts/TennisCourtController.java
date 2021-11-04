package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/tennis-court")
@RestController
@Api(tags="Tennis Court")
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping("/add")
    @ApiOperation("Add a new tennis court")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "Tennis Court successfully added") })
    public ResponseEntity<Void> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        return ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build();
    }

    @GetMapping("/{tennisCourtId}")
    @ApiOperation("Find a tennis court by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tennis court found"),
            @ApiResponse(code = 404, message = "Tennis court not found")
    })
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @GetMapping("/{tennisCourtId}/withSchedules")
    @ApiOperation("Find a tennis court by id with their schedules")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tennis court found"),
            @ApiResponse(code = 404, message = "Tennis court not found")
    })
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
