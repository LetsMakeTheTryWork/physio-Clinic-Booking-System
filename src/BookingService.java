import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class BookingService {
    private final List<Physiotherapist> physiotherapists;
    private final List<Patient> patients;
    private final Scanner scanner = new Scanner(System.in);

    public BookingService(List<Physiotherapist> physiotherapists, List<Patient> patients) {
        this.physiotherapists = physiotherapists;
        this.patients = patients;
    }

    public void bookAppointment(Patient patient, Treatment treatment) {
        treatment.setPatient(patient);
        System.out.println("Booking appointment for patient: " + patient.getFullName() + " with physiotherapist: " + treatment.getPhysiotherapist().getFullName());
    }


    public void rescheduleAppointment() {
        try {

            System.out.println("Enter Patient Name to reschedule appointment:");
            String patientName = scanner.nextLine();
            Patient patient = getPatientByName(patientName);

            if (patient == null) {
                System.out.println("No appointment found for this patient.");
                return;
            }

            System.out.println("Enter new date (yyyy-MM-dd):");
            String newDateStr = scanner.nextLine();
            LocalDate newDate = parseDate(newDateStr);
            if (newDate == null) {
                System.out.println("Invalid date format. Please enter a valid date in yyyy-MM-dd format.");
                return;
            }

            System.out.println("Enter new time (HH:mm):");
            String newTimeStr = scanner.nextLine();
            LocalTime newTime = parseTime(newTimeStr);
            if (newTime == null) {
                System.out.println("Invalid time format. Please enter a valid time in HH:mm format.");
                return;
            }

            LocalDateTime newAppointmentDateTime = LocalDateTime.of(newDate, newTime);

            boolean conflictFound = false;
            for (Physiotherapist physio : physiotherapists) {
                for (Treatment treatment : physio.getTreatments()) {
                    if (treatment.getDateTime().equals(newAppointmentDateTime) && treatment.getPatient() != null) {
                        System.out.println("Sorry, the new time slot (" + newAppointmentDateTime + ") is already booked with " +
                                physio.getFullName() + ". Please select a different time.");
                        conflictFound = true;
                        break;
                    }
                }
                if (conflictFound) break;
            }

            if (!conflictFound) {
                for (Physiotherapist physio : physiotherapists) {
                    for (Treatment treatment : physio.getTreatments()) {
                        if (treatment.getPatient().equals(patient)) {
                            treatment.setDateTime(newAppointmentDateTime);
                            System.out.println("Your appointment has been rescheduled to " + newAppointmentDateTime);
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while rescheduling. Please try again.");
        }
    }

    private Patient getPatientByName(String name) {
        for (Patient patient : patients) {
            if (patient.getFullName().equalsIgnoreCase(name)) {
                return patient;
            }
        }
        return null;
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalTime parseTime(String timeStr) {
        try {
            return LocalTime.parse(timeStr);
        } catch (Exception e1) {
            try {
                return LocalTime.of(Integer.parseInt(timeStr.substring(0, 2)), Integer.parseInt(timeStr.substring(2, 4)));
            } catch (Exception e2) {
                return null;
            }
        }
    }
}
