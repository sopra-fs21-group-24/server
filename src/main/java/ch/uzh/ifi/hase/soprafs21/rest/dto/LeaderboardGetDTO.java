package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.gameModeEnum;

public class LeaderboardGetDTO {

    private String username;
    private gameModeEnum gameMode;
    private int score;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
