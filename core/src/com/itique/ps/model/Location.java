package com.itique.ps.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Location {

    private float longitude;
    private float latitude;

    public Location(float longitude, float latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location() {
        // for serialization
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("longitude", longitude)
                .append("latitude", latitude)
                .toString();
    }

}
