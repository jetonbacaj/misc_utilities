package writer;

public class PhoneBookMemoryWriter implements IPhoneBookWriter {
    private StringBuilder memoryWriter;

    PhoneBookMemoryWriter() {
        this.memoryWriter = new StringBuilder();
    }

    @Override
    public void write(final String str) {
        this.memoryWriter.append(str).append(System.lineSeparator());
    }

    @Override
    public String wrapUp() {
        return memoryWriter.length() == 0 ? null : memoryWriter.toString();
    }
}
