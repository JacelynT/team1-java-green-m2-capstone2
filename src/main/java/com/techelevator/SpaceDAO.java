package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface SpaceDAO {

    List<Space> retrieveSpacesFromVenues(Long venueId);

    List<Space> retrieveValidSpaces(Long venueId, LocalDate dateNeeded, int numberOfDays, int numberOfPeople);

    Space retrieveSpaceDetails(Long SpaceId);

}
