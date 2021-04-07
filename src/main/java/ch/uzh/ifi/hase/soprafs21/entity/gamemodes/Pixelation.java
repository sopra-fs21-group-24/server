package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;
public class Pixelation extends GameMode {
    public Pixelation(String name, int duration){
        this.GameDuration = duration;
        this.GameModeName = name;
        this.difficultyFactor = 1;
    }

    @Override
    public int calculateScore(int time, int difficultyFactor) {
        return 0;
    }
}
