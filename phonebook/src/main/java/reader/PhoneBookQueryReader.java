package reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PhoneBookQueryReader {

    private String queryFileName;
    private List<String> phoneBookQueryList = new LinkedList<>();

    public PhoneBookQueryReader(final String queryFileName) {
        this.queryFileName = queryFileName;
    }

    public List<String> load() {
        try {
            final BufferedReader bufferedReader = new BufferedReader(new FileReader(this.queryFileName));

            String line;
            while ((line = bufferedReader.readLine()) != null && line.length() != 0) {
                phoneBookQueryList.add(line);
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.err.println(
                    String.format("Could not find file = %s. Please ensure the file (and its path) are correct.",
                            this.queryFileName));
        } catch (IOException e) {
            System.err.println("Could not read from the query file. Cause = " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error reading query file. Cause = " + e.getMessage());
        }

        return Collections.unmodifiableList(phoneBookQueryList);
    }
}