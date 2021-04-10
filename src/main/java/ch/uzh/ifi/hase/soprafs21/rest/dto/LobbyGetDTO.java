package ch.uzh.ifi.hase.soprafs21.rest.dto;
import java.util.List;
import ch.uzh.ifi.hase.soprafs21.entity.User;



public class LobbyGetDTO {
    private Long id;
    private String creater;
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
