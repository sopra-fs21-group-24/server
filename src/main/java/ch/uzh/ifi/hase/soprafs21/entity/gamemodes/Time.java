package ch.uzh.ifi.hase.soprafs21.entity.gamemodes;
public class Time extends GameMode {
    private static final long serialVersionUID = 1L;

    private String name = "Time";

    public String getName() {
        return name;
    }

    public void setName(String name) {
       this.name = name; 
    }
}
