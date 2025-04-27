import java.util.*;

public class Physiotherapist {
    private String id;
    private String name;
    private String address;
    private String phone;
    private List<String> expertise;
    private Map<Integer, List<TreatmentSlot>> schedule;

    public Physiotherapist(String id, String name, String address, String phone, List<String> expertise) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.expertise = expertise;
        this.schedule = new HashMap<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public List<String> getExpertise() { return expertise; }
    public Map<Integer, List<TreatmentSlot>> getSchedule() { return schedule; }

    public void addToSchedule(int week, TreatmentSlot slot) {
        schedule.computeIfAbsent(week, k -> new ArrayList<>()).add(slot);
    }

    @Override
    public String toString() {
        return name + " [" + String.join(", ", expertise) + "]";
    }
}
