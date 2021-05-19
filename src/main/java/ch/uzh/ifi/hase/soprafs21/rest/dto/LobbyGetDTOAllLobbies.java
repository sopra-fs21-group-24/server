package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LobbyGetDTOAllLobbies {

    private Long id;
    private String username;
    private Integer users;
    private Boolean publicStatus;
    private String GameMode;


    public String getGameMode() {return GameMode;}

    public void setGameMode(String gameMode) {GameMode = gameMode;}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
