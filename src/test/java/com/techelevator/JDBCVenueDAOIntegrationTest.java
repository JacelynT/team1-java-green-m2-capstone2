package com.techelevator;

import com.techelevator.jdbcDAO.JDBCVenueDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JDBCVenueDAOIntegrationTest extends DAOIntegrationTest{

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private Long dummyCityId;
    private Long dummyCategory1Id;
    private Long dummyCategory2Id;
    private JDBCVenueDAO dao;

    @Before
    public void setUp() {
        dataSource = getDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JDBCVenueDAO(dataSource);
        jdbcTemplate.update("INSERT INTO state (abbreviation, name) VALUES (?, ?)", "DU", "DummyState");
        jdbcTemplate.update("INSERT INTO city (name, state_abbreviation) VALUES (?, ?)",  "DummyCity", "DU");
        String categorySql = "INSERT INTO category (name) VALUES (?)";
        jdbcTemplate.update(categorySql, "Dummy Category 1");
        jdbcTemplate.update(categorySql, "Dummy Category 2");

        SqlRowSet resultCity = jdbcTemplate.queryForRowSet("SELECT * FROM city WHERE name = ?", "DummyCity");
        if (resultCity.next()){dummyCityId = resultCity.getLong("id");}
        SqlRowSet resultCat1 = jdbcTemplate.queryForRowSet("SELECT * FROM category WHERE name = ?", "Dummy Category 1");
        if (resultCat1.next()){dummyCategory1Id = resultCat1.getLong("id");}
        SqlRowSet resultCat2 = jdbcTemplate.queryForRowSet("SELECT * FROM category WHERE name = ?", "Dummy Category 2");
        if (resultCat2.next()){dummyCategory2Id = resultCat2.getLong("id");}

    }

    @Test
    public void test_retrieve_all_venues() {
        //no parameters, returns List<Venue>
        Venue testVenue1 = createTestVenue("Dummy Venue 1","This is a test.");
        Venue testVenue2 = createTestVenue("Dummy Venue 2","This is still a test.");
        Venue testVenue3 = createTestVenue("Dummy Venue 3","Yes, this too is a test.");

        List<Venue> dummyVenueList = dao.retrieveAllVenues();

        Assert.assertTrue(this.checkIfVenueExists(dummyVenueList,testVenue1));
        Assert.assertTrue(this.checkIfVenueExists(dummyVenueList,testVenue2));
        Assert.assertTrue(this.checkIfVenueExists(dummyVenueList,testVenue3));
    }

    @Test
    public void test_retrieve_venue_details() {
        Venue testVenue = createTestVenue("Dummy Venue", "This is a test.");

        Venue returnedVenue = dao.retrieveVenueDetails(testVenue.getId());

        Assert.assertTrue(this.checkIfVenuesAreEqual(testVenue,returnedVenue));
    }

    private Venue createTestVenue(String name, String description){
        String sql = "INSERT INTO venue (name, city_id, description) VALUES (?,?,?)";
        jdbcTemplate.update(sql, name, dummyCityId, description);

        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM venue WHERE name = ?", name);
        Long id = (long)0;
        if (results.next()) {
               id = results.getLong("id");
        }

        String categoryAddSql = "INSERT INTO category_venue (venue_id, category_id) VALUES (?,?)";
        jdbcTemplate.update(categoryAddSql, id, dummyCategory1Id);
        jdbcTemplate.update(categoryAddSql, id, dummyCategory2Id);
        List<String> categoryList = new ArrayList<>();
        SqlRowSet categoryResults = jdbcTemplate.queryForRowSet("SELECT category.name FROM category JOIN category_venue ON category.id = category_venue.category_id WHERE venue_id = ?",id);
        while (categoryResults.next()) {
            categoryList.add(categoryResults.getString("name"));
        }

        return new Venue(id ,name, dummyCityId, description, categoryList);
    }


    private boolean checkIfVenueExists(List<Venue> venueList, Venue testVenue) {
        for (int i = 0; i<venueList.size(); i++) {
            if (testVenue.getId().equals(venueList.get(i).getId())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfVenuesAreEqual(Venue expectedVenue, Venue actualVenue) {
        if (
                expectedVenue.getId().equals(actualVenue.getId())
                && expectedVenue.getName().equals(actualVenue.getName())
                && expectedVenue.getCityId().equals(actualVenue.getCityId())
                && expectedVenue.getDescription().equals(actualVenue.getDescription())
        ) {
            return true;
        }
        else {
            return false;
        }

    }

}
