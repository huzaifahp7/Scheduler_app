package com.example.scheduler_app;

public class AssignmentModel {
    private String title;
    private String date;
    private String subject;
    private String time;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AssignmentModel(String title, String subject, String date, String time, int id) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.subject = subject;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
