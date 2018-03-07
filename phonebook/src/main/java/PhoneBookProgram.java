import com.sun.tools.javac.util.Assert;
import model.PhoneBookEntries;
import model.PhoneBookEntry;
import reader.PhoneBookDirectoryReader;
import reader.PhoneBookQueryReader;
import writer.PhoneBookEntryWriter;

import java.io.IOException;
import java.util.Map;

public class PhoneBookProgram {

    public static void main(final String... args) {
        if (args.length != 2) {
            System.out.println("usage: PhoneBookProgram <phone_dataset.csv> <queries.csv>");
        } else {
            execute(args[0], args[1], new PhoneBookEntryWriter("output.csv"));
        }
    }

    /**
     * @param phoneBookFileName    The phonebook file path
     * @param queries              The queries file path
     * @param phoneBookEntryWriter The writer where all of the results will be "written" to
     * @return The results of all written results.  If the PhoneBookEntryWriter is file backed, then the result is always null
     */
    public static String execute(final String phoneBookFileName, final String queries, final PhoneBookEntryWriter phoneBookEntryWriter) {

        // create the phone book directory
        final PhoneBookDirectoryReader phoneBookDirectoryReader = new PhoneBookDirectoryReader(phoneBookFileName);

        // Get the actual entries
        final Map<String, PhoneBookEntries> phoneBookEntriesMap = phoneBookDirectoryReader.load();

        // Get queries
        final PhoneBookQueryReader phoneBookQueryReader = new PhoneBookQueryReader(queries);

        // print out the results
        for (final String query : phoneBookQueryReader.load()) {
            try {
                writePhoneBookQueryResults(phoneBookEntriesMap, query, phoneBookEntryWriter);
            } catch (IOException e) {
                System.err.println("Error while reading the query file. Reason: " + e.getMessage());
            }
        }

        // close things out, and return the results, if applicable
        return phoneBookEntryWriter.closeOut();
    }

    /**
     * Write out the lookup results for supplied last name
     * @param phoneBookEntriesMap The phonebook
     * @param lastName The last name to search the phonebook by
     * @param phoneBookEntryWriter The writer class to "write" the result to
     * @throws IOException If any IO errors are encoutered
     */
    private static void writePhoneBookQueryResults(final Map<String, PhoneBookEntries> phoneBookEntriesMap,
                                                   final String lastName,
                                                   final PhoneBookEntryWriter phoneBookEntryWriter) throws IOException {

        Assert.checkNonNull(phoneBookEntriesMap, "phonebook is null!");
        Assert.checkNonNull(lastName, "the last name is null");
        Assert.checkNonNull(phoneBookEntryWriter, "the phonebook entry writer is null!");

        phoneBookEntryWriter.write(String.format("Matches for: %s", lastName));
        if (phoneBookEntriesMap.containsKey(lastName.toLowerCase())) {
            final PhoneBookEntries phoneBookEntries = phoneBookEntriesMap.get(lastName.toLowerCase());
            int resultCounter = 1;
            for (PhoneBookEntry phoneBookEntry : phoneBookEntries.getPhoneBookEntries()) {
                phoneBookEntryWriter.write(String.format("Result %d: %s", resultCounter, phoneBookEntry.toString()));
                ++resultCounter;
            }
        } else {
            phoneBookEntryWriter.write("No results found");
        }
    }
}