package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;

public class GamePostDTOCreate {
    private Long userId;
    private UserMode userMode;
    private GameMode gameMode;
    private boolean publicStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserMode getUserMode() {
        return userMode;
    }

    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Boolean getPublicStatus(){
        return publicStatus;
    }

    public void setPublicStatus(boolean publicStatus) {
        this.publicStatus = publicStatus;
    }


}
