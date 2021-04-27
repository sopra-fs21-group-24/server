package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.Map;

// Contains no Secret Information

public class UserGetDTO {

    private Long id;
    private String username;
    private String token;
    private Map<String, Integer> highscores;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getToken(){ return token; }

    public void setToken(String token){ this.token = token; }

    public Map<String, Integer> getHighscores() {
        return highscores;
    }

    public void setHighscores(Map<String, Integer> highscores) {
        this.highscores = highscores;
    }

}
