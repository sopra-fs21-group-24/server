package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;

public class ScoreGetDTO {

    private long userId;
    private long tempScore;
    private long totalScore;
    private String username;
    private Coordinate lastCoordinate;
    private Coordinate solutionCoordinate;


    public long getUserId() { return userId; }

    public void setUserId(long userId) { this.userId = userId; }

    public long getTempScore() { return tempScore; }

    public void setTempScore(long tempScore) { this.tempScore = tempScore; }

    public long getTotalScore() { return totalScore; }

    public void setTotalScore(long totalScore) { this.totalScore = totalScore; }

    public Coordinate getLastCoordinate() { return lastCoordinate; }

    public void setLastCoordinate(Coordinate lastCoordinate) { this.lastCoordinate = lastCoordinate; }

    public Coordinate getSolutionCoordinate() {
        return solutionCoordinate;
    }

    public void setSolutionCoordinate(Coordinate solutionCoordinate) {
        this.solutionCoordinate = solutionCoordinate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
