import java.util.*;

public class Main {

    static List<Patient> patients = new ArrayList<>();
    static List<Physiotherapist> physios = new ArrayList<>();
    static List<Appointment> appointments = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int nextPatientId = 16;
    static int nextAppointmentId = 1;

    public static void main(String[] args) {
        setupSampleData();
        mainMenu();
    }

    static void setupSampleData() {
        physios.add(new Physiotherapist("PH1", "Bailey Hayleys", "Green Drive", "0787965342", List.of("Physiotherapy", "Massage")));
        physios.add(new Physiotherapist("PH2", "Ivy Heu", "Oakland Garden", "0736254178", List.of("Rehabilitation", "Sports Therapy")));
        physios.add(new Physiotherapist("PH3", "Rodgers Michaels", "Cavendish Way", "0798253728", List.of("Osteopathy", "Neurology")));
        physios.add(new Physiotherapist("PH4", "Stephen Stone", "Polar Bay", "07849494904", List.of("Joint Mobilization", "Electrical Stimulation")));
        physios.add(new Physiotherapist("PH5", "May Jacksons", "Kingstone Way", "0721345238", List.of("Pool Rehabilitation", "Physiotherapy")));
        physios.add(new Physiotherapist("PH6", "Philips Jayson", "Meadow Avenue", "0704367266", List.of("Hydrotherapy", "Physiotherapy")));
        physios.add(new Physiotherapist("PH7", "Anna Clarkson", "Sunny Road", "0754829154", List.of("Massage", "Sports Therapy")));
        physios.add(new Physiotherapist("PH8", "Daniel Craig", "Hilltop Street", "0748392635", List.of("Osteopathy", "Rehabilitation")));

        for (Physiotherapist physio : physios) {
            for (int week = 1; week <= 4; week++) {
                physio.addToSchedule(week, new TreatmentSlot("Massage", "2025-05-0" + (week + 1) + " 10:00"));
                physio.addToSchedule(week, new TreatmentSlot("Mobilisation", "2025-05-0" + (week + 1) + " 11:00"));
            }
        }


        String[] patientNames = {
                "Sarah Williams", "Jack Snow", "Robinson Grace", "Meadow White",
                "George Olive", "Harry Song", "Spencer Gardens", "Miranda Michaels",
                "Woo Sang", "Peters Johnson", "Mercy Elizabeth", "Wells Heu",
                "Sun Mark", "Dominica Greens", "Charlotte Ivy"
        };

        for (int i = 0; i < patientNames.length; i++) {
            patients.add(new Patient("P" + (i + 1), patientNames[i], "Address " + (i + 1), "0700" + (1000 + i)));
        }
    }

    static void mainMenu() {

        while (true) {
            System.out.println("\n=== Boost Physio Clinic ===");
            System.out.println("1. Book Appointment");
            System.out.println("2. Add Patient");
            System.out.println("3. Remove Patient");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Reschedule Appointment");
            System.out.println("6. Mark as Attended");
            System.out.println("7. Generate Report");
            System.out.println("8. Exit");
            System.out.print("Select: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> bookAppointment();
                case 2 -> addPatient();
                case 3 -> removePatient();
                case 4 -> cancelAppointment();
                case 5 -> rescheduleAppointment();
                case 6 -> markAsAttended();
                case 7 -> generateReport();
                case 8 -> {
                    System.out.println("\n See you!! in your next appointment...");
                    pause();
                }
                default -> System.out.println("Invalid option.");
            }
            pause();
        }
    }

    static void addPatient() {
        System.out.println("\n=== New Patient Registration ===");
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        String id = "P" + (nextPatientId++);
        Patient newPatient = new Patient(id, name, address, phone);
        patients.add(newPatient);

        System.out.println("\n Patient successfully registered!");
        System.out.println(" Patient ID: " + newPatient.getId());
        System.out.println(" Name: " + newPatient.getName());
        System.out.println(" Address: " + newPatient.getAddress());
        System.out.println(" Phone: " + newPatient.getPhone() + "\n");
    }


