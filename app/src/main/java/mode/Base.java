package mode;

public class Base {
    private String Status;
    private String Time;

    public Base() {
    }

    public Base(String status, String time) {
        Status = status;
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
