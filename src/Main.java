import java.time.*;
import java.util.*;

public class Main {

    private static final List<Patient> allPatients = new ArrayList<>();
    private static final List<Physiotherapist> allPhysios = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static String formatAppointmentEntry(String patientName, String treatmentName, String physioName, LocalDateTime dateTime, String status) {
        return patientName + " booked " + treatmentName + " with " + physioName + " at " + dateTime + " [" + status + "]";
    }

    public static void main(String[] args) {
        // Initialize Physiotherapists
        Physiotherapist Sandy = new Physiotherapist("001", "Sandy Stephens", "14 Joyworld", "0765487998",
                Arrays.asList("Physiotherapy", "Rehabilitation"),
                Arrays.asList(new WorkingTime(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0))));

        Physiotherapist Mark = new Physiotherapist("002", "Mark Hillary", "45 Meadow Way", "0787654321",
                Arrays.asList("Osteopathy"),
                Arrays.asList(new WorkingTime(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(16, 0))));

        Physiotherapist Sarah = new Physiotherapist("003", "Sarah Roberts", "40 Spring Avenue", "0745965334",
                Arrays.asList("Neurology"),
                Arrays.asList(new WorkingTime(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(14, 0))));

        Physiotherapist Ivy = new Physiotherapist("004", "Blake Ivy", "100 GreenDrive", "0724567989",
                Arrays.asList("Sports Therapy"),
                Arrays.asList(new WorkingTime(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(18, 0))));

        Physiotherapist Gray = new Physiotherapist("005", "Evans Gray", "15 Cavendish Street", "0793547689",
                Arrays.asList("Pediatric Physiotherapy"),
                Arrays.asList(new WorkingTime(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(15, 0))));

        allPhysios.addAll(List.of(Sandy, Mark, Sarah, Ivy, Gray));

        // Create dummy patients
        for (int i = 1; i <= 10; i++) {
            allPatients.add(new Patient("PT" + i, "Patient " + i, "Address " + i, "0700" + i + "000" + i));
        }

        // Assign default treatments
        LocalDate startDate = LocalDate.now();
        for (Physiotherapist physio : allPhysios) {
            for (int i = 0; i < 28; i++) {
                LocalDateTime dt = LocalDateTime.of(startDate.plusDays(i), LocalTime.of(9 + (i % 6), 0));
                Treatment t = new Treatment("Session " + (i + 1), dt, physio);
                physio.addTreatment(t);
            }
        }

        BookingService bookingSystem = new BookingService();
        allPhysios.forEach(bookingSystem::addPhysiotherapist);

