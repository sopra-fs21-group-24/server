package ch.uzh.ifi.hase.soprafs21.rest.dto;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;

public class AnswerPostDTO{

    private Long questionId;
    private Coordinate coordGuess;
    private float difficultyFactor;

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Coordinate getCoordGuess() {
        return coordGuess;
    }

    public void setCoordGuess(Coordinate coordGuess) {
        this.coordGuess = coordGuess;
    }

    public float getDifficultyFactor() {
        return difficultyFactor;
    }

    public void setDifficultyFactor(float difficultyFactor) {
        this.difficultyFactor = difficultyFactor;
    }


}
