package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;

public class SinglePlayer extends UserMode {
    private static final long serialVersionUID = 1L;

    @Override
    public void init(GameEntity game) {
        throw new UnsupportedOperationException();
    };

    @Override
    public void start(GameEntity game) {
        Set<Long> users = game.getUserIds();
        if (users.size() != 1) {
            throw new NotFoundException("Single: Number of Users playing incorrect");
        }

        Map<Long, Score> scores = new HashMap<>();
        Score score = new Score();
        Long userId = game.getCreatorUserId();
        score.setUserId(userId);
        scores.put(userId, score);

    }
}