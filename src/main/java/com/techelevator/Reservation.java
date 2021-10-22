package com.techelevator;

import java.time.LocalDate;

public class Reservation {

    private Long reservationId;
    private Long spaceId;
    private int numberOfAttendees;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reservedFor;

    public Reservation(Long reservationId, Long spaceId, int numberOfAttendees, LocalDate startDate, LocalDate endDate, String reservedFor) {
        this.reservationId = reservationId;
        this.spaceId = spaceId;
        this.numberOfAttendees = numberOfAttendees;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reservedFor = reservedFor;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public int getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getReservedFor() {
        return reservedFor;
    }
}
