package com.example.iot;

public class PIR {
    String state;
    String date;

    public PIR(String state, String date) {
        this.state = state;
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
