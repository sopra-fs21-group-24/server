package ch.uzh.ifi.hase.soprafs21.rest.dto;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.User;



public class LobbyGetDTO {
    private Long id;
    private String creator;
    private List<Long> users;
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

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }
}
