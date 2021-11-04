package com.tenniscourts.schedules;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FindScheduleRequestDTO {

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2021-11-01")
    private LocalDate startDate;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(required = true, example = "2021-11-02")
    private LocalDate endDate;

}
