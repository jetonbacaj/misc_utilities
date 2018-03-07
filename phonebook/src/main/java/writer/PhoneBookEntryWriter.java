package writer;

import java.io.IOException;

public class PhoneBookEntryWriter {

    private IPhoneBookWriter phoneBookWriter;

    // for memory backed writer
    public PhoneBookEntryWriter() {
        this(null, false);
    }

    // for file back writer, obviously
    public PhoneBookEntryWriter(final String fileName) {
        this(fileName, true);
    }

    /**
     *
     * While I could've keyed off of fileName which writer to use (wither fileName is null or not),
     * there may be cases where fileName can be used as an entry in an external persistence as
     * a kind of document, for example, to write this stuff out to.
     * Not something I would do, really, but for the sake of "future proofing", here it is
     *
     */
    private PhoneBookEntryWriter(final String fileName, final boolean writeToDisk) {
        phoneBookWriter = (writeToDisk) ?
                new PhoneBookFileWriter(fileName) :
                new PhoneBookMemoryWriter();
    }

    public void write(final String str) throws IOException {
        phoneBookWriter.write(str);
    }

    public String closeOut() {
        return phoneBookWriter.wrapUp();
    }
}
