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

        allPhysios.addAll(List.of(
                new Physiotherapist("001", "Sandy Stephens", "14 Joyworld", "0765487998",
                        List.of("Physiotherapy", "Rehabilitation"),
                        List.of(new WorkingTime(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)))),

                new Physiotherapist("002", "Mark Hillary", "45 Meadow Way", "0787654321",
                        List.of("Osteopathy"),
                        List.of(new WorkingTime(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(16, 0)))),

                new Physiotherapist("003", "Sarah Roberts", "40 Spring Avenue", "0745965334",
                        List.of("Neurology"),
                        List.of(new WorkingTime(DayOfWeek.WEDNESDAY, LocalTime.of(8, 0), LocalTime.of(14, 0)))),

                new Physiotherapist("004", "Blake Ivy", "100 GreenDrive", "0724567989",
                        List.of("Sports Therapy"),
                        List.of(new WorkingTime(DayOfWeek.THURSDAY, LocalTime.of(11, 0), LocalTime.of(18, 0)))),

                new Physiotherapist("005", "Evans Gray", "15 Cavendish Street", "0793547689",
                        List.of("Pediatric Physiotherapy"),
                        List.of(new WorkingTime(DayOfWeek.FRIDAY, LocalTime.of(9, 0), LocalTime.of(15, 0))))));

        for (int i = 1; i <= 10; i++) {
            allPatients.add(new Patient("PT" + i, "Patient " + i, "Address " + i, "0700" + i + "000" + i));
        }

        BookingService bookingSystem = new BookingService(allPhysios, allPatients);

        LocalDate startDate = LocalDate.now();
        for (Physiotherapist physio : allPhysios) {
            for (int i = 0; i < 28; i++) {
                LocalDateTime dt = LocalDateTime.of(startDate.plusDays(i), LocalTime.of(9 + (i % 6), 0));
                physio.addTreatment(new Treatment("Session " + (i + 1), dt, physio));
            }
        }

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

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> bookAppointment(bookingSystem);
                    case 2 -> cancelAppointment();
                    case 3 -> bookingSystem.rescheduleAppointment();
                    case 4 -> markAsAttended();
                    case 5 -> addPatient();
                    case 6 -> removePatient();
                    case 7 -> generateReport();
                    case 8 -> {
                        System.out.println("Exiting the System...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    private static void bookAppointment(BookingService bookingSystem) {
        try {
            System.out.println("Please enter the patient's full name:");
            String name = scanner.nextLine();

            System.out.println("Now, enter the patient's phone number (e.g., 0700123456):");
            String phone = scanner.nextLine();

            System.out.println("Please enter the patient's address:");
            String address = scanner.nextLine();

            System.out.println("How would you like to view available appointments?");
            System.out.println("1. By Expertise (e.g., Physiotherapy, Rehabilitation)");
            System.out.println("2. By Physiotherapist's Name");
            System.out.print("Choose an option: ");
            int viewOption = Integer.parseInt(scanner.nextLine());

            List<Treatment> availableTreatments = new ArrayList<>();

            if (viewOption == 1) {

                System.out.println("Please enter the expertise (e.g., Physiotherapy, Rehabilitation):");
                String expertise = scanner.nextLine();

                for (Physiotherapist physio : allPhysios) {
                    for (Treatment t : physio.getTreatments()) {
                        if (t.getTreatmentName().equalsIgnoreCase(expertise) && t.getPatient() == null) {
                            availableTreatments.add(t);
                        }
                    }
                }
            } else if (viewOption == 2) {

                System.out.println("Please enter the physiotherapist's name:");
                String physioName = scanner.nextLine();

                for (Physiotherapist physio : allPhysios) {
                    if (physio.getFullName().equalsIgnoreCase(physioName)) {
                        for (Treatment t : physio.getTreatments()) {
                            if (t.getPatient() == null) {
                                availableTreatments.add(t);
                            }
                        }
                    }
                }
            } else {
                System.out.println("Invalid option. Please try again.");
                return;
            }


            if (availableTreatments.isEmpty()) {
                System.out.println("No available appointments found.");
                return;
            }

            System.out.println("Available appointments:");
            for (int i = 0; i < availableTreatments.size(); i++) {
                Treatment t = availableTreatments.get(i);
                System.out.println((i + 1) + ". " + t.getTreatmentName() + " with " + t.getPhysiotherapist().getFullName() +
                        " on " + t.getDateTime() + " | Status: " + (t.getPatient() == null ? "Available" : "Booked"));
            }

            System.out.println("Choose an appointment by number:");
            int treatmentChoice = Integer.parseInt(scanner.nextLine()) - 1;

            if (treatmentChoice < 0 || treatmentChoice >= availableTreatments.size()) {
                System.out.println("Invalid choice. Please try again.");
                return;
            }

            Treatment selectedTreatment = availableTreatments.get(treatmentChoice);

            for (Physiotherapist physio : allPhysios) {
                for (Treatment t : physio.getTreatments()) {
                    if (t.getPatient() != null && t.getPatient().getFullName().equalsIgnoreCase(name) &&
                            t.getDateTime().equals(selectedTreatment.getDateTime())) {
                        System.out.println("❌ This patient already has an appointment at this time. Please choose another slot.");
                        return;
                    }
                }
            }

            Patient patient = allPatients.stream()
                    .filter(p -> p.getFullName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseGet(() -> {
                        Patient newP = new Patient(UUID.randomUUID().toString(), name, address, phone);
                        allPatients.add(newP);
                        return newP;
                    });

            selectedTreatment.setPatient(patient);
            selectedTreatment.setStatus(TreatmentStatus.BOOKED);

            selectedTreatment.getPhysiotherapist().addTreatment(selectedTreatment);
            bookingSystem.bookAppointment(patient, selectedTreatment);

            System.out.println(" Appointment booked successfully!");
            System.out.println("Patient: " + patient.getFullName() + " | Treatment: " + selectedTreatment.getTreatmentName() +
                    " | Physiotherapist: " + selectedTreatment.getPhysiotherapist().getFullName());
            System.out.println("Date: " + selectedTreatment.getDateTime());
            System.out.println("Status: " + selectedTreatment.getStatus());

        } catch (Exception e) {
            System.out.println("Oops! Something went wrong. Please try again.");
        }
    }

    private static void cancelAppointment() {
        System.out.println("Enter Patient Name to cancel appointment:");
        String name = scanner.nextLine();
        for (Physiotherapist physio : allPhysios) {
            for (Treatment t : physio.getTreatments()) {
                if (t.getPatient() != null && t.getPatient().getFullName().equalsIgnoreCase(name)) {
                    t.setStatus(TreatmentStatus.CANCELLED);
                    t.setPatient(null);
                    System.out.println("Appointment cancelled for: " + name);
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

        for (Physiotherapist physio : allPhysios) {
            physio.getTreatments().removeIf(t -> t.getPatient() != null && t.getPatient().getFullName().equalsIgnoreCase(name));
        }

        if (removed) {
            System.out.println("Patient removed and appointments cleared.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void generateReport() {
        System.out.println("Generating Report...");

        for (Physiotherapist physio : allPhysios) {
            System.out.println("\nPhysiotherapist: " + physio.getFullName());
            for (Treatment t : physio.getTreatments()) {
                String status = (t.getPatient() != null ? t.getPatient().getFullName() : "No patient assigned") + " - " + t.getStatus();
                System.out.println(t.getDateTime() + " | " + t.getTreatmentName() + " | Status: " + status);
            }
        }
    }
}
