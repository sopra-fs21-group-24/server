package ch.uzh.ifi.hase.soprafs21.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ch.uzh.ifi.hase.soprafs21.deserialitzers.GameDeserializer;

@JsonDeserialize(using = GameDeserializer.class)
public class GamePostDTOCreate {
    private Long userId;
    private String userMode;
    private String gameMode;
    private boolean publicStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserMode() {
        return userMode;
    }

    public void setUserMode(String userMode) {
        this.userMode = userMode;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public Boolean getPublicStatus(){
        return publicStatus;
    }

    public void setPublicStatus(boolean publicStatus) {
        this.publicStatus = publicStatus;
    }


}
