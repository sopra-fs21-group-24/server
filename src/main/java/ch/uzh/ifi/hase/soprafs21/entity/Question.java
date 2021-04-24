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
    @GeneratedValue
    private Long questionId;

    @Column(nullable = false)
    public float zoomLevel;

    @Column(nullable = false)
    public float lat;

    @Column(nullable = false)
    public float lng;

    @Column(nullable = false)
    public Coordinate coordinate;


    @Id
    public Long getQuestionId() {
        return questionId;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(float zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}


