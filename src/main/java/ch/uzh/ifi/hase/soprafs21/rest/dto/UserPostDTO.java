package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;

public class UserPostDTO {

    private String username;
    private String password;
    private String token;
    private UserStatus status;

    public UserStatus getUserStatus() {
        return this.status;
    }

    public void setUserStatus(UserStatus status) {
        this.status = status;
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
