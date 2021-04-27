package ch.uzh.ifi.hase.soprafs21.rest.dto;

public class LobbyGetDTOAllLobbies {

    private Long id;
    private String username;
    private Integer users;
    private Boolean isPublic;


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

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
