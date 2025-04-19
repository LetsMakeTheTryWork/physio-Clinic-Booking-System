import java.time.DayOfWeek;
import java.time.LocalTime;

public class WorkingTime {
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkingTime(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

}
