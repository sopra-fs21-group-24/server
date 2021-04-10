package ch.uzh.ifi.hase.soprafs21.rest.dto;



public class LeaderboardGetDTO {

    private long userId;
    private String userName;
    private int gameModeId;
    private int score;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGameModeId() {
        return gameModeId;
    }

    public void setGameModeId(int gameModeId) {
        this.gameModeId = gameModeId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
