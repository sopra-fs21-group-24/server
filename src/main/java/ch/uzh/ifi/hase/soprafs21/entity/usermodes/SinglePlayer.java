package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;

public class SinglePlayer extends UserMode {
    private static final long serialVersionUID = 1L;

    private String name = "SinglePlayer";

    @Override
    public void init(GameEntity game, boolean publicStatus) {
        game.setLobbyId(null);
    }

    @Override
    public void start(GameEntity game) {
        List<Long> users = game.getUserIds();
        if (users.size() != 1) {
            throw new NotFoundException("Single: Number of Users playing incorrect");
        }

        Map<Long, Score> scores = new HashMap<>();
        Score score = new Score();
        Long userId = game.getCreatorUserId();
        score.setUserId(userId);
        scores.put(userId, score);

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setLobbyService(LobbyService lobbyService) {
        super.setLobbyService(null);
    }

    
}
