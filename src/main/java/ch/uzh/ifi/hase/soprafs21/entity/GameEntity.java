package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;


@Entity
@Table(name = "GAMEENTITY")
public class GameEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = false, unique = true)
    private Long creatorUserId;

    @Column(nullable = false)
    private int round = 0;

    @Column(nullable = false)
    @ElementCollection
    private List<Long> questions;

    @Column(nullable = false)
    private UserMode userMode;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column()
    private Long gameStartTime;

    @Column()
    private Long roundStart;

    @Column()
    private Long lobbyId;

    @Column()
    private transient int roundDuration = 35;

    @Column()
    private transient int breakDuration = 8;
    
    @Column()
    @ElementCollection
    private List<Long> usersAnswered;

    @Column()
    private int threshold;

    @Column(nullable = false)
    @ElementCollection
    private List<Long> userIds =Collections.synchronizedList(new ArrayList<>());

    public GameMode getGameMode() {
        return gameMode;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public Long getGameStartTime() {
        return gameStartTime;
    }

    public void setCurrentTime() {
        this.gameStartTime = System.currentTimeMillis();
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

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setUsersAnswered(List<Long> usersAnswered) {
        this.usersAnswered = usersAnswered;
    }

    public List<Long> getUsersAnswered() {
        return usersAnswered;
    }

    public void addUserAnswered(Long userId){
        usersAnswered.add(userId);
    }

    public void removeUserAnswered(Long userId){
        usersAnswered.remove(userId);
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public void setGameModeFromName(String gameModeName){

        this.gameMode = switch (gameModeName) {
            case "Pixelation" -> new Pixelation();
            case "Clouds" -> new Clouds();
            default -> new Time();
        };
    }

    public void setUserMode(UserMode userMode){
        this.userMode = userMode;
    }

    public void setUserModeFromName(String userModeName){
        UserMode uMode;

        if (userModeName.equals("Singleplayer")){
            uMode = new SinglePlayer();
        } else if(userModeName.equals("Multiplayer")){
            uMode = new MultiPlayer();
        } else {
            throw new NotFoundException("Can not read this kind of Usermode");
        }

        this.userMode = uMode;
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

    public void incrementRound(){
        this.round += 1;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Long getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getRoundDuration() {
        return roundDuration;
    }

    public void setRoundDuration(int roundDuration) {
        this.roundDuration = roundDuration;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public Long getRoundStart() {
        return roundStart;
    }

    public void setRoundStart(Long roundStart) {
        this.roundStart = roundStart;
    }
}
