import java.time.DayOfWeek;
import java.time.LocalTime;

public class WorkingTime {
    DayOfWeek day;
    LocalTime startTime;
    LocalTime endTime;

    public WorkingTime(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
