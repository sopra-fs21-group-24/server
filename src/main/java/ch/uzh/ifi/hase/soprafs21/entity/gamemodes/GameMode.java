package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;

@JsonDeserialize()
@Entity
@Table(name = "GAMEMODE")
public abstract class GameMode implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long gModeId;

    public Long calculateScore(Answer answer) {
        float difficultyFactor = answer.getDifficultyFactor();
        float timeFactor = answer.getTimeFactor();
        Coordinate coordGuess = answer.getCoordGuess();
        Coordinate coordQuestion = answer.getCoordQuestion();

        if (difficultyFactor < 0 || difficultyFactor > 1){
            throw new PreconditionFailedException("DifficultyFactor is not in range 0 - 1");
        }
        else if (timeFactor < 0.0f || timeFactor > 1.0f){
            throw new PreconditionFailedException("timeFactor: " + timeFactor + " is not in range 0 - 1");
        }
        // normalized to distance india - chile 
        double distanceFactor = 1 - (haversineDistance(coordGuess, coordQuestion)/16000);
        double scoreFactor = distanceFactor * timeFactor * difficultyFactor;

        return Math.round(scoreFactor * 500);
    } 

    public double haversineDistance(Coordinate coordGuess, Coordinate coordQuestion){
        // degrees to radians.
        double lon1 = Math.toRadians(coordGuess.getLon());
        double lat1 = Math.toRadians(coordGuess.getLat());
        double lon2 = Math.toRadians(coordQuestion.getLon());
        double lat2 = Math.toRadians(coordQuestion.getLat());
 
        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2),2);
             
        double c = 2 * Math.asin(Math.sqrt(a));
        double radius = 3956;
 
        return(c * radius);
    }

    public abstract String getName(); 
    public abstract void setName(String name);
}