package com.example.iot;

public class Motor {
    String RPM;
    String date;

    public Motor(String RPM, String date) {
        this.RPM = RPM;
        this.date = date;
    }

    public String getRPM() {
        return RPM;
    }

    public void setRPM(String RPM) {
        this.RPM = RPM;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