    static Patient registerNewPatient() {
        System.out.println("\nRegister new patient:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        String id = "P" + (nextPatientId++);
        Patient newPatient = new Patient(id, name, address, phone);
        patients.add(newPatient);
        System.out.println(" New patient registered with ID: " + id);
        return newPatient;
    }

    static void removePatient() {
        System.out.print("Enter patient ID to remove: ");
        String id = scanner.nextLine();
        boolean found = false;

        for (Appointment a : appointments) {
            if (a.getPatient().getId().equalsIgnoreCase(id) && (a.getStatus().equals("BOOKED") || a.getStatus().equals("ATTENDED"))) {
                a.setStatus("CANCELLED");
                a.getSlot().cancel();
                System.out.println("\n Cancelled Appointment:");
                System.out.println("Patient: " + a.getPatient().getName());
                System.out.println("️Physio: " + a.getPhysio().getName());
                System.out.println("Date: " + a.getSlot().getDateTime());
                System.out.println("Status: CANCELLED\n");
                found = true;
            }
        }

        boolean patientRemoved = patients.removeIf(p -> p.getId().equalsIgnoreCase(id));

        if (patientRemoved) {
            System.out.println(" Patient record successfully removed.\n");
        } else if (!found) {
            System.out.println(" No patient found with that ID.\n");
        }
    }


    static void bookAppointment() {
        System.out.println("Available Patients:");
        for (Patient p : patients) {
            System.out.println(p.getId() + " - " + p.getName());
        }

        System.out.print("Enter patient ID or name: ");
        String input = scanner.nextLine().trim();

        Patient patient = patients.stream()
                .filter(p -> p.getId().equalsIgnoreCase(input) || p.getName().equalsIgnoreCase(input))
                .findFirst()
                .orElse(null);

        if (patient == null) {
            System.out.println("Patient not found.");
            System.out.print("Would you like to register as a new patient? (yes/no): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                patient = registerNewPatient();
            } else {
                System.out.println("Returning to main menu...");
                return;
            }
        }

        System.out.println("Book by:\n1. Area of Expertise\n2. Physiotherapist");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 1) {
            List<String> expertiseList = new ArrayList<>();
            Set<String> allExpertise = new TreeSet<>();
            for (Physiotherapist physio : physios) {
                allExpertise.addAll(physio.getExpertise());
            }
            expertiseList.addAll(allExpertise);

            for (int i = 0; i < expertiseList.size(); i++) {
                System.out.println((i + 1) + ". " + expertiseList.get(i));
            }

            System.out.print("Enter expertise number or name: ");
            String expInput = scanner.nextLine().trim();
            String selectedExpertise = null;

            try {
                int expNumber = Integer.parseInt(expInput);
                if (expNumber >= 1 && expNumber <= expertiseList.size()) {
                    selectedExpertise = expertiseList.get(expNumber - 1);
                }
            } catch (NumberFormatException ignored) {
                for (String expertise : expertiseList) {
                    if (expertise.equalsIgnoreCase(expInput)) {
                        selectedExpertise = expertise;
                        break;
                    }
                }
            }

            if (selectedExpertise == null) {
                System.out.println("Invalid expertise input.");
                return;
            }

            String exp = selectedExpertise.toLowerCase();
            List<Physiotherapist> matches = physios.stream()
                    .filter(p -> p.getExpertise().stream().anyMatch(e -> e.toLowerCase().equals(exp)))
                    .toList();

            if (matches.isEmpty()) {
                System.out.println("No physios found for that expertise.");
                return;
            }

            matches.forEach(p -> System.out.println(p.getId() + " - " + p.getName()));
            System.out.print("Enter physio ID: ");
            String physioId = scanner.nextLine();
            Physiotherapist physio = findPhysioById(physioId);
            if (physio != null) selectSlotAndBook(patient, physio);

        } else if (option == 2) {
            System.out.print("Enter physiotherapist ID: ");
            String physioId = scanner.nextLine();
            Physiotherapist physio = findPhysioById(physioId);
            if (physio != null) selectSlotAndBook(patient, physio);
        }
    }

