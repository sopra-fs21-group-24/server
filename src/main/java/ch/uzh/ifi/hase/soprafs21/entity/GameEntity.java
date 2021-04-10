package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;

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
    
    
    // Observer pattern fields
    final ArrayList<Observer> observers;

    // brauchen wir das?
    private HashMap<String, Integer> userScores;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

    @Column(nullable = false)
    private final String roomKey;

    @Column(nullable = false)
    private GameStatus status;

    @ElementCollection
    @CollectionTable(name = "QUESTIONS", joinColumns = @JoinColumn(name = "gameId"))
    @Column(nullable = false)
    private ArrayList<Question> questions = new ArrayList<>();

    @Column(nullable = false)
    private final UserMode userMode;

    @Column(nullable = false)
    private final GameMode gameMode;


}
