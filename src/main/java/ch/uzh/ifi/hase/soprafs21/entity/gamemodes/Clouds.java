package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;

import java.io.Serial;

public class Clouds extends GameMode {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name = "Clouds";

    public String getName() {
        return name;
    }

    public void setName(String name) {
       this.name = name; 
    }


    
}
