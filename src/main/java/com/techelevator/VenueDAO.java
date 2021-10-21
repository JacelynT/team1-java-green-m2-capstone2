package com.techelevator;

import java.util.List;

public interface VenueDAO {

    List<Venue> retrieveAllVenues();
    Venue retrieveVenueDetails(Long id);

}
