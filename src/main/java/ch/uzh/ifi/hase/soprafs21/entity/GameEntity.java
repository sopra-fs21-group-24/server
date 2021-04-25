package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;

/* 
TODO:
- user and game mode enum?
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
    private Long creatorUserId;

    @Column(nullable = false)
    private int round = 0;

    private boolean publicStatus; // fühlt sich komisch an

    @Column(nullable = false)
    @ElementCollection
    private List<Long> questions;

    @Column(nullable = false)
    private UserMode userMode;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column
    private Long gameTime;

    @Column(nullable = false)
    @ElementCollection
    private Set<Long> userIds = Collections.synchronizedSet(new HashSet<Long>());

    public GameMode getGameMode() {
        return gameMode;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setCurrentTime() {
        this.gameTime = System.currentTimeMillis();
    }

    public void setQuestions(List<Long> questions) {
        this.questions = questions;
    }

    public List<Long> getQuestions() {
        return questions;
    }

    public Long getGameId() {
        return gameId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public boolean getPublicStatus(){
        return publicStatus;
    }

    public void setPublicStatus(boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

}
