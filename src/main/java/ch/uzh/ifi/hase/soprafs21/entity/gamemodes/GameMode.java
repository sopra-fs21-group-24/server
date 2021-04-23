package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "GAMEMODE")
public class GameMode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gModeId;

    public int calculateScore(float lon, float lat, float difficultyFactor) {
        return 0;
    } 


}