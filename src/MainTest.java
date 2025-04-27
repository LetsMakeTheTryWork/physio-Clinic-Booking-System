import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;


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
        Main.patients.add(new Patient("P100", "Test Patient", "Test Address", "0700123456"));
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
        TreatmentSlot oldSlot = physio.getSchedule().get(1).get(0);

        String oldAppointmentId = "A" + Main.nextAppointmentId++;
        oldSlot.book(oldAppointmentId);
        Appointment oldAppointment = new Appointment(oldAppointmentId, patient, physio, oldSlot, "BOOKED");
        Main.appointments.add(oldAppointment);


        oldAppointment.setStatus("CANCELLED");
        oldSlot.cancel();


        TreatmentSlot newSlot = physio.getSchedule().get(1).get(1);
        String newAppointmentId = "A" + Main.nextAppointmentId++;
        newSlot.book(newAppointmentId);
        Appointment newAppointment = new Appointment(newAppointmentId, patient, physio, newSlot, "BOOKED");
        Main.appointments.add(newAppointment);

        assertEquals("CANCELLED", oldAppointment.getStatus());
        assertTrue(newSlot.isBooked());
        assertEquals("BOOKED", newAppointment.getStatus());
    }

    @Test
    void testMarkAsAttendedWithRealData() {
        Patient patient = new Patient("P996", "Attend Tester", "Attend Lane", "0711223344");
        Physiotherapist physio = new Physiotherapist("PH11", "Attend Physio", "Attend Street", "0777555444", List.of("Rehabilitation"));
        TreatmentSlot slot = new TreatmentSlot("Rehab", "2025-06-03 09:00");
        physio.addToSchedule(1, slot);

        Main.patients.add(patient);
        Main.physios.add(physio);

        String appointmentId = "A997";
        slot.book(appointmentId);
        Appointment appointment = new Appointment(appointmentId, patient, physio, slot, "BOOKED");
        Main.appointments.add(appointment);

        appointment.setStatus("ATTENDED");

        assertEquals("ATTENDED", appointment.getStatus());
    }

    @Test
    void testRealRescheduleAppointment() {
        Patient patient = new Patient("P995", "Reschedule Tester", "Reschedule Road", "0722334455");
        Physiotherapist physio = new Physiotherapist("PH12", "Reschedule Physio", "Reschedule Court", "0766778899", List.of("Massage"));
        TreatmentSlot slot1 = new TreatmentSlot("Massage", "2025-06-04 12:00");
        TreatmentSlot slot2 = new TreatmentSlot("Massage", "2025-06-05 13:00");
        physio.addToSchedule(1, slot1);
        physio.addToSchedule(2, slot2);

        Main.patients.add(patient);
        Main.physios.add(physio);

        String appointmentId = "A996";
        slot1.book(appointmentId);
        Appointment appointment = new Appointment(appointmentId, patient, physio, slot1, "BOOKED");
        Main.appointments.add(appointment);

        appointment.setStatus("CANCELLED");
        slot1.cancel();
        slot2.book("A996-NEW");
        Appointment newAppointment = new Appointment("A996-NEW", patient, physio, slot2, "BOOKED");
        Main.appointments.add(newAppointment);

        assertEquals("CANCELLED", appointment.getStatus());
        assertTrue(slot2.isBooked());
        assertEquals("BOOKED", newAppointment.getStatus());
    }

    @Test
    void testGenerateReportDoesNotThrow() {
        assertDoesNotThrow(() -> Main.generateReport());
    }
}
