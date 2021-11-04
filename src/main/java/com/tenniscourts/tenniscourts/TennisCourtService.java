package com.tenniscourts.tenniscourts;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class TennisCourtService {

    private final TennisCourtRepository tennisCourtRepository;

    private final ScheduleRepository scheduleRepository;

    private final ScheduleService scheduleService;

    private final TennisCourtMapper tennisCourtMapper;

    @Transactional(rollbackOn = Exception.class)
    public TennisCourtDTO addTennisCourt(TennisCourtDTO tennisCourt) {
        TennisCourt tennisCourtCreated = tennisCourtRepository.saveAndFlush(tennisCourtMapper.map(tennisCourt));
        tennisCourt.getTennisCourtSchedules().forEach((scheduleDTO) -> {
            Schedule schedule = Schedule.builder()
                    .tennisCourt(tennisCourtCreated)
                    .startDateTime(scheduleDTO.getStartDateTime())
                    .endDateTime(scheduleDTO.getEndDateTime())
                    .build();

            scheduleRepository.saveAndFlush(schedule);
        });
        return tennisCourtMapper.map(tennisCourtCreated);
    }

    public TennisCourtDTO findTennisCourtById(Long id) {
        TennisCourtDTO tennisCourtDTO = tennisCourtRepository.findById(id).map(tennisCourtMapper::map).<EntityNotFoundException>orElseThrow(() -> {
            throw new EntityNotFoundException("Tennis Court not found.");
        });
        return tennisCourtDTO;
    }

    public TennisCourtDTO findTennisCourtWithSchedulesById(Long tennisCourtId) {
        TennisCourtDTO tennisCourtDTO = findTennisCourtById(tennisCourtId);
        tennisCourtDTO.setTennisCourtSchedules(scheduleService.findSchedulesByTennisCourtId(tennisCourtId));
        return tennisCourtDTO;
    }
}
