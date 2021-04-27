package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.Map;

public class UserGetDTOWithoutToken {

    private Long id;
    private String username;
    private Map<String, Integer> highscores;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Map<String, Integer> getHighscores() {
        return highscores;
    }

    public void setHighscores(Map<String, Integer> highscores) {
        this.highscores = highscores;
    }

}
