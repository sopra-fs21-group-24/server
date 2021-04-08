package ch.uzh.ifi.hase.soprafs21.entity;
import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;
import javassist.runtime.Desc;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;


@Entity
@Table(name = "Leaderboard")
public class Leaderboard implements Observer {
    @Id
    @Column(nullable = false, unique = true)
    private long userId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private int gameModeId;

    @Column(nullable = false)
    @OrderBy
    private int score;


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

    @Override
    public void update() {

    }
}
