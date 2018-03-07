package reader;

import model.PhoneBookEntries;
import model.PhoneBookEntry;
import util.PhoneBookParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PhoneBookDirectoryReader {
    private String fileContainingInformation;
    private Map<String, PhoneBookEntries> phoneBookEntriesMap = new HashMap<>();

    public PhoneBookDirectoryReader(final String fileContainingInformation) {
        this.fileContainingInformation = fileContainingInformation;
    }

    /**
     * Load the phonebook directory from the file.  This will open up the file
     * and attempt to read each entry, line by line
     *
     * @return Map (String{Last Name} -> model.PhoneBookEntry).  It's unmodifiable to preserve integrity
     */
    public Map<String, PhoneBookEntries> load() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.fileContainingInformation));

            String line;
            while ((line = bufferedReader.readLine()) != null && line.length() != 0) {
                final PhoneBookEntry phoneBookEntry = PhoneBookParser.parseLine(line);

                if (phoneBookEntry != null) {
                    if (phoneBookEntriesMap.containsKey(phoneBookEntry.getLastName())) {
                        phoneBookEntriesMap.get(phoneBookEntry.getLastName()).addPhoneBookEntry(phoneBookEntry);
                    } else {
                        final PhoneBookEntries phoneBookEntries = new PhoneBookEntries();
                        phoneBookEntries.addPhoneBookEntry(phoneBookEntry);
                        phoneBookEntriesMap.put(phoneBookEntry.getLastName(), phoneBookEntries);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(
                    String.format("Could not find file = %s. Please ensure the file (and its path) are correct.",
                            this.fileContainingInformation));
        } catch (IOException e) {
            System.err.println("Could not read from the phonebook file. Cause = " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error reading the phonebook. Cause = " + e.getMessage());
        }

        return phoneBookEntriesMap.size() == 0 ? null : Collections.unmodifiableMap(phoneBookEntriesMap);
    }
}
