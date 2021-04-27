package ch.uzh.ifi.hase.soprafs21.rest.dto;

// Contains no Secret Information

public class UserGetDTO {

    private Long id;
    private String username;
    private String token;
    private Integer highClouds;
    private Integer highPixel;
    private Integer highTime;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getToken(){ return token; }

    public void setToken(String token){ this.token = token; }

    public Integer getHighClouds() { return highClouds; }

    public void setHighClouds(Integer highClouds) { this.highClouds = highClouds; }

    public Integer getHighPixel() { return highPixel; }

    public void setHighPixel(Integer highPixel) { this.highPixel = highPixel; }

    public Integer getHighTime() { return highTime; }

    public void setHighTime(Integer highTime) { this.highTime = highTime; }
}
