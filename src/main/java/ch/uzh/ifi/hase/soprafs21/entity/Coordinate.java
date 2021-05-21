package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;

import org.springframework.lang.Nullable;

public class Coordinate implements Serializable{
    @Nullable
    private Double lon;

    @Nullable
    private Double lat;

    public Coordinate(Double lon, Double lat){
        this.lon = lon;
        this.lat = lat;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
