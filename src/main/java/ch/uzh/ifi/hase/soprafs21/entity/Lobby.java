package ch.uzh.ifi.hase.soprafs21.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LOBBY")
public class Lobby {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String creater;


    @OneToMany(targetEntity= User.class)
    @Column(nullable = false)
    private List<User> users;



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


    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }
}
