package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

    @Serial
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

    @Column(nullable = false)
    @ElementCollection
    private Map<String, Integer> highScores = Collections.synchronizedMap(new HashMap<>());

    @Column(nullable = false)
    private Boolean isInLobby  = false;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public Boolean getInLobby() { return isInLobby; }

    public void setInLobby(Boolean inLobby) { isInLobby = inLobby; }

    public Map<String, Integer> getHighScores() {
        return highScores;
    }

    public void setHighScores(Map<String, Integer> highScores) {
        this.highScores = highScores;
    }

    public void initHighScores(){
        highScores.put("Time", 0);
        highScores.put("Clouds", 0);
        highScores.put("Pixelation", 0);
    }
}
