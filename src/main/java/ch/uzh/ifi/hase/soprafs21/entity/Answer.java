package ch.uzh.ifi.hase.soprafs21.entity;

public class Answer {
    private Long userId;
    private Long gameId;
    private Long questionId;
    private Coordinate coordGuess;
    private Coordinate coordQuestion;
    private float difficultyFactor;
    private float timeFactor;

    public Answer(Long userId, Long gameId, Long questionId, Coordinate coordGuess, float difficultyFactor){
        this.userId = userId;
        this.questionId = questionId;
        this.gameId = gameId;
        this.coordGuess = coordGuess;
        this.difficultyFactor = difficultyFactor;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Coordinate getCoordGuess() {
        return coordGuess;
    }

    public Coordinate getCoordQuestion() {
        return coordQuestion;
    }

    public void setCoordQuestion(Coordinate coordQuestion) {
        this.coordQuestion = coordQuestion;
    }

    public float getDifficultyFactor() {
        return difficultyFactor;
    }

    public float getTimeFactor() {
        return timeFactor;
    }

    public void setTimeFactor(float timeFactor) {
        this.timeFactor = timeFactor;
    }
}
