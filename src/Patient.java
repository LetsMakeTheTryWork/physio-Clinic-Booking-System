public class Patient {
    private String id;
    private String fullName;
    private String address;
    private String telephoneNumber;

    public Patient(String id, String fullName, String address, String telephoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Patient [ID=" + id + ", Name=" + fullName + ", Phone=" + telephoneNumber + "]";
    }
}
