package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class GamePutDTO{
    private Long userId;
    private String gamemode;
    private boolean publicStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public Boolean getPublicStatus(){
        return publicStatus;
    }

    public void setPublicStatus(boolean publicStatus) {
        this.publicStatus = publicStatus;
    }


}
