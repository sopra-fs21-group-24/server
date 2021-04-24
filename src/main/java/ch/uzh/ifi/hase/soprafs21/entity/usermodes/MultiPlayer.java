package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;

public class MultiPlayer extends UserMode {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(GameEntity game) {
        Lobby lobby = new Lobby();
        lobby.setCreator(game.getCreatorUserId());
        lobby.setPublic(game.getPublicStatus());
    }

    @Override
    public void start(GameEntity game) {
        Set<Long> users = game.getUserIds();
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
}
