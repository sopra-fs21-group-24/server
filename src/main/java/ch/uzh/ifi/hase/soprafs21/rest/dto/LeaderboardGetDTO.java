package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.gameModeEnum;

public class LeaderboardGetDTO {

    private long id;
    private String userName;
    private gameModeEnum gameMode;
    private int score;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public gameModeEnum getGameMode() {
        return gameMode;
    }

    public void setGameMode(gameModeEnum gameMode) {
        this.gameMode = gameMode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
