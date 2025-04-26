package com.example.myapplication;

public class GeminiRequest {
    private String message;

    public GeminiRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

