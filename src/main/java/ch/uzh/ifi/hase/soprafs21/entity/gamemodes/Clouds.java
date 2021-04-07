package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;
public class Clouds extends GameMode {

    public Clouds(String name, int duration){
        this.GameDuration = duration;
        this.GameModeName = name;
    }

    @Override
    public int calculateScore(int time, int difficultyFactor) {
        return 0;
    }
}
