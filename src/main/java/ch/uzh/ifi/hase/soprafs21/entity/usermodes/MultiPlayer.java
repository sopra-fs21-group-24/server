package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;

public class MultiPlayer extends UserMode {
    private static final long serialVersionUID = 1L;

    private String name = "MultiPlayer";
    private int playersFinished = 0;

    @Override
    public void init(GameEntity game, boolean publicStatus) {
        Lobby lobby = new Lobby();
        Long creator = game.getCreatorUserId();
        lobby.setCreator(creator);
        lobby.setPublic(publicStatus);
        this.lobbyService.createLobby(lobby);
        game.setLobbyId(lobby.getId());
        game.setUserIds(Arrays.asList(creator));
    }

    @Override
    public void start(GameEntity game) {
        // set roundStart time 
        super.start(game);
        
        List<Long> users = game.getUserIds();
        if (users.size() < 2) {
            throw new PreconditionFailedException("User is starting the game prematurly");
        }

        for (Long user : users) {
            Score score = new Score();
            score.setUserId(user);
            scoreService.save(score);
        }
    }  
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getPlayersFinished() {
        return playersFinished;
    }

    public void setPlayersFinished(int playersFinished) {
        this.playersFinished = playersFinished;
    }
}
