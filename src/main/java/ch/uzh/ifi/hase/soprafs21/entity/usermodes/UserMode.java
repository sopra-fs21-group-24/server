package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.ScoreService;

@Entity
@Table(name = "USERMODE")
public abstract class UserMode implements Serializable {

    private static final long serialVersionUID = 1L;

    protected transient LobbyService lobbyService;
    protected transient ScoreService scoreService;

    @Id
    @GeneratedValue
    private Long uModeId;

    public abstract void init(GameEntity game, boolean publicStatus);

    public void start(GameEntity game){
        throw new UnsupportedOperationException();
    }

    public abstract String getName();
    public abstract void setName(String name);

    public void setLobbyService(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }
}