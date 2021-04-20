package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.GameStatus;

public class GamePostDTOStartGame {
    private GameStatus gameStatus;
    private Long token;


    public Long getToken() {return token;}
    public void setToken(Long token) {this.token = token;}
    public GameStatus getGameStatus() { return gameStatus;}
    public void setGameStatus(GameStatus gameStatus) {this.gameStatus = gameStatus;}
}
