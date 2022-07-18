package com.example.iot;

public class Distance {
    String distance;
    String date;

    public Distance(String distance, String date) {
        this.distance = distance;
        this.date = date;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
