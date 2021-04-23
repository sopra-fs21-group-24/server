package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "Score")
public class Score {
    @Id
    @Column(nullable = false, unique = true)
    private long userId;

    @Column(nullable = false)
    private int tempScore = 0;

    @Column(nullable = false)
    private int totalScore = 0;

    @Column(nullable = true)
    private float lastLat; 

    @Column(nullable = true)
    private float lastLong; 

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getTempScore() {
        return tempScore;
    }

    public void setTempScore(int score) {
        this.tempScore = score;
    }

    public int getTotalScore(){
        return totalScore;
    }

    public void setTotalScore(int totalScore){
        this.totalScore = totalScore;
    }

    public float getLastLat() {
        return lastLat;
    }

    public void setLastLat(float lastLat) {
        this.lastLat = lastLat;
    }

    public float getLastLong() {
        return lastLong;
    }

    public void setLastLong(float lastLong) {
        this.lastLong = lastLong;
    }
}
