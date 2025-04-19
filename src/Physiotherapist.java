
import java.time.*;
import java.util.*;

public class Physiotherapist {
    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private List<String> areasOfExpertise;
    private Map<Integer, List<WorkingTime>> workingSchedules = new HashMap<>();
    private List<Treatment> treatments = new ArrayList<>();
    private int attendedCount = 0;

    public Physiotherapist(String id, String fullName, String address, String phoneNumber,
                           List<String> areasOfExpertise, List<WorkingTime> workingTimes) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.areasOfExpertise = areasOfExpertise;

        for (int week = 1; week <= 4; week++) {
            workingSchedules.put(week, new ArrayList<>(workingTimes));
        }
    }

    public Physiotherapist(String id, String fullName, String address, String phoneNumber,
                           List<String> areasOfExpertise) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.areasOfExpertise = areasOfExpertise;
        generate4WeekSchedule();
    }

    private void generate4WeekSchedule() {
        for (int week = 1; week <= 4; week++) {
            List<WorkingTime> schedule = new ArrayList<>();
            for (DayOfWeek day : DayOfWeek.values()) {
                int startHour = 9 + (week % 3);
                int endHour = 17 + (week % 2);
                schedule.add(new WorkingTime(day, LocalTime.of(startHour, 0), LocalTime.of(endHour, 0)));
            }
            workingSchedules.put(week, schedule);
        }
    }

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public List<Treatment> getAvailableTreatments(int week) {
        List<Treatment> available = new ArrayList<>();
        List<WorkingTime> weekTimes = workingSchedules.get(week);

        if (weekTimes == null) return available;

        for (Treatment t : treatments) {
            LocalDateTime dateTime = t.getDateTime();
            if (dateTime == null || t.getPatient() != null) continue;

            DayOfWeek day = dateTime.getDayOfWeek();
            LocalTime time = dateTime.toLocalTime();

            for (WorkingTime slot : weekTimes) {
                if (slot.getDay().equals(day)
                        && !time.isBefore(slot.getStartTime())
                        && !time.isAfter(slot.getEndTime())) {
                    available.add(t);
                    break;
                }
            }
        }

        return available;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }

    public void incrementAttendedCount() {
        attendedCount++;
    }

    public int getAttendedCount() {
        return attendedCount;
    }

    @Override
    public String toString() {
        return fullName;
    }
}
