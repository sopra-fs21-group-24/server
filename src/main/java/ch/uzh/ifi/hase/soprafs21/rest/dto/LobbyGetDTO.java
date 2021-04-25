package ch.uzh.ifi.hase.soprafs21.rest.dto;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.User;



public class LobbyGetDTO {
    private Long id;
    private String creator;
    private List<User> users;
    private Boolean isPublic;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
