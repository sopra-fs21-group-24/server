package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;

/* 
TODO:
- user and game mode enum?
- random roomkey
- get OneToMany to work
- makeguess()
- Round int
*/

@Entity
@Table(name = "GAMEENTITY")
public class GameEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = false)
    private final String roomKey;

    @Column(nullable = false)
    private int round = 0;

    private GameStatus status;

    @Column(nullable = false)
    @OneToMany
    private List<Question> questions;

    @Column(nullable = false)
    private final UserMode userMode;

    @Column(nullable = false)
    private final GameMode gameMode;

    @Column
    private long gameTime;


    public GameMode getGameMode() {
        return gameMode;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void start() {
        this.gameTime = System.currentTimeMillis();

    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @ManyToOne
    @JoinColumn(name="CUST_ID", nullable=false)
    public List<Question> getQuestions() {
        return questions;
    }

    public Long getGameId() {
        return gameId;
    }

    public Map<String, Integer> getUserScores() {
        return userScores;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getRoomKey() {
        return roomKey;
    }

}
