package ch.uzh.ifi.hase.soprafs21.entity;
import javax.persistence.*;

import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;


@Entity
@Table(name = "Leaderboard")
public class Leaderboard{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
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



    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

}
