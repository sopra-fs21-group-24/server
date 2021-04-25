package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;

@Entity
@Table(name = "USERMODE")
public abstract class UserMode implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;

    @Id
    @GeneratedValue
    private Long uModeId;

    public void init(GameEntity game, boolean publicStatus){
        throw new UnsupportedOperationException();
    }

    public void start(GameEntity game){
        throw new UnsupportedOperationException();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}