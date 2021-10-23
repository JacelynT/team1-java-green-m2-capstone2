package com.techelevator;

import com.techelevator.jdbcDAO.JDBCReservationDAO;
import com.techelevator.jdbcDAO.JDBCSpaceDAO;
import com.techelevator.jdbcDAO.JDBCVenueDAO;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
                Venue venue = venueDAO.retrieveAllVenues().get(result-1);
                Long venueId = venue.getId();
                this.displayVenueSpaces(venueId);
                List<Space> validSpaces = this.displaySpacesMenu(venueId);
                this.makeReservation(validSpaces);
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

    public void displayVenueSpaces(Long venueId) {
        int count = 1;
        System.out.println(venueDAO.retrieveVenueDetails(venueId).getName());
        System.out.print("\n");
        System.out.println("Header");

        for (Space space : spaceDAO.retrieveSpacesFromVenues(venueId)) {
            System.out.println("#"
                            + count
                            + " " + space.getName()
                            + " " + spaceDAO.openDateIntToString(space.getOpenFrom())
                            + " " + spaceDAO.openDateIntToString(space.getOpenTo())
                            + " " + space.getDailyRate()
                            + " " + space.getMaxOccupancy()
            );

            count++;
        }
    }

    public List<Space> displaySpacesMenu(Long venueId) {
        System.out.println("What would you like to do next?");
        System.out.println("1) Reserve a Space");
        System.out.println("R) Return to Previous Screen");

        int result = -1;
        while (true) {
            try {
                result = userInput.verifyUserInput("R");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number or R");
            }

            if (result == 1) {
                return this.displayReservationMenu(venueId);
            } else if (result == 0) {
                break;
            } else {
                System.out.println("Invalid input");
            }
        }
        return null;
    }


    public List<Space> displayReservationMenu(Long venueId) {
        System.out.println("When do you need the space (yyyy-MM-dd)? ");
        LocalDate date = userInput.verifyUserDate();
        System.out.println("How many days will you need the space? ");
        int days = -1;
        while (true) {
            days = userInput.verifyUserInput("0");
            if (days == 0) {
                System.out.println("Invalid input");
            } else {
                break;
            }
        }
        System.out.println("How many people will be in attendance? ");
        int attendance = -1;
        while (true) {
            attendance = userInput.verifyUserInput("0");
            if (attendance == 0) {
                System.out.println("Invalid input");
            } else {
                break;
            }
        }

        List<Space> validSpaces = spaceDAO.retrieveValidSpaces(venueId, date, days, attendance);
        this.displayValidSpaces(validSpaces, days);
        return validSpaces;

    }

    public void displayValidSpaces(List<Space> validSpaceList, int days) {

        BigDecimal dayMultiplier = BigDecimal.valueOf(days);

        System.out.println("The following spaces are available based on your needs:");
        System.out.println("Header");

        for (Space space : validSpaceList) {
            BigDecimal totalCost = (space.getDailyRate().multiply(dayMultiplier));
            String accessible = "";
            if(space.isAccessible()) {
                accessible = "Yes";
            }
            else {
                accessible = "No";
            }

            System.out.println(space.getId()
                    + " " + space.getName()
                    + " " + space.getDailyRate()
                    + " " + space.getMaxOccupancy()
                    + " " + accessible
                    + " $" + totalCost

            );


        }
    }


    public void makeReservation(List<Space> validSpacesList) {
       List<Long> validSpaceIds = new ArrayList<>();
        for(Space space: validSpacesList) {
           validSpaceIds.add(space.getId());
       }

        System.out.println("Which space would you like to reserve (enter 0 to cancel)? ");

            int result = -1;
            while (true) {
                try {
                    result = userInput.verifyUserInput("0");
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number or 0");
                }
                if (result > 0) {
                    if (validSpaceIds.contains((long)result)) {
                    System.out.println("Who is this reservation for? ");
                    userInput.convertUserString();
                    } else {
                        System.out.println("Invalid input. Please choose a Space # from the list.");
                    }
                } else if (result == 0) {
                    break;
                } else {
                    System.out.println("Invalid input.");
                }

            }

    }



}
