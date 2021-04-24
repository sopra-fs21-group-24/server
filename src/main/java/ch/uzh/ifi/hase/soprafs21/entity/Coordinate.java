package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;

public class Coordinate implements Serializable{
    private double lon;
    private double lat;

    public Coordinate(double lon, double lat){
        this.lon = lon;
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
