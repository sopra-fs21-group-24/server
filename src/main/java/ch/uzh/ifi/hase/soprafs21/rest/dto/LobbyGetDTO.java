package ch.uzh.ifi.hase.soprafs21.rest.dto;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;


public class LobbyGetDTO {
    private Long id;
    private Long gameId;
    private Long creator;
    private Long roomKey;
    private Boolean isPublic;
    private GameMode gamemode;
    private List<UserGetDTOWithoutToken> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public List<UserGetDTOWithoutToken> getUsers() {
        return users;
    }

    public void setUsers(List<UserGetDTOWithoutToken> users) {
        this.users = users;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public GameMode getGamemode() {
        return gamemode;
    }

    public void setGamemode(GameMode gamemode) {
        this.gamemode = gamemode;
    }

    public Long getRoomKey() { 
        return roomKey; 
    }

    public void setRoomKey(Long roomKey) { 
        this.roomKey = roomKey; 
    }
}
