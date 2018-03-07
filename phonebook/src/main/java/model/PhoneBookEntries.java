package model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class PhoneBookEntries {
    // we're choosing a TreeSet, so that we get insertion sort by first name
    // (then state, then phone number ... not a requirement, but looks neater!)
    private Set<PhoneBookEntry> entries = new TreeSet<>(
            Comparator.comparing(PhoneBookEntry::getFirstName)
                    .thenComparing(PhoneBookEntry::getState)
                    .thenComparing(PhoneBookEntry::getPhoneNumber)
    );

    /**
     * Add entry into the internal phonebook repository.  Each entry
     * will need to have all of the fields non-null-able to be added
     *
     * @param phoneBookEntry The phonebook entry
     */
    public void addPhoneBookEntry(final PhoneBookEntry phoneBookEntry) {
        if (isPhoneBookEntryGood(phoneBookEntry)) {
            entries.add(phoneBookEntry);
        } // requirement says to ignore if phonebook entry is not OK, so we won't raise a fuss about it
    }

    /**
     * Get the current collection of the PhoneBook entries
     *
     * @return An unmodifiable set, to ensure integrity of the entries
     */
    public Set<PhoneBookEntry> getPhoneBookEntries() {
        return Collections.unmodifiableSet(entries);
    }

    private boolean isPhoneBookEntryGood(final PhoneBookEntry phoneBookEntry) {
        // does it have all the fields?

        return phoneBookEntry.getFirstName() != null &&
                phoneBookEntry.getLastName() != null &&
                phoneBookEntry.getState() != null &&
                phoneBookEntry.getPhoneNumber() != null;

        // does it already exist? Do we care?
        //&& entries.contains(phoneBookEntry);
    }
}