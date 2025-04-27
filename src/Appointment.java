public class Appointment {
    private String id;
    private Patient patient;
    private Physiotherapist physio;
    private TreatmentSlot slot;
    private String status;

    public Appointment(String id, Patient patient, Physiotherapist physio, TreatmentSlot slot, String status) {
        this.id = id;
        this.patient = patient;
        this.physio = physio;
        this.slot = slot;
        this.status = status;
    }

    public String getId() { return id; }
    public Patient getPatient() { return patient; }
    public Physiotherapist getPhysio() { return physio; }
    public TreatmentSlot getSlot() { return slot; }
    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "ID='" + id + '\'' +
                ", Patient=" + patient.getName() +
                ", Physio=" + physio.getName() +
                ", Slot=" + slot +
                ", Status='" + status + '\'' +
                '}';
    }
}
