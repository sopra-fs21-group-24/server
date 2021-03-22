package ch.uzh.ifi.hase.soprafs21.entity.GameModes;

import ch.uzh.ifi.hase.soprafs21.entity.GameMode;

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
