import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import writer.PhoneBookEntryWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since this "project" is not production grade, no unit tests with an
 * enormous amount of phonebook entries will be tested
 */
class PhoneBookTest {
    @Test
    @DisplayName("Phonebook and query file inputs are null")
    void testBadInputNoParams() {
        final String output = PhoneBookProgram.execute(null, null, new PhoneBookEntryWriter());
        assertNull(output, "Because we're passing in NULL for both phonebook and queries, we shouldn't get back anything");
    }

    @Test
    @DisplayName("Query file is null")
    void testBadInputNullQueryParam() {
        final URL phoneBookUrl = Thread.currentThread().getContextClassLoader().getResource("phonebook.csv");
        final String output = PhoneBookProgram.execute(phoneBookUrl.getFile(), null, new PhoneBookEntryWriter());
        assertNull(output, "Because we're passing in NULL for query file, we shouldn't get back anything");
    }

    @Test
    @DisplayName("Phonebook file is null")
    void testBadInputNullPhoneBookParam() {
        final URL queryUrl = Thread.currentThread().getContextClassLoader().getResource("query.csv");
        try {
            final String output = PhoneBookProgram.execute(null, queryUrl.getFile(), new PhoneBookEntryWriter());
            fail("Phonebook filename is null - we should fail the assertion!");
        } catch (AssertionError ae) {
            assertEquals("phonebook is null!", ae.getMessage());
        }
    }

    @Test
    @DisplayName("Phonebook and query file inputs are of non-existent files")
    void testBadInputBadParams() {
        final String output = PhoneBookProgram.execute("nonexistentFile", "nonexistentFile", new PhoneBookEntryWriter());
        assertNull(output, "Because we're passing in NULL for both phonebook and queries, we shouldn't get back anything");
    }

    @Test
    @DisplayName("Query file is nonexistent")
    void testBadInputBadQueryParam() {
        final URL phoneBookUrl = Thread.currentThread().getContextClassLoader().getResource("phonebook.csv");
        final String output = PhoneBookProgram.execute(phoneBookUrl.getFile(), "nonexistentFile", new PhoneBookEntryWriter());
        assertNull(output, "Because we're passing in NULL for query file, we shouldn't get back anything");
    }

    @Test
    @DisplayName("Phonebook file is nonexistent")
    void testBadInputBadPhoneBookParam() {
        final URL queryUrl = Thread.currentThread().getContextClassLoader().getResource("query.csv");
        try {
            final String output = PhoneBookProgram.execute("nonexistentFile", queryUrl.getFile(), new PhoneBookEntryWriter());
            fail("Phonebook filename is null - we should fail the assertion!");
        } catch (AssertionError ae) {
            assertEquals("phonebook is null!", ae.getMessage());
        }
    }

    @Test
    @DisplayName("Run against the supplied input")
    void testWithSuppliedInput() throws IOException {
        final URL phoneBookUrl = Thread.currentThread().getContextClassLoader().getResource("phonebook.csv");
        final URL queryUrl = Thread.currentThread().getContextClassLoader().getResource("query.csv");

        final String output = PhoneBookProgram.execute(phoneBookUrl.getFile(), queryUrl.getFile(), new PhoneBookEntryWriter());

        assertNotNull(output, "Because we're passing in legit phonebook and query file, we should get back the expected output!");

        // now check output against

        // get expected output
        final InputStream expectedOutput = Thread.currentThread().getContextClassLoader().getResourceAsStream("expectedOutput.txt");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(expectedOutput));

        final StringBuilder stringBuilder = new StringBuilder();

        String str;
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str).append(System.lineSeparator());
        }

        assertEquals(stringBuilder.toString(), output, "The output is not matching that which is expected!");
    }

    @Test
    @DisplayName("The supplied phonebook has the same entries multiple times - the resulting phonebook should be pruned")
    void testWithMultipleIdenticalEntriesPhoneBook() throws IOException {
        final URL phoneBookUrl = Thread.currentThread().getContextClassLoader().getResource("multiple/multiple.csv");
        final URL queryUrl = Thread.currentThread().getContextClassLoader().getResource("query.csv");

        final String output = PhoneBookProgram.execute(phoneBookUrl.getFile(), queryUrl.getFile(), new PhoneBookEntryWriter());

        assertNotNull(output, "Because we're passing in legit phonebook and query file, we should get back the expected output!");

        // now check output against

        // get expected output
        final InputStream expectedOutput = Thread.currentThread().getContextClassLoader().getResourceAsStream("expectedOutput.txt");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(expectedOutput));

        final StringBuilder stringBuilder = new StringBuilder();

        String str;
        while ((str = bufferedReader.readLine()) != null) {
            stringBuilder.append(str).append(System.lineSeparator());
        }

        assertEquals(stringBuilder.toString(), output, "The output is not matching that which is expected!");
    }
}