    static void selectSlotAndBook(Patient patient, Physiotherapist physio) {
        for (Map.Entry<Integer, List<TreatmentSlot>> entry : physio.getSchedule().entrySet()) {
            System.out.println("Week " + entry.getKey() + ":");
            for (int i = 0; i < entry.getValue().size(); i++) {
                TreatmentSlot slot = entry.getValue().get(i);
                System.out.println("  [" + entry.getKey() + "-" + i + "] " + slot);
            }
        }

        System.out.print("Enter week-slot index (e.g., 2-0): ");
        String[] input = scanner.nextLine().split("-");
        int week = Integer.parseInt(input[0]);
        int index = Integer.parseInt(input[1]);
        TreatmentSlot slot = physio.getSchedule().get(week).get(index);

        if (slot.isBooked()) {
            System.out.println("This slot is already booked. Please choose another slot.");
            return;
        }


        boolean alreadyBooked = appointments.stream()
                .anyMatch(a -> a.getPatient().getId().equals(patient.getId())
                        && a.getSlot().getDateTime().equals(slot.getDateTime())
                        && (a.getStatus().equals("BOOKED") || a.getStatus().equals("ATTENDED")));

        if (alreadyBooked) {
            System.out.println("\n You already have an appointment booked at this time!");
            return;
        }

        String appointmentId = "A" + (nextAppointmentId++);
        slot.book(appointmentId);
        Appointment appt = new Appointment(appointmentId, patient, physio, slot, "BOOKED");
        appointments.add(appt);

        System.out.println("\n Appointment confirmed: " + appointmentId);
        System.out.println("Patient: " + patient.getName());
        System.out.println("Physio: " + physio.getName());
        System.out.println("Date: " + slot.getDateTime());
        System.out.println("Status: BOOKED");
    }

    static void cancelAppointment() {
        System.out.print("Enter appointment ID to cancel: ");
        String id = scanner.nextLine();
        boolean found = false;

        for (Appointment a : appointments) {
            if (a.getId().equalsIgnoreCase(id)) {
                a.setStatus("CANCELLED");
                a.getSlot().cancel();

                System.out.println("\n Appointment cancelled:");
                System.out.println(" Appointment ID: " + a.getId());
                System.out.println(" Patient: " + a.getPatient().getName());
                System.out.println(" Physio: " + a.getPhysio().getName());
                System.out.println("Date: " + a.getSlot().getDateTime());
                System.out.println("Status: CANCELLED\n");

                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Appointment ID not found.\n");
        }
    }


    static void rescheduleAppointment() {
        System.out.print("Enter appointment ID to reschedule: ");
        String id = scanner.nextLine();
        Appointment existing = appointments.stream()
                .filter(a -> a.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            System.out.println("Appointment not found.");
            return;
        }

        existing.setStatus("CANCELLED");
        existing.getSlot().cancel();
        System.out.println("Appointment cancelled. Now rebooking...");
        selectSlotAndBook(existing.getPatient(), existing.getPhysio());
    }

    static void markAsAttended() {
        System.out.print("Enter appointment ID to mark as attended: ");
        String id = scanner.nextLine();
        for (Appointment a : appointments) {
            if (a.getId().equalsIgnoreCase(id)) {
                a.setStatus("ATTENDED");
                System.out.println("\n---------------------------------------");
                System.out.println("Appointment ID: " + a.getId());
                System.out.println("Patient: " + a.getPatient().getName());
                System.out.println("Physio: " + a.getPhysio().getName());
                System.out.println("Date: " + a.getSlot().getDateTime());
                System.out.println("Status: MARKED AS ATTENDED");
                System.out.println("---------------------------------------\n");
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

    static void generateReport() {
        System.out.println("=== Appointment Report by Physiotherapist ===");
        for (Physiotherapist physio : physios) {
            System.out.println("\n" + physio.getName() + ":");
            for (Appointment a : appointments) {
                if (a.getPhysio().getId().equals(physio.getId())) {
                    System.out.println(" - " + a.getSlot().getDateTime() + " | " + a.getSlot().getTreatmentName()
                            + " | " + a.getPatient().getName() + " | " + a.getStatus());
                }
            }
        }
    }

    static Physiotherapist findPhysioById(String id) {
        return physios.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    static void pause() {
        System.out.print("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }
}
