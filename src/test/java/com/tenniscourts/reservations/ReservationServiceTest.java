package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().plusDays(2));
        Assert.assertEquals(new BigDecimal(10), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }

    @Test
    public void getRefundValue75PercentRefund() {
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().plusHours(23).plusMinutes(59));
        Assert.assertEquals(new BigDecimal(7.50).setScale(2), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusHours(13).plusMinutes(40));
        Assert.assertEquals(new BigDecimal(7.50).setScale(2), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusHours(12));
        Assert.assertEquals(new BigDecimal(7.50).setScale(2), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusHours(11).plusMinutes(59));
        Assert.assertNotEquals(new BigDecimal(7.50).setScale(2), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

    }

    @Test
    public void getRefundValue50PercentRefund() {
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().plusHours(10));
        Assert.assertEquals(new BigDecimal(5.0).setScale(1), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));
    }

    @Test
    public void getRefundValue25PercentRefund() {
        Schedule schedule = new Schedule();
        schedule.setStartDateTime(LocalDateTime.now().plusHours(1));
        Assert.assertEquals(new BigDecimal(2.5).setScale(2), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

        schedule.setStartDateTime(LocalDateTime.now().plusMinutes(1));
        Assert.assertEquals(new BigDecimal(2.5).setScale(2), reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()));

    }
}