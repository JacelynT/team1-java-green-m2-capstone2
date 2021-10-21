package com.techelevator;

import com.techelevator.jdbcDAO.JDBCSpaceDAO;
import com.techelevator.jdbcDAO.JDBCVenueDAO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
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
        jdbcTemplate.update("INSERT INTO city (name, state_abbreviation) VALUES (?, ?)",  "DummyCity", "DU");
        SqlRowSet resultCity = jdbcTemplate.queryForRowSet("SELECT * FROM city WHERE name = ?", "DummyCity");
        if (resultCity.next()){dummyCityId = resultCity.getLong("id");}
        String sql = "INSERT INTO venue (name, city_id, description) VALUES (?,?,?)";
        jdbcTemplate.update(sql, "Dummy Venue", dummyCityId, "Yep, still 100% a test.");

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM venue WHERE name = ?", "Dummy Venue");
        Long id = (long)0;
        if (results.next()) {
            id = results.getLong("id");
        }
        List<String> dummyCategories = new ArrayList<>();
        dummyCategories.add("Dummy Category");
        dummyVenue = new Venue(id, results.getString("name"),dummyCityId,results.getString("decription"), dummyCategories);
    }

    @Test
    public void test_retrieve_spaces_from_venues() {

    }

    @Test
    public void test_retrieve_valid_spaces() {

    }

    @Test
    public void test_retrieve_space_details() {

    }



}
