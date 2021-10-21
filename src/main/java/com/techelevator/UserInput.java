package com.techelevator;

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


}
