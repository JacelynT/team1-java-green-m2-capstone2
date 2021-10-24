package com.techelevator.jdbcDAO;

import com.techelevator.Reservation;
import com.techelevator.ReservationDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JDBCReservationDAO implements ReservationDAO {

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public JDBCReservationDAO(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Reservation addReservation(Long spaceId, LocalDate date, int numberOfDays, int attendance, String reservationFor) {
        LocalDate dateThrough = date.plusDays(numberOfDays - 1);
        DateTimeFormatter formatFullDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        jdbcTemplate.update("INSERT INTO reservation (space_id, number_of_attendees, start_date, end_date, reserved_for) VALUES (?, ?, ?::DATE, ?::DATE, ?)",
                spaceId, attendance, date.format(formatFullDate), dateThrough.format(formatFullDate), reservationFor);
        SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM reservation WHERE space_id = ? AND start_date = ?::DATE AND reserved_for = ?", spaceId, date.format(formatFullDate), reservationFor);
        if (results.next()) {
            return new Reservation(results.getLong("reservation_id"), spaceId, attendance, date, dateThrough, reservationFor);
        }
        return null;

    }

}
