package com.techelevator.jdbcDAO;

import com.techelevator.Venue;
import com.techelevator.VenueDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JDBCVenueDAO implements VenueDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JDBCVenueDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Venue> retrieveAllVenues() {

        List<Venue> venueList = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM venue ORDER BY name");

        while (results.next()) {
            venueList.add(this.retrieveVenueDetails(results.getLong("id")));
        }

        return venueList;
    }

    public Venue retrieveVenueDetails(Long id) {
        List<String> categories = new ArrayList<>();

        SqlRowSet resultCategory = jdbcTemplate.queryForRowSet("SELECT category.name FROM category JOIN category_venue ON category.id = category_venue.category_id WHERE venue_id = ?",id);
        while (resultCategory.next()) {
            categories.add(resultCategory.getString("name"));
        }
        SqlRowSet resultVenue = jdbcTemplate.queryForRowSet("SELECT * FROM venue WHERE id = ?", id);

        if (resultVenue.next()) {
            return new Venue(id,resultVenue.getString("name"),resultVenue.getLong("city_id"),resultVenue.getString("description"),categories);
        }

        return null;
    }

}