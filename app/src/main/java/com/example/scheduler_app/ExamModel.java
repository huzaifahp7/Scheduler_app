package com.example.scheduler_app;

import java.io.Serializable;

public class ExamModel implements Serializable {
    private String date;
    private String time;
    private String location;
    private String title;

    public ExamModel(String title, String date, String time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
        this.title = title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getTitle() {
        return title;
    }
}

