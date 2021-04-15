package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class GameGetDTO {
    private String gameId;
    private String token;


    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
