package com.example.scheduler_app;

public class ToDoModel {
    private String name;
    private String time;
    private String date;
    private int id;

    public ToDoModel(String name, String time, String date, int id) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
