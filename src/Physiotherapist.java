import java.util.ArrayList;
import java.util.List;

public class Physiotherapist {
    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private List<String> areasOfExpertise;
    private List<WorkingTime> workingTimes;
    private List<Treatment> treatments = new ArrayList<>(); // Holds assigned treatments

    public Physiotherapist(String id, String fullName, String address, String phoneNumber,
                           List<String> areasOfExpertise, List<WorkingTime> workingTimes) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.areasOfExpertise = areasOfExpertise;
        this.workingTimes = workingTimes;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public List<Treatment> getAvailableTreatments() {
        List<Treatment> available = new ArrayList<>();
        for (Treatment t : treatments) {
            if (t.getPatient() == null) {
                available.add(t);
            }
        }
        return available;
    }

    public String getFullName() {
        return fullName;
    }
    public String getAddress() {
        return address;
    }

    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }

    public List<WorkingTime> getWorkingTimes() {
        return workingTimes;
    }

    public int getAttendedCount() {
        int count = 0;
        for (Treatment t : treatments) {
            if (t.getStatus() == TreatmentStatus.ATTENDED) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
