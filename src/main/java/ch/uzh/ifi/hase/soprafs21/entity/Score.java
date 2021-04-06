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
    public long userId;

    @Column(nullable = false, unique = true)
    public String userName;

    @Column(nullable = false)
    public int gameModeId;

    @Column(nullable = false)
    public int score;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGameModeId() {
        return gameModeId;
    }

    public void setGameModeId(int gameModeId) {
        this.gameModeId = gameModeId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    public void setUserId(long userId) {
        this.userId = userId;
    }


    public long getUserId() {
        return userId;
    }
}
