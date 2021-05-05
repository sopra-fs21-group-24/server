package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;

import java.io.Serial;

public class Pixelation extends GameMode {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name = "Pixelation";

    public String getName() {
        return name;
    }

    public void setName(String name) {
       this.name = name; 
    }

}
