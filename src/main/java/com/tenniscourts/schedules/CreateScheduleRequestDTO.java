package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleRequestDTO {

    @NotNull
    @ApiModelProperty(required = true, example = "1")
    private Long tennisCourtId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @ApiModelProperty(required = true, example = "2021-11-01T10:00")
    private LocalDateTime startDateTime;

}
