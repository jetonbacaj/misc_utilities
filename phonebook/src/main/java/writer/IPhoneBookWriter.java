package writer;

import java.io.IOException;

public interface IPhoneBookWriter {

    void write(final String str) throws IOException;

    String wrapUp();
}
