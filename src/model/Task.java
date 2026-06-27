package model;

public class Task {
    private String title;
    private String priority;
    private String deadline;

    public Task(String title, String priority, String deadline) {
        this.title = title;
        this.priority = priority;
        this.deadline = deadline;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    public String getDeadline() {
        return deadline;
    }
}
