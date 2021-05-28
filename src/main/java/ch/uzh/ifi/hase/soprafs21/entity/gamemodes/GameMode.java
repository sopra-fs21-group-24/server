package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;

@Entity
@Table(name = "GAMEMODE")
public abstract class GameMode implements Serializable {
    private static final int X_ZERO = 8000;
    private static final int FULL_POINTS_RANGE = 25;
    private static final double M = 1/Math.pow((FULL_POINTS_RANGE - X_ZERO), 2);


    @Serial
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
        double distanceFactor = calculateDistanceFactor(coordGuess, coordQuestion);
        double scoreFactor = distanceFactor * difficultyFactor;

        return Math.round(scoreFactor * 450 + timeFactor * 50);
    } 

    public void checkTimeValid(GameEntity game, long currentTime) {
        Long roundStart = game.getRoundStart();
        if (currentTime < roundStart || currentTime > (roundStart + game.getRoundDuration() * 1000)){
            throw new PreconditionFailedException("Request outside of round timeframe");
        } 
    }

    public double calculateDistanceFactor(Coordinate coordGuess, Coordinate coordQuestion){
        double distance = haversineDistance(coordGuess, coordQuestion);

        if (distance < FULL_POINTS_RANGE){
            return 1;
        }

        else if(distance > X_ZERO){
            return 0;
        }

        return M * Math.pow((distance - X_ZERO), 2);
    }

    public float calculateTimeFactor(GameEntity game, Long currentTime){
        checkTimeValid(game, currentTime);
        long inGameTime = currentTime - game.getRoundStart(); 
        int roundDuration = game.getRoundDuration()*1000;

        if (inGameTime <= (roundDuration/2)){
            return 1;
        } 
        return 1.0f - (inGameTime / (float)roundDuration);
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