package com.techelevator;

import com.techelevator.jdbcDAO.JDBCReservationDAO;
import com.techelevator.jdbcDAO.JDBCSpaceDAO;
import com.techelevator.jdbcDAO.JDBCVenueDAO;

import javax.sql.DataSource;
import java.util.Scanner;

public class Menu {

    private DataSource dataSource;
    private UserInput userInput = new UserInput();
    private JDBCVenueDAO venueDAO;
    private JDBCSpaceDAO spaceDAO;
    private JDBCReservationDAO reservationDAO;

    public Menu(DataSource dataSource) {
        //create daos here
        this.dataSource = dataSource;
        venueDAO = new JDBCVenueDAO(dataSource);
        spaceDAO = new JDBCSpaceDAO(dataSource);
        reservationDAO = new JDBCReservationDAO(dataSource);
    }


    public void runMainMenu() {

        //add fancy beginning?

        while (true) {

            //print menu
            System.out.println("What would you like to do?");
            System.out.println("1) List Venues");
            System.out.println("Q) Quit");

            int result = -1;
            try {
                result = userInput.verifyUserInput("Q");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number or Q");
            }

            if (result == 1) {
                this.VenueMenu();
            }
            else if (result == 0) {
                break;
            }
            else {
                System.out.println("Invalid input");
            }

        }

    }

    public void VenueMenu() {
        while (true) {
            System.out.println("Which venue would you like to view?");
            venueDAO.retrieveAllVenues();
            this.displayVenues();
            System.out.println("R) Return to Previous Screen");

            //UserInput
            int result = -1;
            try {
                result = userInput.verifyUserInput("R");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number or R");
            }
            if (result > 0 && result <= venueDAO.retrieveAllVenues().size()) {
                this.displayVenueSpaces();
            }
            else if (result == 0) {
                break;
            }
            else {
                System.out.println("Invalid input.");
            }

        }

    }

    public void displayVenues() {
        int count = 1;
        for (Venue venue : venueDAO.retrieveAllVenues()) {
            System.out.println(count + ") " + venue.getName());
            count++;
        }
    }

    public void displayVenueSpaces() {

    }



}
