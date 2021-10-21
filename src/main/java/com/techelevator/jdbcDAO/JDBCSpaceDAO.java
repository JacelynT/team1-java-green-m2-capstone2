package com.techelevator.jdbcDAO;

import com.techelevator.SpaceDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JDBCSpaceDAO implements SpaceDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JDBCSpaceDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


}
