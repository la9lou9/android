package com.example.myapplication.Prescription;

public class Prescription {
    private long id;
    private long userId;
    private String doctorName; // Changed from long doctorId to String doctorName
    private String details;
    private String date;

    public Prescription(long userId, String doctorName, String details, String date) {
        this.userId = userId;
        this.doctorName = doctorName;
        this.details = details;
        this.date = date;
    }

    // Getter and Setter methods
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

