package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;

public class ScoreGetDTO {

    private long userId;
    private long tempScore;
    private long totalScore;
    private Coordinate lastCoordinate;


    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public long getTempScore() { return tempScore; }

    public void setTempScore(long tempScore) { this.tempScore = tempScore; }

    public long getTotalScore() { return totalScore; }

    public void setTotalScore(long totalScore) { this.totalScore = totalScore; }

    public Coordinate getLastCoordinate() { return lastCoordinate; }

    public void setLastCoordinate(Coordinate lastCoordinate) { this.lastCoordinate = lastCoordinate; }
}
