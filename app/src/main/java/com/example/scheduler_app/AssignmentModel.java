package com.example.scheduler_app;

public class AssignmentModel {
    private String title;
    private String date;
    private String subject;

    public AssignmentModel(String title, String date, String subject) {
        this.title = title;
        this.date = date;
        this.subject = subject;
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

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
