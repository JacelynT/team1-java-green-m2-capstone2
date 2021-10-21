package com.techelevator;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JDBCVenueDAOIntegrationTest extends DAOIntegrationTest{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Before
    private void setUp() {
         dataSource = getDataSource();
         jdbcTemplate = new JdbcTemplate(dataSource);
         jdbcTemplate.update("INSERT INTO state (abbreviation, name) VALUES (?, ?)", "DU", "DummyState");
         jdbcTemplate.update("INSERT INTO city (name, state_abbreviation) VALUES (?, ?)",  "DummyCity", "DU");



         // *********************************************************
         //TODO CATEGORIES
        // ************************************************************




    }

    @Test
    public void test_retrieve_all_venues() {
        //no parameters, returns List<Venue>
        Venue testVenue = createTestVenue();

    }



    private Venue createTestVenue(String name, int cityId, String description){
        String sql = "INSERT INTO venue (name, city_id, description) VALUES (?,?,?)";
        jdbcTemplate.update(sql, );
        return new Venue(name, cityId, description);
    }


    private boolean checkIfVenueExists(List<Venue> venueList, Venue testVenue) {
        for (int i = 0; i<venueList.size(); i++) {
            if (testVenue.getId().equals(venueList.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

}
