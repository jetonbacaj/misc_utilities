package model;

public class PhoneBookEntry {
    private String firstName;
    private String lastName;
    private String state;
    private String phoneNumber;

    public PhoneBookEntry(final String firstName,
                          final String lastName,
                          final String state,
                          final String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.state = state;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getState() {
        return state;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", lastName, firstName, state, phoneNumber);
    }
}
