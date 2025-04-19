
import java.time.LocalDateTime;

public class Treatment {
    private String treatmentName;
    private LocalDateTime dateTime;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private TreatmentStatus status;
    private Treatment treatment;

    public Treatment(String treatmentName, LocalDateTime dateTime, Physiotherapist physiotherapist) {
        this.treatmentName = treatmentName;
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
        this.status = TreatmentStatus.BOOKED;
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

    public Treatment getTreatment() {
        return treatment;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public TreatmentStatus getStatus() {
        return status;
    }

    public void setStatus(TreatmentStatus status) {
        this.status = status;
    }
    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    @Override
    public String toString() {
        return treatmentName + " on " + dateTime.toString() + " with " + physiotherapist.getFullName();
    }
}
