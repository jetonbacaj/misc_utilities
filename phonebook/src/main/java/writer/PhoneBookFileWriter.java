package writer;

import java.io.FileWriter;
import java.io.IOException;

public class PhoneBookFileWriter implements IPhoneBookWriter {
    private FileWriter fileWriter;
    private boolean canWrite = false;

    PhoneBookFileWriter(final String fileName) {
        try {
            fileWriter = new FileWriter(fileName);
            canWrite = true;
        } catch (IOException e) {
            System.err.println("Error while creating the output file. Reason: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error creating the writer. Cause = " + e.getMessage());
        }
    }

    @Override
    public void write(final String str) throws IOException {
        if(!canWrite) {
            throw new IOException("Cannot write to file - writer did not initialize correctly. " +
                    "Please check the file path, and instantiate a new instance of this class!");
        }
        fileWriter.write(str);
        fileWriter.write(System.lineSeparator());
    }

    @Override
    public String wrapUp() {
        try {
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Could not flush and close out the content. Reason: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while closing up the writer. Cause = " + e.getMessage());
        }
        return null;
    }
}