package com.tenniscourts.guests;

import java.util.List;

import com.tenniscourts.config.BaseRestController;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@AllArgsConstructor
@RequestMapping("guest")
@RestController
@Api(tags="Guest")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    @ApiOperation(value = "Add a guest")
    public ResponseEntity<Void> add(@RequestBody CreateGuestDTO guestDTO) {
        return ResponseEntity.created(locationByEntity(guestService.addGuest(guestDTO).getId())).build();
    }

    @PutMapping
    @ApiOperation(value = "Update a guest")
    public ResponseEntity<GuestDTO> update(@RequestBody GuestDTO guestDTO) {
        return ResponseEntity.ok(guestService.updateGuest(guestDTO));
    }

    @DeleteMapping("/{guestId}")
    @ApiOperation(value = "Delete a guest by id")
    public ResponseEntity<String> delete(@PathVariable Long guestId) {
        guestService.deleteGuest(guestId);
        return ResponseEntity.ok("Guest deleted.");
    }

    @GetMapping("/{guestId}")
    @ApiOperation(value = "Find a guest by id")
    public ResponseEntity<GuestDTO> findById(@PathVariable Long guestId) {
        return ResponseEntity.ok(guestService.findGuestById(guestId));
    }

    @GetMapping
    @ApiOperation(value = "Find guests by name")
    public ResponseEntity<List<GuestDTO>> findByName(
            @RequestParam(value = "search", defaultValue = "name", required = true) String name) {
        return ResponseEntity.ok(guestService.findGuestByName(name));
    }

    @GetMapping("/all")
    @ApiOperation(value = "Find all guests")
    public ResponseEntity<List<GuestDTO>> findAll() {
        return ResponseEntity.ok(guestService.findGuests());
    }
}