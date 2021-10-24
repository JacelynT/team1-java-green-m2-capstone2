package com.techelevator;

import java.time.LocalDate;

public interface ReservationDAO {

    Reservation addReservation(Long spaceId, LocalDate date, int numberOfDays, int attendance, String reservationFor);

}
