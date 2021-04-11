package ch.uzh.ifi.hase.soprafs21.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unique across the database -> composes the primary key
 */
@Entity
@Table(name = "GAMEENTITY")
public class GameEntity implements Serializable {
    
    
    // Observer pattern fields
  //  final List<Observer> observers;

    // brauchen wir das?
  //  private Map<String, Integer> userScores;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gameId;

   // @Column(nullable = false)
  //  private final String roomKey;

 //   @Column(nullable = false)
    private GameStatus status;

    @Column(nullable = false)
    @OneToMany
    private List<Question> questions = new ArrayList<>();

  //  @Column(nullable = false)
   // private final UserMode userMode;

    //@Column(nullable = false)
    //private final GameMode gameMode;

    //public List<Observer> getObservers() {
    //    return observers;
  //  }

    //   public GameMode getGameMode() {
    //       return gameMode;
    //   }

    //    public UserMode getUserMode() {
    //        return userMode;
    //    }


    public GameStatus getStatus() {
        return status;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Long getGameId() {
        return gameId;
    }

    //    public Map<String, Integer> getUserScores() {
    //       return userScores;
    //   }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    //   public String getRoomKey() {
    //       return roomKey;
    //   }

    //  public void setUserScores(HashMap<String, Integer> userScores) {
    //       this.userScores = userScores;
    //   }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
