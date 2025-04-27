public class TreatmentSlot {
    private String treatmentName;
    private String dateTime;
    private boolean isBooked;
    private String appointmentId;

    public TreatmentSlot(String treatmentName, String dateTime) {
        this.treatmentName = treatmentName;
        this.dateTime = dateTime;
        this.isBooked = false;
        this.appointmentId = null;
    }

    public String getTreatmentName() { return treatmentName; }
    public String getDateTime() { return dateTime; }
    public boolean isBooked() { return isBooked; }
    public String getAppointmentId() { return appointmentId; }

    public void book(String appointmentId) {
        this.isBooked = true;
        this.appointmentId = appointmentId;
    }

    public void cancel() {
        this.isBooked = false;
        this.appointmentId = null;
    }

    @Override
    public String toString() {
        return treatmentName + " at " + dateTime + (isBooked ? " [BOOKED]" : " [AVAILABLE]");
    }
}
