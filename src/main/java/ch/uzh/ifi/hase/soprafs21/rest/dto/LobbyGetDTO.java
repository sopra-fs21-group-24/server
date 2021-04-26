package ch.uzh.ifi.hase.soprafs21.rest.dto;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.User;



public class LobbyGetDTO {
    private Long id;
    private Long creator;
    private List<UserGetDTOWithoutToken> users;
    private Boolean isPublic;
    private Long roomKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public List<UserGetDTOWithoutToken> getUsers() {
        return users;
    }

    public void setUsers(List<UserGetDTOWithoutToken> users) {
        this.users = users;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Long getRoomKey() { return roomKey; }

    public void setRoomKey(Long roomKey) { this.roomKey = roomKey; }
}
