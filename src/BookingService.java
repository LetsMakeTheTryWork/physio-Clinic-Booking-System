import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

public class BookingService {
    List<Physiotherapist> physiotherapists;
    List<Patient> patients;

    public BookingService() {
        physiotherapists = new ArrayList<>();
        patients = new ArrayList<>();
    }

    public void addPhysiotherapist(Physiotherapist physiotherapist) {
        physiotherapists.add(physiotherapist);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public boolean removePatient(Patient patient) {
        for (Physiotherapist physio : physiotherapists) {
            int week = getWeekOfMonth(LocalDate.now());
            for (Treatment treatment : physio.getAvailableTreatments(week)) {
                if (treatment.getPatient() != null && treatment.getPatient().equals(patient)) {
                    treatment.setStatus(TreatmentStatus.CANCELLED);
                    treatment.setPatient(null);
                }
            }
        }
        return patients.remove(patient);
    }

    public List<Treatment> getAvailableTreatmentsByExpertise(String expertise, LocalDateTime date) {
        int week = getWeekOfMonth(date.toLocalDate());
        List<Treatment> availableTreatments = new ArrayList<>();
        for (Physiotherapist physio : physiotherapists) {
            if (physio.getAreasOfExpertise().contains(expertise)) {
                for (Treatment treatment : physio.getAvailableTreatments(week)) {
                    if (treatment.getDateTime().equals(date)) {
                        availableTreatments.add(treatment);
                    }
                }
            }
        }
        return availableTreatments;
    }

    public List<Treatment> getAvailableTreatmentsByPhysiotherapist(String physiotherapistName, LocalDateTime date) {
        int week = getWeekOfMonth(date.toLocalDate());
        List<Treatment> availableTreatments = new ArrayList<>();
        for (Physiotherapist physio : physiotherapists) {
            if (physio.getFullName().equals(physiotherapistName)) {
                for (Treatment treatment : physio.getAvailableTreatments(week)) {
                    if (treatment.getDateTime().equals(date)) {
                        availableTreatments.add(treatment);
                    }
                }
            }
        }
        return availableTreatments;
    }

    public boolean bookAppointment(Patient patient, Treatment treatment) {
        if (treatment.getStatus() == TreatmentStatus.BOOKED) {
            System.out.println("Appointment already booked.");
            return false;
        }
        treatment.setPatient(patient);
        treatment.setStatus(TreatmentStatus.BOOKED);
        System.out.println(patient.getFullName() + " booked " + treatment.getName() + " with " +
                treatment.getPhysiotherapist().getFullName() + " on " + treatment.getDateTime());
        return true;
    }

    public boolean cancelAppointment(Patient patient, Treatment treatment) {
        if (treatment.getPatient() != null && treatment.getPatient().equals(patient)) {
            treatment.setStatus(TreatmentStatus.CANCELLED);
            treatment.setPatient(null);
            System.out.println(patient.getFullName() + " cancelled their appointment for " + treatment.getName() +
                    " with " + treatment.getPhysiotherapist().getFullName() + " on " + treatment.getDateTime());
            return true;
        }
        return false;
    }

    public boolean changeAppointment(Patient patient, Treatment oldTreatment, Treatment newTreatment) {
        if (cancelAppointment(patient, oldTreatment)) {
            return bookAppointment(patient, newTreatment);
        }
        return false;
    }

    public List<Physiotherapist> getAllPhysiotherapists() {
        return physiotherapists;
    }

    public void generateEndOfTermReport() {
        System.out.println("\n📋 TREATMENT REPORT");

        for (Physiotherapist physio : physiotherapists) {
            System.out.println("\n👨‍⚕️ " + physio.getFullName());
            for (int week = 1; week <= 4; week++) {
                for (Treatment treatment : physio.getAvailableTreatments(week)) {
                    String patientName = treatment.getPatient() != null ? treatment.getPatient().getFullName() : "None";
                    System.out.println("• " + treatment.getName() +
                            " | Time: " + treatment.getDateTime() +
                            " | Patient: " + patientName +
                            " | Status: " + treatment.getStatus());
                }
            }
        }

        System.out.println("\n PHYSIOTHERAPIST PERFORMANCE (by attended sessions):");

        physiotherapists.stream()
                .sorted((a, b) -> {
                    long attendedB = countAttendedTreatments(b);
                    long attendedA = countAttendedTreatments(a);
                    return Long.compare(attendedB, attendedA);
                })
                .forEach(p -> {
                    long attended = countAttendedTreatments(p);
                    System.out.println(p.getFullName() + " - " + attended + " attended appointments");
                });
    }

    private long countAttendedTreatments(Physiotherapist physio) {
        long count = 0;
        for (int week = 1; week <= 4; week++) {
            count += physio.getAvailableTreatments(week).stream()
                    .filter(t -> t.getStatus() == TreatmentStatus.ATTENDED)
                    .count();
        }
        return count;
    }

    private int getWeekOfMonth(LocalDate date) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        return date.get(weekFields.weekOfMonth());
    }
}
