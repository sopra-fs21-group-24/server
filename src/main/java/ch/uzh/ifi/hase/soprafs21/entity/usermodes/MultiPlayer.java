package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;

public class MultiPlayer extends UserMode {
    private static final long serialVersionUID = 1L;

    private String name = "MultiPlayer";

    @Override
    public void init(GameEntity game, boolean publicStatus) {
        Lobby lobby = new Lobby();
        lobby.setCreator(game.getCreatorUserId());
        lobby.setPublic(publicStatus);
        game.setLobbyId(lobby.getId());
        this.lobbyService.createLobby(lobby);
    }

    @Override
    public void start(GameEntity game) {
        List<Long> users = game.getUserIds();
        if (users.size() < 2) {
            throw new NotFoundException("User is starting the game prematurly");
        }

        Map<Long, Score> scores = new HashMap<>();
        for (Long user : users) {
            Score score = new Score();
            score.setUserId(user);
            scores.put(user, score);
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
}
