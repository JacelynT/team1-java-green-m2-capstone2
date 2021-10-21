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

        String sql = "";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "");

        return venueList;
    }


    public Venue retrieveVenueDetails(Long id) {
        return null;
    }
}
