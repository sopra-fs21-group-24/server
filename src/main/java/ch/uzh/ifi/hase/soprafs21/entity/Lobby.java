package ch.uzh.ifi.hase.soprafs21.entity;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name = "LOBBY")
public class Lobby {


    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long creator;

    @Column(nullable = false, unique =  true)
    private Long roomKey;

    @OneToMany()
    @Column(nullable = false)
    private List<User> users;

    @Column(nullable = false)
    private Boolean isPublic;



    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }


    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(Long roomKey) {
        this.roomKey = roomKey;
    }

}
