package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserPostDTO {

    private String name;

    private String username;

    private String password;

    private String token;

    private UserStatus logged_in;

    public UserStatus getLogged_in() {
        return this.logged_in;
    }

    public void setUserStatus(UserStatus logged_in) {
        this.logged_in = logged_in;
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

    public String getPassword(){return password;}

    public void setPassword(String password){this.password = password; }

    public String getToken(){return token;}

    public void setToken(String token){this.token = token; } 

}
