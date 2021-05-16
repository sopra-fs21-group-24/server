package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;

import java.io.Serial;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;

public class Pixelation extends GameMode {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name = "Pixelation";

    @Override
    public Long calculateScore(Answer answer) {
        answer.setDifficultyFactor(1);
        return super.calculateScore(answer);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
       this.name = name; 
    }

}
