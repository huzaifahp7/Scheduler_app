package com.example.scheduler_app;

public class CourseModel {
    private String course;
    private String instructor;
    private String time;
    private String location;

    public CourseModel(String course, String instructor, String time, String location) {
        this.course = course;
        this.instructor = instructor;
        this.time = time;
        this.location = location;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
