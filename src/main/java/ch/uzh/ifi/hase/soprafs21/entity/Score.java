package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;



@Entity
@Table(name = "Score")
public class Score implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(nullable = false, unique = true)
    private long userId;

    @Column(nullable = false)
    private long tempScore = 0;

    @Column(nullable = false)
    private long totalScore = 0;

    @Column()
    private Coordinate lastCoordinate;

    @Column()
    private String city;

    @Column()
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTempScore() {
        return tempScore;
    }

    public void setTempScore(long score) {
        this.tempScore = score;
    }

    public long getTotalScore(){
        return totalScore;
    }

    public void setTotalScore(long totalScore){
        this.totalScore = totalScore;
    }

    public Coordinate getLastCoordinate() {
        return lastCoordinate;
    }

    public void setLastCoordinate(Coordinate lastCoordinate) {
        this.lastCoordinate = lastCoordinate;
    }

}
