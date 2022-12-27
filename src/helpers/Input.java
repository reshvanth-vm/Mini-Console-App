package helpers;

import java.util.function.Predicate;

public final class Input {

    private Input() {
    }

    private static void printErrorMessages(String... errorMessages) {
        for (String errorMessage : errorMessages)
            System.out.println(errorMessage);
        System.out.println();
    }

    public static int getInt(String message, int floor, int ceil) {
        while (true)
            try {
                int input = Integer.parseInt(getString(message));
                if (input >= floor && input <= ceil)
                    return input;
                else
                    System.out.println("Please enter an integer ranging from " + floor + " to " + ceil + "!");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!, Please enter an integer");
            }
    }

    public static String getPassword(String message) {
        return new String(System.console().readPassword(message));
    }

    public static String getPassword(String message, Predicate<String> predicate, String... messagesOnInvalidPassword) {
        String password = null;
        do {
            if (password != null)
                printErrorMessages(messagesOnInvalidPassword);
            password = getPassword(message);
        } while (!predicate.test(password));
        return password;
    }

    public static String getString(String message) {
        String s = null;
        do {
            if (s != null)
                System.out.println("Please enter something!");
            s = System.console().readLine(message).trim();
        } while (s.isEmpty());
        return s;
    }

    public static String waitUntilReturn() {
        System.out.println();
        return System.console().readLine("Press enter to go back...");
    }

    public static String getString(String message, Predicate<String> predicate, String... messagesOnInvalidPassword) {
        String input = null;
        do {
            if (input != null)
                printErrorMessages(messagesOnInvalidPassword);
            input = getString(message);
        } while (!predicate.test(input));
        return input;
    }

    public static boolean getYesOrNoAsBoolean(String message) {
        do {
            switch (getString(message).toLowerCase()) {
                case "y":
                case "yes":
                    return true;
                case "n":
                case "no":
                    return false;
            }
            System.out.println("Please enter a valid input from (yes, y, no, n)");
            System.out.println();
        } while (true);
    }

    public static String getEmail(String message) {
        String email = null;
        do {
            if (email != null)
                System.out.println("Invalid email address");
            email = getString(message);
        } while (!email.matches(
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"));
        return email;
    }

    public static String getUsername(String message) {
        return getString(message, Input::isValidName, "Invalid name!", "Please enter a valid name!");
    }

    public static String getPasswordAfterConformPassword() {
        String password = Input.getPassword("ENTER PASSWORD >> ", Input::isValidPassword, "Invalid password",
                        "Password should only contain alphabetical characters and numbers and lenght less than 10");
        System.out.println();
        return Input.getPassword("CONFIMR PASSWORD >> ", (cp) -> new String(cp).equals(password), "Please try again, confirm password does not match new password!");
    }

    private static boolean isValidPassword(String password) {
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (!(Character.isAlphabetic(ch) || Character.isDigit(ch))) {
                return false;
            }
        }
        return password.length() <= 9;
    }

    private static boolean isValidName(String name) {
        boolean spaceFound = false;
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isAlphabetic(name.charAt(i))) {
                if (Character.isSpaceChar(name.charAt(i))) {
                    if (spaceFound) {
                        return false;
                    } else {
                        spaceFound = true;
                    }
                } else
                    return false;
            } else {
                spaceFound = false;
            }
        }
        return true;
    }
}
