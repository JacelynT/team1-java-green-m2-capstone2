package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class UserInput {

    Scanner scanner = new Scanner(System.in);

    public int verifyUserInput(String exitCharacter) throws NumberFormatException {

        String result = scanner.nextLine();
        if (result.equalsIgnoreCase(exitCharacter)) {
            return 0;
        }

        return Integer.parseInt(result);

    }

    public LocalDate verifyUserDate() {

        while (true) {
            String[] splitDate = new String[3];
            String result = scanner.nextLine();
            if (result.contains("/")) {
                splitDate = result.split("/");
            } else if (result.contains("-")) {
                splitDate = result.split("-");
            }

            if (splitDate[0].length() == 4) {
                int year = Integer.parseInt(splitDate[0]);
                int month = Integer.parseInt(splitDate[1]);
                int day = Integer.parseInt(splitDate[2]);

                return LocalDate.of(year, month, day);
            }
            else {
                System.out.println("Invalid input. Please enter date as yyyy-MM-dd");
            }

        }
    }

    public String convertUserString () {
        return scanner.nextLine();
    }


}
