package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.*;


@Entity
@Table(name = "Question")
public class Question {



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


