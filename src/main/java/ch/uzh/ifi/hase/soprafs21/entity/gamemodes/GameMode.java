package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;
public abstract class GameMode {

    public int gameModeId;
    public int difficultyFactor;
    public String GameModeName;

    public int GameDuration;

    public abstract int calculateScore(int time, int difficultyFactor);

}
