package com.example.iot;

import java.util.Calendar;


public class Temp {
    String temp;
    String date;

    public Temp(String temp, String date) {
        this.temp = temp;
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
