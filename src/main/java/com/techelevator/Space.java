package com.techelevator;

import java.math.BigDecimal;

public class Space {

    private Long id;
    private Long venueId;
    private String name;
    private boolean isAccessible;
    private int openFrom;
    private int openTo;
    private BigDecimal dailyRate;
    private int maxOccupancy;

    public Space(Long id, Long venueId, String name, boolean isAccessible, int openFrom, int openTo, BigDecimal dailyRate, int maxOccupancy) {
        this.id = id;
        this.venueId = venueId;
        this.name = name;
        this.isAccessible = isAccessible;
        this.openFrom = openFrom;
        this.openTo = openTo;
        this.dailyRate = dailyRate;
        this.maxOccupancy = maxOccupancy;
    }

    public Long getId() {
        return id;
    }

    public Long getVenueId() {
        return venueId;
    }

    public String getName() {
        return name;
    }

    public boolean isAccessible() {
        return isAccessible;
    }

    public int getOpenFrom() {
        return openFrom;
    }

    public int getOpenTo() {
        return openTo;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }
}
