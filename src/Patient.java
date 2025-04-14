public class Patient {
    String id;
    String fullName;
    String address;
    String telephoneNumber;

    public Patient(String id, String fullName, String address, String telephoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
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
}
