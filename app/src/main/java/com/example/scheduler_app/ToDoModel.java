package com.example.scheduler_app;

public class ToDoModel {
    private String task;
    private boolean complete;

    public ToDoModel(String task, boolean complete) {
        this.task = task;
        this.complete = complete;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
