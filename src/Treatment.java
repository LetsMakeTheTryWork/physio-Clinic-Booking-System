import java.time.LocalDateTime;

public class Treatment {
    private String name;
    private LocalDateTime dateTime;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private TreatmentStatus status;

    public Treatment(String name, LocalDateTime dateTime, Physiotherapist physiotherapist) {
        this.name = name;
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
        this.status = TreatmentStatus.BOOKED;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return name + " on " + dateTime.toString() + " with " + physiotherapist.getFullName();
    }
}
