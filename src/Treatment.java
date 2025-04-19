import java.time.LocalDateTime;
import java.util.UUID;

public class Treatment {
    private String treatmentName;
    private LocalDateTime dateTime;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private TreatmentStatus status;
    private String bookingId;

    public Treatment(String treatmentName, LocalDateTime dateTime, Physiotherapist physiotherapist) {
        this.treatmentName = treatmentName;
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
        this.status = TreatmentStatus.BOOKED;
        this.bookingId = UUID.randomUUID().toString();
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public Patient getPatient() {
        return patient;
    }

    public TreatmentStatus getStatus() {
        return status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setStatus(TreatmentStatus status) {
        this.status = status;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public String toString() {
        return treatmentName + " on " + dateTime.toString() +
                " with " + physiotherapist.getFullName() +
                " | Status: " + status +
                (patient != null ? " | Booked by: " + patient.getFullName() : "") +
                " | Booking ID: " + bookingId;
    }
}
