//import javax.swing.*;
//import java.awt.*;
//import java.time.*;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.*;
import java.util.List;

public class Main {

    private static final List<Patient> allPatients = new ArrayList<>();
    private static final List<Physiotherapist> allPhysios = new ArrayList<>();

    private static String formatAppointmentEntry(String patientName, String treatmentName, String physioName, LocalDateTime dateTime, String status) {
        return patientName + " booked " + treatmentName + " with " + physioName + " at " + dateTime + " [" + status + "]";
    }

    public static void main(String[] args) {

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


        for (int i = 1; i <= 10; i++) {
            allPatients.add(new Patient("PT" + i, "Patient " + i, "Address " + i, "0700" + i + "000" + i));
        }

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

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Physio Clinic App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel heading = new JLabel("👋 Welcome! Book your Physio Appointment:");
            heading.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(heading);

            panel.add(new JLabel("Patient Name:"));
            JTextField nameField = new JTextField();
            nameField.setMaximumSize(new Dimension(300, 25));
            panel.add(nameField);

            panel.add(new JLabel("Phone Number:"));
            JTextField phoneField = new JTextField();
            phoneField.setMaximumSize(new Dimension(300, 25));
            panel.add(phoneField);

            panel.add(new JLabel("Filter by Area of Expertise:"));
            JComboBox<String> expertiseDropdown = new JComboBox<>();
            Set<String> allExpertise = new HashSet<>();
            for (Physiotherapist p : bookingSystem.getAllPhysiotherapists()) {
                allExpertise.addAll(p.getAreasOfExpertise());
            }
            expertiseDropdown.addItem("-- Select --");
            allExpertise.forEach(expertiseDropdown::addItem);
            panel.add(expertiseDropdown);

            panel.add(new JLabel("Choose Physiotherapist:"));
            JComboBox<Physiotherapist> physioDropdown = new JComboBox<>();
            bookingSystem.getAllPhysiotherapists().forEach(physioDropdown::addItem);
            panel.add(physioDropdown);

            panel.add(new JLabel("Treatment Name:"));
            JTextField treatmentField = new JTextField();
            treatmentField.setMaximumSize(new Dimension(300, 25));
            panel.add(treatmentField);

            panel.add(new JLabel("Available Treatments for Selected Physio:"));
            DefaultListModel<String> treatmentListModel = new DefaultListModel<>();
            JList<String> treatmentList = new JList<>(treatmentListModel);
            JScrollPane treatmentScroll = new JScrollPane(treatmentList);
            treatmentScroll.setPreferredSize(new Dimension(500, 80));
            panel.add(treatmentScroll);

            panel.add(new JLabel("Select Date:"));
            JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
            dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
            panel.add(dateSpinner);

            panel.add(new JLabel("Select Time:"));
            JSpinner timeSpinner = new JSpinner(new SpinnerDateModel());
            timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
            panel.add(timeSpinner);

            DefaultListModel<String> bookedListModel = new DefaultListModel<>();
            JList<String> bookedList = new JList<>(bookedListModel);
            JScrollPane scrollPane = new JScrollPane(bookedList);
            scrollPane.setPreferredSize(new Dimension(500, 150));

            JButton bookBtn = new JButton("📅 Book Appointment");
            JButton cancelBtn = new JButton("❌ Cancel Appointment");
            JButton rescheduleBtn = new JButton("🔁 Reschedule");
            JButton attendBtn = new JButton("✅ Mark as Attended");
            JButton addPatientBtn = new JButton("➕ Add Patient");
            JButton removePatientBtn = new JButton("➖ Remove Patient");
            JButton reportBtn = new JButton("📄 Generate Report");

            expertiseDropdown.addActionListener(e -> {
                String selectedExpertise = (String) expertiseDropdown.getSelectedItem();
                physioDropdown.removeAllItems();
                for (Physiotherapist p : bookingSystem.getAllPhysiotherapists()) {
                    if (selectedExpertise.equals("-- Select --") || p.getAreasOfExpertise().contains(selectedExpertise)) {
                        physioDropdown.addItem(p);
                    }
                }
            });

            physioDropdown.addActionListener(e -> {
                treatmentListModel.clear();
                Physiotherapist selected = (Physiotherapist) physioDropdown.getSelectedItem();
                if (selected != null) {
                    for (Treatment t : selected.getAvailableTreatments()) {
                        treatmentListModel.addElement(t.getName() + " - " + t.getDateTime().toString());
                    }
                }
            });

            treatmentList.addListSelectionListener(e -> {
                String selected = treatmentList.getSelectedValue();
                if (selected != null) {
                    String[] parts = selected.split(" - ");
                    if (parts.length == 2) {
                        treatmentField.setText(parts[0]);
                        LocalDateTime dt = LocalDateTime.parse(parts[1]);
                        Date date = Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
                        dateSpinner.setValue(date);
                        timeSpinner.setValue(date);
                    }
                }
            });

            // ✅ Updated to include collision check
            bookBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                Physiotherapist selectedPhysio = (Physiotherapist) physioDropdown.getSelectedItem();
                String treatmentName = treatmentField.getText().trim();

                Date selectedDate = ((SpinnerDateModel) dateSpinner.getModel()).getDate();
                Date selectedTime = ((SpinnerDateModel) timeSpinner.getModel()).getDate();

                if (name.isEmpty() || phone.isEmpty() || treatmentName.isEmpty() || selectedPhysio == null) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the fields.", "Missing Info", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDateTime dateTime = LocalDateTime.of(
                        selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        selectedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalTime()
                );

