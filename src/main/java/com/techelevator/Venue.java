package com.techelevator;

import java.util.List;

public class Venue {

    private Long id;
    private String name;
    private Long cityId;
    private String description;
    private List<String> category;

    public Venue (Long id, String name, Long cityId, String description, List<String> category) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.description = description;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getCityId() {
        return cityId;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getCategory() {
        return category;
    }
}
