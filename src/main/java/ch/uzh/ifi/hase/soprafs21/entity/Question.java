package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "Question")
public class Question implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long questionId;

    @Column(nullable = false)
    private int zoomLevel;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Coordinate coordinate;

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}


