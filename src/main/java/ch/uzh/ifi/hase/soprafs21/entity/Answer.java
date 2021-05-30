package ch.uzh.ifi.hase.soprafs21.entity;

public class Answer {
    private Long userId;
    private Long gameId;
    private Long questionId;
    private Coordinate coordGuess;
    private Coordinate coordQuestion;
    private float difficultyFactor;
    private float timeFactor;
    private String city;
    private String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

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

    public Coordinate getCoordQuestion() {
        return coordQuestion;
    }

    public void setCoordQuestion(Coordinate coordQuestion) {
        this.coordQuestion = coordQuestion;
    }

    public float getDifficultyFactor() {
        return difficultyFactor;
    }

    public void setDifficultyFactor(float difficultyFactor) {
        this.difficultyFactor = difficultyFactor;
    }

    public float getTimeFactor() {
        return timeFactor;
    }

    public void setTimeFactor(float timeFactor) {
        this.timeFactor = timeFactor;
    }
}