                // 🛑 Check for time conflict
                for (Treatment t : selectedPhysio.getTreatments()) {
                    if (t.getDateTime().equals(dateTime) && t.getPatient() != null) {
                        JOptionPane.showMessageDialog(frame, "This session is already booked!", "Booking Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Patient patient = new Patient(UUID.randomUUID().toString(), name, "", phone);
                Treatment treatment = new Treatment(treatmentName, dateTime, selectedPhysio);
                treatment.setPatient(patient);
                treatment.setStatus(TreatmentStatus.BOOKED);
                selectedPhysio.addTreatment(treatment);

                bookingSystem.bookAppointment(patient, treatment);
                bookedListModel.addElement(formatAppointmentEntry(name, treatmentName, selectedPhysio.getFullName(), dateTime, "Booked"));
                JOptionPane.showMessageDialog(frame, "Appointment successfully booked!");
            });

            cancelBtn.addActionListener(e -> {
                int selectedIndex = bookedList.getSelectedIndex();
                if (selectedIndex == -1) return;
                String entry = bookedListModel.get(selectedIndex);
                bookedListModel.set(selectedIndex, entry.replaceAll("\\[.*?\\]$", "[Cancelled]"));
            });

            rescheduleBtn.addActionListener(e -> {
                int selectedIndex = bookedList.getSelectedIndex();
                if (selectedIndex == -1) return;
                String entry = bookedListModel.get(selectedIndex);
                String[] parts = entry.split(" booked | with | at ");
                if (parts.length >= 4) {
                    nameField.setText(parts[1].trim());
                    treatmentField.setText(parts[2].trim());
                    LocalDateTime dt = LocalDateTime.parse(parts[3].split(" \\[")[0]);
                    Date date = Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
                    dateSpinner.setValue(date);
                    timeSpinner.setValue(date);
                    bookedListModel.remove(selectedIndex);
                }
            });

            attendBtn.addActionListener(e -> {
                int selectedIndex = bookedList.getSelectedIndex();
                if (selectedIndex == -1) return;
                String entry = bookedListModel.get(selectedIndex);
                bookedListModel.set(selectedIndex, entry.replaceAll("\\[.*?\\]$", "[Attended]"));
            });

            addPatientBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                if (!name.isEmpty() && !phone.isEmpty()) {
                    allPatients.add(new Patient(UUID.randomUUID().toString(), name, "", phone));
                    JOptionPane.showMessageDialog(frame, "✅ Patient added.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Enter both name and phone.");
                }
            });

            removePatientBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                boolean removed = allPatients.removeIf(p -> p.getFullName().equalsIgnoreCase(name));
                JOptionPane.showMessageDialog(frame, removed ? "🗑️ Patient removed." : "❌ Patient not found.");
            });

            reportBtn.addActionListener(e -> {
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
            });
            physioDropdown.removeAllItems();
            for (Physiotherapist p : bookingSystem.getAllPhysiotherapists()) {
                physioDropdown.addItem(p);
            }

            panel.add(bookBtn);
            panel.add(cancelBtn);
            panel.add(rescheduleBtn);
            panel.add(attendBtn);
            panel.add(addPatientBtn);
            panel.add(removePatientBtn);
            panel.add(reportBtn);
            panel.add(new JLabel("📋 Booked Appointments:"));
            panel.add(scrollPane);

            frame.getContentPane().add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
