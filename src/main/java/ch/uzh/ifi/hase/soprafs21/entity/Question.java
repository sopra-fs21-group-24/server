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

    @Column(nullable = false)
    public float zoomLevel;
    @Column(nullable = false)
    public float lat;
    @Column(nullable = false)
    public float lng;
    @Id
    @GeneratedValue
    private Long questionId;


    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

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
}


