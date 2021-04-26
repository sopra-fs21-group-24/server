package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String password;

    @Column(nullable = true)
    private Integer highClouds;

    @Column(nullable = true)
    private Integer highTime;

    @Column(nullable = true)
    private Integer highPixel;

    // TODO
    //List with already answered questions


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getHighClouds() { return highClouds; }

    public void setHighClouds(Integer highClouds) { this.highClouds = highClouds; }

    public Integer getHighTime() { return highTime; }

    public void setHighTime(Integer highTime) { this.highTime = highTime; }

    public Integer getHighPixel() { return highPixel; }

    public void setHighPixel(Integer highPixel) { this.highPixel = highPixel; }
}
