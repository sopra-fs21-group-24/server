package ch.uzh.ifi.hase.soprafs21.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Question")
public class Question implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue    for testing
    private Long questionId;

    @Column(nullable = false)

    private int zoomLevel;

    @Column(nullable = false)
    private float lat;

    @Column(nullable = false)
    private float lng;


    @Column(nullable = false)
    private Coordinate coordinate;


    public Long getQuestionId() {
        return questionId;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel =  zoomLevel;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
}


