package com.techelevator;

import com.techelevator.jdbcDAO.JDBCReservationDAO;
import com.techelevator.jdbcDAO.JDBCSpaceDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JDBCReservationDAOIntegrationTest extends DAOIntegrationTest{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private Long dummyCityId;
    private Space dummySpace;
    private Venue dummyVenue;
    private JDBCReservationDAO dao;

    @Before
    public void setUp() {
        dataSource = getDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JDBCReservationDAO(dataSource);

        jdbcTemplate.update("INSERT INTO state (abbreviation, name) VALUES (?, ?)", "DU", "DummyState");
        jdbcTemplate.update("INSERT INTO city (name, state_abbreviation) VALUES (?, ?)", "DummyCity", "DU");
        SqlRowSet resultCity = jdbcTemplate.queryForRowSet("SELECT * FROM city WHERE name = ?", "DummyCity");
        if (resultCity.next()) {
            dummyCityId = resultCity.getLong("id");
        }
        String sql = "INSERT INTO venue (name, city_id, description) VALUES (?,?,?)";
        jdbcTemplate.update(sql, "Dummy Venue", dummyCityId, "Yep, still 100% a test.");

        SqlRowSet venueResults = jdbcTemplate.queryForRowSet("SELECT * FROM venue WHERE name = ?", "Dummy Venue");
        Long VenueId = (long) 0;
        if (venueResults.next()) {
            VenueId = venueResults.getLong("id");
        }
        List<String> dummyCategories = new ArrayList<>();
        dummyCategories.add("Dummy Category");
        dummyVenue = new Venue(VenueId, venueResults.getString("name"), dummyCityId, venueResults.getString("description"), dummyCategories);

        BigDecimal dailyRate = new BigDecimal(8.75);
        jdbcTemplate.update("INSERT INTO space (venue_id, name, is_accessible, daily_rate, max_occupancy) VALUES (?,?,?,?,?)",
                dummyVenue.getId(), "Dummy Space", true, dailyRate, 200);

        SqlRowSet spaceResults = jdbcTemplate.queryForRowSet("SELECT * FROM space WHERE name = ?", "Dummy Space");
        if (spaceResults.next()) {
            dummySpace = new Space(spaceResults.getLong("id"), dummyVenue.getId(), spaceResults.getString("name"), spaceResults.getBoolean("is_accessible"), 0, 0, dailyRate, spaceResults.getInt("max_occupancy"));
        }

    }

    @Test
    public void test_add_reservation() {
        Reservation expectedReservation = null;
        LocalDate date = LocalDate.now();
        Reservation actualReservation = dao.addReservation(dummySpace.getId(), date, 5, 50, "Dummy People");  //method being tested

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM reservation WHERE space_id = ? AND start_date = ?::DATE AND reserved_for = ?", dummySpace.getId(), date, "Dummy People");
        if (results.next()) {
            LocalDate endDate = date.plusDays(4); //number of days - 1
            expectedReservation = new Reservation(results.getLong("reservation_id"),dummySpace.getId(), 50, date, endDate, "Dummy People");
        }

        this.assertReservationsAreEqual(expectedReservation,actualReservation);
    }

    private void assertReservationsAreEqual(Reservation expectedReservation, Reservation actualReservation) {
        Assert.assertEquals(expectedReservation.getReservationId(),actualReservation.getReservationId());
        Assert.assertEquals(expectedReservation.getSpaceId(),actualReservation.getSpaceId());
        Assert.assertEquals(expectedReservation.getNumberOfAttendees(),actualReservation.getNumberOfAttendees());
        Assert.assertEquals(expectedReservation.getStartDate(),actualReservation.getStartDate());
        Assert.assertEquals(expectedReservation.getEndDate(),actualReservation.getEndDate());
        Assert.assertEquals(expectedReservation.getReservedFor(),actualReservation.getReservedFor());
    }

}
