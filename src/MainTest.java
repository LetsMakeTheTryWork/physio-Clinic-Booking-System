import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTest {

    @BeforeEach
    void setup() {
        Main.patients.clear();
        Main.physios.clear();
        Main.appointments.clear();
        Main.nextPatientId = 16;
        Main.nextAppointmentId = 1;
        Main.setupSampleData();
    }

    @Test
    void testAddPatient() {
        int before = Main.patients.size();
        Main.patients.add(new Patient("P100", "Test Patient", "Test Address", 700123456));
        assertEquals(before + 1, Main.patients.size());
    }

    @Test
    void testRemovePatient() {
        String idToRemove = Main.patients.get(0).getId();
        Main.patients.removeIf(p -> p.getId().equalsIgnoreCase(idToRemove));
        assertFalse(Main.patients.stream().anyMatch(p -> p.getId().equals(idToRemove)));
    }

    @Test
    void testBookAppointment() {
        Patient patient = Main.patients.get(0);
        Physiotherapist physio = Main.physios.get(0);
        TreatmentSlot slot = physio.getSchedule().get(1).get(0);

        String appointmentId = "A" + Main.nextAppointmentId++;
        slot.book(appointmentId);
        Appointment appt = new Appointment(appointmentId, patient, physio, slot, "BOOKED");
        Main.appointments.add(appt);

        assertTrue(Main.appointments.contains(appt));
        assertEquals("BOOKED", appt.getStatus());
    }

    @Test
    void testCancelAppointment() {
        Patient patient = Main.patients.get(0);
        Physiotherapist physio = Main.physios.get(0);
        TreatmentSlot slot = physio.getSchedule().get(1).get(0);

        String appointmentId = "A" + Main.nextAppointmentId++;
        slot.book(appointmentId);
        Appointment appt = new Appointment(appointmentId, patient, physio, slot, "BOOKED");
        Main.appointments.add(appt);

        appt.setStatus("CANCELLED");
        slot.cancel();
        assertEquals("CANCELLED", appt.getStatus());
        assertFalse(slot.isBooked());
    }

    @Test
    void testRescheduleAppointment() {
        Patient patient = Main.patients.get(0);
        Physiotherapist physio = Main.physios.get(0);
        TreatmentSlot slot = physio.getSchedule().get(1).get(0);

        String appointmentId = "A" + Main.nextAppointmentId++;
        slot.book(appointmentId);
        Appointment appt = new Appointment(appointmentId, patient, physio, slot, "BOOKED");
        Main.appointments.add(appt);

        appt.setStatus("CANCELLED");
        slot.cancel();

        TreatmentSlot newSlot = physio.getSchedule().get(1).get(1);
        String newAppointmentId = "A" + Main.nextAppointmentId++;
        newSlot.book(newAppointmentId);
        Appointment newAppt = new Appointment(newAppointmentId, patient, physio, newSlot, "BOOKED");
        Main.appointments.add(newAppt);

        assertEquals("CANCELLED", appt.getStatus());
        assertTrue(newSlot.isBooked());
        assertEquals("BOOKED", newAppt.getStatus());
    }

    @Test
    void testMarkAsAttended() {
        Patient patient = Main.patients.get(0);
        Physiotherapist physio = Main.physios.get(0);
        TreatmentSlot slot = physio.getSchedule().get(1).get(0);

        String appointmentId = "A" + Main.nextAppointmentId++;
        slot.book(appointmentId);
        Appointment appt = new Appointment(appointmentId, patient, physio, slot, "BOOKED");
        Main.appointments.add(appt);

        appt.setStatus("ATTENDED");
        assertEquals("ATTENDED", appt.getStatus());
    }

    @Test
    void testGenerateReport() {
        assertDoesNotThrow(() -> Main.generateReport());
    }
}
