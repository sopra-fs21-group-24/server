package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "LOBBY")
public class Lobby {


    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true, unique =  true)
    private Long gameId;

    @Column(nullable = false)
    private Long creator;

    @Column(nullable = true, unique =  true)
    private Long roomKey;

    @Column(nullable = false)
    @ElementCollection
    private List<Long> users = Collections.synchronizedList(new ArrayList<>());

    @Column(nullable = false)
    private Boolean publicStatus;

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public void addUser(Long userId){
        this.users.add(userId);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }

    public Long getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(Long roomKey) {
        this.roomKey = roomKey;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }
}
