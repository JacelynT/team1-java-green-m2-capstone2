package com.techelevator;

import com.techelevator.jdbcDAO.JDBCReservationDAO;
import com.techelevator.jdbcDAO.JDBCSpaceDAO;
import com.techelevator.jdbcDAO.JDBCVenueDAO;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

        //title

        while (true) {

            //print menu
            System.out.print("\n");
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

    } //end runMainMenu()

    public void VenueMenu() {
        while (true) {
            System.out.print("\n");
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
                this.displaySpacesMenu(venueId);
            }
            else if (result == 0) {
                break;
            }
            else {
                System.out.println("Invalid input.");
            }

        }

    } // end VenueMenu()

    public void displayVenues() {
        System.out.print("\n");
        int count = 1;
        for (Venue venue : venueDAO.retrieveAllVenues()) {
            System.out.println(count + ") " + venue.getName());
            count++;
        }
    } //end displayVenues()

    public void displayVenueSpaces(Long venueId) {
        int count = 1;

        //Formatting Strings for display columns
        String numberCol = "%-3s";
        String nameCol = "%-25s";
        String openCol = "%-6s";
        String closeCol = "%-6s";
        String rateCol = "%-12s";
        String maxOcCol = "%-4d";

        //display header
        System.out.print("\n");
        System.out.println(venueDAO.retrieveVenueDetails(venueId).getName());
        System.out.print("\n");
        System.out.println(
                String.format(numberCol,"")
                + " " + String.format(nameCol,"Name")
                + " " + String.format(openCol,"Open")
                + " " + String.format(closeCol,"Close")
                + " " + String.format(rateCol,"Daily Rate")
                + " " + String.format("%-14s","Max. Occupancy")
        );

        //for each loop to display table
        for (Space space : spaceDAO.retrieveSpacesFromVenues(venueId)) {
            String countString = "#" + count;
            System.out.println(String.format(numberCol,countString)
                            + " " + String.format(nameCol,space.getName())
                            + " " + String.format(openCol,this.openDateIntToString(space.getOpenFrom()))
                            + " " + String.format(closeCol,this.openDateIntToString(space.getOpenTo()))
                            + " " + String.format(rateCol,NumberFormat.getCurrencyInstance(Locale.US).format(space.getDailyRate()))
                            + " " + String.format(maxOcCol,space.getMaxOccupancy())
            );

            count++;
        }
    } //end displayVenueSpaces()

    public String openDateIntToString(int monthDate) {
        String[] month = new String[] {"Jan.","Feb.","Mar.","Apr.","May","Jun.","Jul.","Aug.","Sep.","Oct.","Nov.","Dec."};
        if (monthDate > 0 && monthDate < 13) {
            return month[monthDate - 1];
        }
        else {
            return "";
        }
    } // end openDateIntToString

    public void displaySpacesMenu(Long venueId) {
        int result = -1;
        while (true) {
            System.out.print("\n");
            System.out.println("What would you like to do next?");
            System.out.println("1) Reserve a Space");
            System.out.println("R) Return to Previous Screen");
            try {
                result = userInput.verifyUserInput("R");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number or R");
            }

            if (result == 1) {
                this.displayReservationMenu(venueId);
                break;
            } else if (result == 0) {
                break;
            } else {
                System.out.println("Invalid input");
            }
        }

    } //end displaySpacesMenu()

    public void displayReservationMenu(Long venueId) {
        System.out.print("\n");
        System.out.println("When do you need the space (yyyy-MM-dd)? ");
        LocalDate date = userInput.verifyUserDate();
        System.out.print("\n");
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
        System.out.print("\n");
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
        if (validSpaces.size() > 0) {
            this.displayValidSpaces(validSpaces, days);
            this.makeReservation(validSpaces, venueId, date, days, attendance);
        }
        else {
            System.out.println("No spaces meet the criteria.");
        }

    } //end displayReservationMenu()

    public void displayValidSpaces(List<Space> validSpaceList, int days) {

        BigDecimal dayMultiplier = BigDecimal.valueOf(days - 1);

        String spaceNumberCol = "%-8d";
        String nameCol = "%-25s";
        String rateCol = "%-12s";
        String maxOcCol = "%-12d";
        String accessibleCol = "%-12s";
        String totalCol = "%-10s";

        System.out.print("\n");
        System.out.println("The following spaces are available based on your needs:");
        System.out.print("\n");
        System.out.println(
                String.format("%-8s","Space #")
                + " " + String.format(nameCol,"Name")
                + " " + String.format(rateCol,"Daily Rate")
                + " " + String.format("%-12s","Max Occup.")
                + " " + String.format(accessibleCol,"Accessible?")
                + " " + String.format(totalCol,"Total Cost")
        );

        for (Space space : validSpaceList) {
            BigDecimal totalCost = (space.getDailyRate().multiply(dayMultiplier));
            String accessible = "";
            if(space.isAccessible()) {
                accessible = "Yes";
            }
            else {
                accessible = "No";
            }

            System.out.println(
                    String.format(spaceNumberCol,space.getId())
                    + " " + String.format(nameCol,space.getName())
                    + " " + String.format(rateCol,NumberFormat.getCurrencyInstance(Locale.US).format(space.getDailyRate()))
                    + " " + String.format(maxOcCol,space.getMaxOccupancy())
                    + " " + String.format(accessibleCol,accessible)
                    + " " + String.format(totalCol,NumberFormat.getCurrencyInstance(Locale.US).format(totalCost))

            );


        }

    }  // end displayValidSpaces()

    public void makeReservation(List<Space> validSpacesList, Long venueId, LocalDate date, int numberOfDays, int attendance) {
        //getting list of valid IDs from list of valid spaces
        List<Long> validSpaceIds = new ArrayList<>();
        for(Space space: validSpacesList) {
           validSpaceIds.add(space.getId());
        }

        System.out.print("\n");
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
                    System.out.print("\n");
                    System.out.println("Who is this reservation for? ");
                    String reservationName = userInput.convertUserString();
                    Reservation reservation = reservationDAO.addReservation((long)result, date, numberOfDays, attendance, reservationName);
                    this.reservationPrintOut(reservation);
                    break;
                }
                else {
                    System.out.println("Invalid input. Please choose a Space # from the list.");
                }
                }
            else if (result == 0) {
                break;
            }
            else {
                System.out.println("Invalid input.");
            }

        }

    } //end makeReservation()

    public void reservationPrintOut(Reservation reservation) {
        System.out.print("\n");
        System.out.println("Thanks for submitting your reservation!");
        System.out.println("The details for your event are listed below: ");
        System.out.print("\n");

        DateTimeFormatter receiptDisplayFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String leftCol = "%16s";

        System.out.println(String.format(leftCol,"Confirmation #: ") + reservation.getReservationId());
        System.out.println(String.format(leftCol,"Venue: ") + venueDAO.retrieveVenueDetails(spaceDAO.retrieveSpaceDetails(reservation.getSpaceId()).getVenueId()).getName());  //retrieves space ID from the reservation object and then used the space object to get the venue object and then used the venue getter to get the venue name.
        System.out.println(String.format(leftCol,"Space: ") + spaceDAO.retrieveSpaceDetails(reservation.getSpaceId()).getName());
        System.out.println(String.format(leftCol,"Reserved For: ") + reservation.getReservedFor());
        System.out.println(String.format(leftCol,"Attendees: ") + reservation.getNumberOfAttendees());
        System.out.println(String.format(leftCol,"Arrival Date: ") + reservation.getStartDate().format(receiptDisplayFormat));
        System.out.println(String.format(leftCol,"Depart Date: ") + reservation.getEndDate().format(receiptDisplayFormat));
        long longNumberOfDays = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        BigDecimal numberOfDays = new BigDecimal(longNumberOfDays);
        BigDecimal total = numberOfDays.multiply(spaceDAO.retrieveSpaceDetails(reservation.getSpaceId()).getDailyRate());
        System.out.println(String.format(leftCol,"Total Cost: ") + NumberFormat.getCurrencyInstance(Locale.US).format(total));

        System.out.print("\n");
        System.out.println("Please press enter to return to the list of venues.");
        System.out.print("\n");
        userInput.waitForEnterKey();

    } // end reservationPrintOut()

}
