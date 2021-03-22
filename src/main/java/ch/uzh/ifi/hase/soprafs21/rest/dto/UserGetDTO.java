package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

import java.util.Date;

// Contains no Secret Information

public class UserGetDTO {

    private Long id;
    private String name;
    private String username;
    private UserStatus logged_in;
    private String token;
    private Date createdDate;
    private String birthdate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserStatus getLogged_in() {
        return logged_in;
    }

    public void setLogged_in(UserStatus logged_in) {
        this.logged_in = logged_in;
    }

    public String getToken(){return token;}

    public void setToken(String token){this.token = token; }

    public Date getCreatedDate(){return createdDate;}

    public void setCreatedDate(Date date){this.createdDate = date; }
    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }


}
