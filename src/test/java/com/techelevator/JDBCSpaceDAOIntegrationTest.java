package com.techelevator;

import com.techelevator.jdbcDAO.JDBCSpaceDAO;
import com.techelevator.jdbcDAO.JDBCVenueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class JDBCSpaceDAOIntegrationTest extends DAOIntegrationTest {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private Long dummyCityId;
    private Venue dummyVenue;
    private JDBCSpaceDAO dao;

    @Before
    public void setUp() {
        dataSource = getDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JDBCSpaceDAO(dataSource);
        jdbcTemplate.update("INSERT INTO state (abbreviation, name) VALUES (?, ?)", "DU", "DummyState");
        jdbcTemplate.update("INSERT INTO city (name, state_abbreviation) VALUES (?, ?)", "DummyCity", "DU");
        SqlRowSet resultCity = jdbcTemplate.queryForRowSet("SELECT * FROM city WHERE name = ?", "DummyCity");
        if (resultCity.next()) {
            dummyCityId = resultCity.getLong("id");
        }
        String sql = "INSERT INTO venue (name, city_id, description) VALUES (?,?,?)";
        jdbcTemplate.update(sql, "Dummy Venue", dummyCityId, "Yep, still 100% a test.");

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM venue WHERE name = ?", "Dummy Venue");
        Long id = (long) 0;
        if (results.next()) {
            id = results.getLong("id");
        }
        List<String> dummyCategories = new ArrayList<>();
        dummyCategories.add("Dummy Category");
        dummyVenue = new Venue(id, results.getString("name"), dummyCityId, results.getString("description"), dummyCategories);
    }

    @Test
    public void test_retrieve_spaces_from_venues() {
        List<Space> spaceList = new ArrayList<>();
        BigDecimal dailyRate = new BigDecimal(8.75);

        Space space1 = this.createTestSpace("Test Space1", true, 1, 8, dailyRate, 200);
        Space space2 = this.createTestSpace("Test Space2", false, 2, 9, dailyRate, 100);

        spaceList = dao.retrieveSpacesFromVenues(dummyVenue.getId());  //method being tested

        Assert.assertTrue(this.checkIfSpaceExists(spaceList, space1));
        Assert.assertTrue(this.checkIfSpaceExists(spaceList, space2));
    }


    //  ******
    //TODO -- null open and close months are valid
    //     -- why is test working when it shouldn't for get details?
    //     -- need to retrieve next serial in tests?


    @Test
    public void test_retrieve_valid_spaces() {
        List<Space> spaceList = new ArrayList<>();
        BigDecimal dailyRate = new BigDecimal(8.75);
        LocalDate reservationDate = LocalDate.of(2020, 6, 15);
        LocalDate space4ReservationStart = LocalDate.of(2020, 6, 12);
        LocalDate space4ReservationEnd = LocalDate.of(2020, 6, 17);

        Space space1 = this.createTestSpace("Test Space1", true, 3, 7 ,dailyRate, 200);
        Space space2 = this.createTestSpace("Test Space2", false, 7, 9, dailyRate, 100);
        Space space3 = this.createTestSpace("Test Space3", false, 5, 9, dailyRate, 15);
        Space space4 = this.createTestSpace("Test Space4", false, 4, 11, dailyRate, 100);

        jdbcTemplate.update("INSERT INTO reservation (space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (?,?,?,?,?)",
                space4.getId(), 50, space4ReservationStart, space4ReservationEnd, "DummyGuest");

        spaceList = dao.retrieveValidSpaces(dummyVenue.getId(), reservationDate, 5, 20);

        Assert.assertTrue(this.checkIfSpaceExists(spaceList, space1));
        Assert.assertFalse(this.checkIfSpaceExists(spaceList, space2));
        Assert.assertFalse(this.checkIfSpaceExists(spaceList, space3));
        Assert.assertFalse(this.checkIfSpaceExists(spaceList, space4));
    }

    @Test
    public void test_retrieve_space_details() {
        BigDecimal dailyRate = new BigDecimal(4.25);

        Space space = this.createTestSpace("DummySpace", true, 2, 8, dailyRate, 40);

        Space resultSpace = dao.retrieveSpaceDetails(space.getId());

        Assert.assertTrue(this.checkSpacesAreEqual(space, resultSpace));

    }

    @Test
    public void test_retrieve_space_details_dates_null() {
        BigDecimal dailyRate = new BigDecimal(4.25);
        Long id = (long)0;

        jdbcTemplate.update("INSERT INTO space (venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (?,?,?,?,?,?,?)",
                dummyVenue.getId(), "DummySpaceNullDates", true, null, null, dailyRate, 40);
        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM space WHERE name = ?", "DummySpaceNullDates");
        if (results.next()) {
            id = results.getLong("id");
        }

        Space resultSpace = dao.retrieveSpaceDetails(id);

        SqlRowSet spaceInDatabase = jdbcTemplate.queryForRowSet("SELECT * FROM space WHERE id = ?", id);
        if (spaceInDatabase.next()) {
            Long dbSpaceId = spaceInDatabase.getLong("id");
            Long resultSpaceId = resultSpace.getId();
            Assert.assertEquals(dbSpaceId,resultSpaceId);
            Long dbSpaceVenueId = spaceInDatabase.getLong("venue_id");
            Long resultSpaceVenueId = resultSpace.getVenueId();
            Assert.assertEquals(dbSpaceVenueId,resultSpaceVenueId);
            Assert.assertEquals(spaceInDatabase.getString("name"),resultSpace.getName());
            Assert.assertEquals(spaceInDatabase.getBoolean("is_accessible"),resultSpace.isAccessible());

            Assert.assertEquals(0,resultSpace.getOpenFrom());
            Assert.assertEquals(0,resultSpace.getOpenTo());

            BigDecimal dbSpaceRate = spaceInDatabase.getBigDecimal("daily_rate");
            BigDecimal resultSpaceRate = resultSpace.getDailyRate();
            Assert.assertEquals(dbSpaceRate,resultSpaceRate);
            Assert.assertEquals(spaceInDatabase.getInt("max_occupancy"),resultSpace.getMaxOccupancy());
        }

    }

    private Space createTestSpace(String name, boolean isAccessible, int openFrom, int openTo, BigDecimal dailyRate, int maxOccupancy) {
        jdbcTemplate.update("INSERT INTO space (venue_id, name, is_accessible, open_from, open_to, daily_rate, max_occupancy) VALUES (?,?,?,?,?,?,?)",
                dummyVenue.getId(), name, isAccessible, openFrom, openTo, dailyRate, maxOccupancy);

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM space WHERE name = ?", name);
        if (results.next()) {
            return new Space(results.getLong("id"), dummyVenue.getId(), name, isAccessible, openFrom, openTo, dailyRate, maxOccupancy);
        }
        return null;
    }

    private boolean checkSpacesAreEqual(Space expectedSpace, Space actualSpace) {
        if (
                expectedSpace.getId().equals(actualSpace.getId())
                && expectedSpace.getVenueId().equals(actualSpace.getVenueId())
                && expectedSpace.getName().equals(actualSpace.getName())
                && expectedSpace.isAccessible() == actualSpace.isAccessible()
                && expectedSpace.getOpenFrom() == (actualSpace.getOpenFrom())
                && expectedSpace.getOpenTo() == (actualSpace.getOpenTo())
                && expectedSpace.getDailyRate().equals(actualSpace.getDailyRate())
                && expectedSpace.getMaxOccupancy() == (actualSpace.getMaxOccupancy())
        ) {
            return true;
        }
        return false;
    }

    private boolean checkIfSpaceExists(List<Space> spaceList, Space testSpace) {
        for (int i = 0; i < spaceList.size(); i++) {
            if (testSpace.getId().equals(spaceList.get(i).getId())) {
                return true;
            }
        }
        return false;
    }


}
