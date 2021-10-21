package com.techelevator.jdbcDAO;

import com.techelevator.ReservationDAO;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JDBCReservationDAO implements ReservationDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JDBCReservationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


}
