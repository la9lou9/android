package com.example.myapplication;

public class Appointment {
    private long id;
    private String doctorName;
    private String address;
    private String phoneNumber;
    private String date;

    public Appointment(String doctorName, String address, String phoneNumber, String date) {
        this.doctorName = doctorName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDate() {
        return date;
    }
}
