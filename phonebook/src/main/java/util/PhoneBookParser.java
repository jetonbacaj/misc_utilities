package util;

import model.PhoneBookEntry;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PhoneBookParser {
    // case 1 - Lastname, Firstname, New York, (917) 958-1191
    private static final Pattern LAST_FIRST_STATE_PHONE = Pattern.compile("\\w+, \\w+, \\w+\\s*\\w*, \\(\\d{3}\\)\\s\\d{3}-\\d{4}");

    // case 2 - Firstname Lastname, 9179581191, New York
    private static final Pattern FIRST_LAST_PHONE_W_NO_PARENS_STATE = Pattern.compile("\\w+ \\w+, \\d{10}, \\w+\\s*\\w*");

    // case 3 - Firstname, Lastname, (917) 358-1291, California
    private static final Pattern FIRST_LAST_PHONE_STATE = Pattern.compile("\\w+, \\w+, \\(\\d{3}\\)\\s\\d{3}-\\d{4}, \\w+\\s*\\w*");

    public static PhoneBookEntry parseLine(final String line) {

        if (line == null || line.trim().length() == 0) {
            System.err.println("The line is either null or empty. Returning null");
            return null;
        }

        final String[] lineComponents = line.split(",");

        if (!Arrays.asList(3, 4).contains(lineComponents.length)) {
            System.err.println(
                    String.format("The line has %d components, when we expect either 3 or 4. Returning null",
                            lineComponents.length));
            return null;
        }

        // get the first three components, trimmed.  Since there are at least
        // 3 components, we trim them and have them ready for future use below
        final String lineComponent0 = lineComponents[0].trim();
        final String lineComponent1 = lineComponents[1].trim();
        final String lineComponent2 = lineComponents[2].trim();

        if (LAST_FIRST_STATE_PHONE.matcher(line).matches()) {
            return new PhoneBookEntry(lineComponent1, lineComponent0, lineComponent2, lineComponents[3].trim());
        } else if (FIRST_LAST_PHONE_W_NO_PARENS_STATE.matcher(line).matches()) {
            final String[] splitFirstAndLast = lineComponent0.split(" ");

            // someone is trying to pull a fast one on us
            if (splitFirstAndLast.length != 2) {
                System.err.println(
                        String.format("The `first name last name` line has %d components, when we expect 2. Returning null",
                                splitFirstAndLast.length));
                return null;
            }

            // convert the phone number to a nicely formatted one... and let's hope we don't
            // have a phone number with wonky digits (in case the regex didn't work right)
            final String formattedPhoneNumber = "(" +
                    lineComponent1.substring(0, 3) +
                    ") " + lineComponent1.substring(3, 6) +
                    "-" +
                    lineComponent1.substring(6, lineComponent1.length());

            return new PhoneBookEntry(splitFirstAndLast[0].trim(), splitFirstAndLast[1].trim(), lineComponent2, formattedPhoneNumber);
        } else if (FIRST_LAST_PHONE_STATE.matcher(line).matches()) {
            return new PhoneBookEntry(lineComponent0, lineComponent1, lineComponents[3].trim(), lineComponent2);
        } else {
            System.err.println("The line format did not match anything we expected. Please check the input!");
        }

        return null;
    }
}