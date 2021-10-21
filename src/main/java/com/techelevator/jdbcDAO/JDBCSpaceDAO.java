package com.techelevator.jdbcDAO;

import com.techelevator.Space;
import com.techelevator.SpaceDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

public class JDBCSpaceDAO implements SpaceDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JDBCSpaceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Space> retrieveSpacesFromVenues(Long venueId) {
        return null;
    }

    public List<Space> retrieveValidSpaces(Long venueId, LocalDate dateNeeded, int numberOfDays, int numberOfPeople) {
        return null;
    }

    public Space retrieveSpaceDetails(Long SpaceId) {
        return null;
    }

}
