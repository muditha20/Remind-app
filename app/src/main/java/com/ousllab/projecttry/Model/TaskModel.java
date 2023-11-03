package com.ousllab.projecttry.Model;

public class TaskModel {

    private String taskName;
    private double latitude;
    private double longitude;
    int id;

    public TaskModel(int id,String taskName, double latitude, double longitude) {
        this.taskName = taskName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
