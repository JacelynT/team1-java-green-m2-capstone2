package com.techelevator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class UserInput {

    private Scanner scanner = new Scanner(System.in);
    private static String USER_INPUT_STRING = "\n>>>>> ";

    public int verifyUserInput(String exitCharacter) throws NumberFormatException {
        System.out.print(USER_INPUT_STRING);
        String result = scanner.nextLine();
        if (result.equalsIgnoreCase(exitCharacter)) {
            return 0;
        }

        return Integer.parseInt(result);

    }

    public LocalDate verifyUserDate() {

        while (true) {
            String[] splitDate = new String[3];
            while (true) {
                System.out.print(USER_INPUT_STRING);
                String result = scanner.nextLine();
                if (result.contains("/")) {
                    splitDate = result.split("/");
                    break;
                }
                else if (result.contains("-")) {
                    splitDate = result.split("-");
                    break;
                }
                else {
                    System.out.println("Invalid input.");
                }
            }

            if (splitDate[0].length() == 4 && splitDate[1].length() == 2 && splitDate[2].length() == 2) {
                int year = Integer.parseInt(splitDate[0]);
                int month = Integer.parseInt(splitDate[1]);
                int day = Integer.parseInt(splitDate[2]);
                try {
                    LocalDate date = LocalDate.of(year, month, day);
                    return date;
                }
                catch (DateTimeException e) {
                    System.out.println("Invalid date.");
                }

            }
            else {
                System.out.println("Invalid input. Please enter date as yyyy-MM-dd");
            }

        }
    }

    public String convertUserString () {
        System.out.print(USER_INPUT_STRING);
        return scanner.nextLine();
    }

    public void waitForEnterKey() {
        scanner.nextLine();
    }


}
