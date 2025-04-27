public class Patient {
    private String id;
    private String name;
    private String address;
    private int phoneNumber;

    public Patient(String id, String name, String address, int phoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public int getPhoneNumber() { return phoneNumber; }

    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setPhoneNumber(int phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "Patient{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}
