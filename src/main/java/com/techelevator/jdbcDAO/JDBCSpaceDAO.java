package com.techelevator.jdbcDAO;

import com.techelevator.Space;
import com.techelevator.SpaceDAO;
import com.techelevator.Venue;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
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

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal(9,2), max_occupancy FROM space WHERE venue_id = ?", venueId);

        while (results.next()) {
            Long id = results.getLong("id");
            Space space = this.retrieveSpaceDetails(id);
            spaceList.add(space);
        }

        return spaceList;
    }


    public List<Space> retrieveValidSpaces(Long venueId, LocalDate dateNeeded, int numberOfDays, int numberOfPeople) {
        List<Space> spaceList = new ArrayList<>();
        DateTimeFormatter formatFullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatDateToMonth = DateTimeFormatter.ofPattern("MM");
        LocalDate endDateNeeded = dateNeeded.plusDays(numberOfDays - 1);



        String sql =
        "SELECT DISTINCT(space.id), space.venue_id, space.name, space.is_accessible, space.open_from, space.open_to, space.daily_rate::decimal(9,2), space.max_occupancy " +
        "FROM space " +
        "LEFT JOIN reservation ON reservation.space_id = space.id " +
        "WHERE space.venue_id = ? " +      // ? = venueId
        "AND (space.open_from <= ? OR space.open_from IS NULL) " +      // ? = month of dateNeeded
        "AND (space.open_to >= ? OR space.open_from IS NULL) " +        // ? = month of (dateNeeded + numberOfDays)
        "AND space.max_occupancy >= ? " +  // ? = numberOfPeople
        "AND (((?::DATE < reservation.start_date OR reservation.start_date IS NULL) AND (?::DATE < reservation.start_date OR reservation.start_date IS NULL)) " + // ?, ? = dateNeeded
        "OR ((?::DATE > reservation.end_date OR reservation.end_date IS NULL) AND (?::DATE > reservation.end_date OR reservation.end_date IS NULL)))";   // ?, ? = dateNeeded + numberOfDays

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,
                venueId,
                Integer.parseInt(dateNeeded.format(formatDateToMonth)),
                Integer.parseInt(endDateNeeded.format(formatDateToMonth)),
                numberOfPeople,
                dateNeeded.format(formatFullDate),
                endDateNeeded.format(formatFullDate),
                dateNeeded.format(formatFullDate),
                endDateNeeded.format(formatFullDate)
        );

        while (results.next()) {
            spaceList.add(this.retrieveSpaceDetails(results.getLong("id")));
        }

        return spaceList;
    }


    public Space retrieveSpaceDetails(Long spaceId) {

        String sql1 = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal(9,2), max_occupancy FROM space WHERE id = ? AND open_from IS NULL";
        SqlRowSet result1 = jdbcTemplate.queryForRowSet(sql1, spaceId);
            if (result1.next()) {
                return new Space(
                        result1.getLong("id"),
                        result1.getLong("venue_id"),
                        result1.getString("name"),
                        result1.getBoolean("is_accessible"),
                        0,
                        0,
                        result1.getBigDecimal("daily_rate"),
                        result1.getInt("max_occupancy")
                );
            }

        String sql2 = "SELECT id, venue_id, name, is_accessible, open_from, open_to, daily_rate::decimal(9,2), max_occupancy FROM space WHERE id = ? AND open_from IS NOT NULL";
        SqlRowSet result2 = jdbcTemplate.queryForRowSet(sql2, spaceId);
        if (result2.next()) {
            return new Space(
                    result2.getLong("id"),
                    result2.getLong("venue_id"),
                    result2.getString("name"),
                    result2.getBoolean("is_accessible"),
                    result2.getInt("open_from"),
                    result2.getInt("open_to"),
                    result2.getBigDecimal("daily_rate"),
                    result2.getInt("max_occupancy")
            );
        }

        return null;

    }

}
