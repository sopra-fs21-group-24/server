package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
- userscores
- creator id saven?
*/

@Entity
@Table(name = "GAMEENTITY")
public class GameEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = false, unique = true)
    private String roomKey;

    @Column(nullable = false, unique = true)
    private Long creatorUserId;

    @Column(nullable = false)
    private int round = 0;

    private GameStatus status;

    @Column(nullable = false)
    @OneToMany
    private List<Question> questions;

    @Column(nullable = false)
    private UserMode userMode;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column
    private long gameTime;

    @Column(nullable = false)
    private Set<Long> userIds = Collections.synchronizedSet(new HashSet<Long>() );

    @Column(nullable = false)
    private HashMap<Long, Long> scores = new HashMap<>(); 


    public GameMode getGameMode() {
        return gameMode;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setCurrentTime() {
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public void setUserMode(UserMode userMode){
        this.userMode = userMode;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public Set<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(Set<Long> userIds) {
        this.userIds = userIds;
    }

    public List<Long> getScoreIds() {
        return scoreIds;
    }

    public void setScoreIds(List<Long> scoreIds) {
        this.scoreIds = scoreIds;
    }
}
