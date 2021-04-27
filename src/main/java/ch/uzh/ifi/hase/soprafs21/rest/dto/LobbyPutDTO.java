package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LobbyPutDTO {

    private Long lobbyId;
    private Long userId;

    public Long getlobbyIdlobby() {
        return lobbyId;
    }

    public void setlobbyId(Long lobbyId) {
        this.lobbyId = lobbyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
