package com.tenniscourts.reservations;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
public class ReservationHistoryRequestDTO {

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2021-10-01")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2021-11-04")
    private LocalDate endDate;

    @NotNull
    @ApiModelProperty(required = true, example = "READY_TO_PLAY")
    private ReservationStatus reservationStatus;
}
