package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;


@Entity
@Table(name = "Leaderboard")
public class Leaderboard implements Observer {
    @Id
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String  username;

    @Column(nullable = false)
    private String gameMode;

    @Column(nullable = false)
    @OrderBy
    private Long score;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }



    public void setId(long id) {
        this.id = id;
    }


    public long getId() {
        return id;
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException();
    }
}
