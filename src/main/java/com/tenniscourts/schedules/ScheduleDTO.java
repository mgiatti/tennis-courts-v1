package com.tenniscourts.schedules;

import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleDTO {

    private Long id;

    private TennisCourtDTO tennisCourt;

    @NotNull
    private Long tennisCourtId;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(required = true, example = "2021-11-01T10:00")
    @NotNull
    private LocalDateTime startDateTime;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(required = true, example = "2021-11-01T11:00")
    private LocalDateTime endDateTime;

}
