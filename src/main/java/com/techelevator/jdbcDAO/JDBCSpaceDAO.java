package com.techelevator.jdbcDAO;

import com.techelevator.Space;
import com.techelevator.SpaceDAO;
import com.techelevator.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCSpaceDAO implements SpaceDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JDBCSpaceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Space> retrieveSpacesFromVenues(Long venueId) {
        List<Space> spaceList = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM space WHERE venue_id = ?", venueId);

        while (results.next()) {
            Long id = results.getLong("venue_id");
            Space space = this.retrieveSpaceDetails(id);
            spaceList.add(space);
        }

        return spaceList;
    }


    public List<Space> retrieveValidSpaces(Long venueId, LocalDate dateNeeded, int numberOfDays, int numberOfPeople) {
        return null;
    }


    public Space retrieveSpaceDetails(Long spaceId) {

        String sql = "SELECT * FROM space WHERE id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, spaceId);

            if (result.next()) {
                return new Space(
                    result.getLong("id"),
                    result.getLong("venue_id"),
                    result.getString("name"),
                    result.getBoolean("is_accessible"),
                    result.getInt("open_from"),
                    result.getInt("open_to"),
                    result.getBigDecimal("daily_rate"),
                    result.getInt("max_occupancy")
                );
            }

        return null;

    }

}
