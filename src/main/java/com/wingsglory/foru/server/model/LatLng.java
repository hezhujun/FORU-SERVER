package com.wingsglory.foru.server.model;

/**
 * Created by hezhujun on 2017/7/23.
 */
public class LatLng {
    public double latitude;
    public double longitude;

    public LatLng() {
    }

    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