        // Console menu
        while (true) {
            System.out.println("\n====== Physio Clinic App ======");
            System.out.println("1. Book Appointment");
            System.out.println("2. Cancel Appointment");
            System.out.println("3. Reschedule Appointment");
            System.out.println("4. Mark as Attended");
            System.out.println("5. Add Patient");
            System.out.println("6. Remove Patient");
            System.out.println("7. Generate Report");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> bookAppointment(bookingSystem);
                case 2 -> cancelAppointment();
                case 3 -> rescheduleAppointment();
                case 4 -> markAsAttended();
                case 5 -> addPatient();
                case 6 -> removePatient();
                case 7 -> generateReport();
                case 8 -> {
                    System.out.println("Exiting the program...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void bookAppointment(BookingService bookingSystem) {
        System.out.println("Enter Patient Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Patient Phone Number:");
        String phone = scanner.nextLine();

        System.out.println("Select Physiotherapist by Number:");
        for (int i = 0; i < allPhysios.size(); i++) {
            System.out.println((i + 1) + ". " + allPhysios.get(i).getFullName());
        }
        int physioChoice = scanner.nextInt() - 1;
        scanner.nextLine();  // Consume newline
        Physiotherapist selectedPhysio = allPhysios.get(physioChoice);

        System.out.println("Enter Treatment Name:");
        String treatmentName = scanner.nextLine();

        System.out.println("Enter Date (yyyy-MM-dd):");
        String dateStr = scanner.nextLine();
        System.out.println("Enter Time (HH:mm):");
        String timeStr = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.parse(dateStr + "T" + timeStr);

        for (Treatment t : selectedPhysio.getTreatments()) {
            if (t.getDateTime().equals(dateTime) && t.getPatient() != null) {
                System.out.println("This session is already booked!");
                return;
            }
        }

        Patient patient = allPatients.stream()
                .filter(p -> p.getFullName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Patient newP = new Patient(UUID.randomUUID().toString(), name, "", phone);
                    allPatients.add(newP);
                    return newP;
                });

        Treatment treatment = new Treatment(treatmentName, dateTime, selectedPhysio);
        treatment.setPatient(patient);
        treatment.setStatus(TreatmentStatus.BOOKED);
        selectedPhysio.addTreatment(treatment);

        bookingSystem.bookAppointment(patient, treatment);
        System.out.println(formatAppointmentEntry(name, treatmentName, selectedPhysio.getFullName(), dateTime, "Booked"));
    }

    private static void cancelAppointment() {
        System.out.println("Enter Patient Name to cancel appointment:");
        String name = scanner.nextLine();
        for (Physiotherapist physio : allPhysios) {
            for (Treatment t : physio.getTreatments()) {
                if (t.getPatient() != null && t.getPatient().getFullName().equalsIgnoreCase(name)) {
                    t.setPatient(null);
                    t.setStatus(TreatmentStatus.BOOKED);
                    System.out.println("Appointment cancelled for: " + name);
                    return;
                }
            }
        }
        System.out.println("No appointment found for this patient.");
    }

    private static void rescheduleAppointment() {
        System.out.println("Enter Patient Name to reschedule appointment:");
        String name = scanner.nextLine();
        for (Physiotherapist physio : allPhysios) {
            for (Treatment t : physio.getTreatments()) {
                if (t.getPatient() != null && t.getPatient().getFullName().equalsIgnoreCase(name)) {
                    System.out.println("Enter new date (yyyy-MM-dd):");
                    String dateStr = scanner.nextLine();
                    System.out.println("Enter new time (HH:mm):");
                    String timeStr = scanner.nextLine();
                    LocalDateTime newDateTime = LocalDateTime.parse(dateStr + "T" + timeStr);

                    for (Treatment other : physio.getTreatments()) {
                        if (other.getDateTime().equals(newDateTime) && other.getPatient() != null) {
                            System.out.println("New slot is already booked.");
                            return;
                        }
                    }

                    t.setDateTime(newDateTime);
                    System.out.println("Appointment rescheduled for: " + name);
                    return;
                }
            }
        }
        System.out.println("No appointment found for this patient.");
    }

    private static void markAsAttended() {
        System.out.println("Enter Patient Name to mark appointment as attended:");
        String name = scanner.nextLine();
        for (Physiotherapist physio : allPhysios) {
            for (Treatment t : physio.getTreatments()) {
                if (t.getPatient() != null &&
                        t.getPatient().getFullName().equalsIgnoreCase(name) &&
                        t.getStatus() == TreatmentStatus.BOOKED) {

                    t.setStatus(TreatmentStatus.ATTENDED);
                    physio.incrementAttendedCount();
                    System.out.println("Marked as attended for: " + name);
                    return;
                }
            }
        }
        System.out.println("No booked appointment found for this patient.");
    }

    private static void addPatient() {
        System.out.println("Enter New Patient Name:");
        String name = scanner.nextLine();
        System.out.println("Enter Patient Phone Number:");
        String phone = scanner.nextLine();
        Patient newPatient = new Patient(UUID.randomUUID().toString(), name, "", phone);
        allPatients.add(newPatient);
        System.out.println("Patient added successfully.");
    }

    private static void removePatient() {
        System.out.println("Enter Patient Name to remove:");
        String name = scanner.nextLine();
        boolean removed = allPatients.removeIf(p -> p.getFullName().equalsIgnoreCase(name));
        if (removed) {
            System.out.println("Patient removed successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void generateReport() {
        System.out.println("\n=== End of Term Report ===");
        for (Physiotherapist physio : allPhysios) {
            System.out.println("\nPhysiotherapist: " + physio.getFullName());
            for (Treatment t : physio.getTreatments()) {
                if (t.getPatient() != null) {
                    System.out.println("  Treatment: " + t.getName() + ", Patient: " + t.getPatient().getFullName() +
                            ", Time: " + t.getDateTime() + ", Status: " + t.getStatus());
                }
            }
        }

        System.out.println("\n=== Physiotherapist Ranking by Attended Appointments ===");
        allPhysios.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getAttendedCount(), p1.getAttendedCount()))
                .forEach(p -> System.out.println(p.getFullName() + " - Attended: " + p.getAttendedCount()));
    }
}
