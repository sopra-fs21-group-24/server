package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.io.Serial;
import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;

public class SinglePlayer extends UserMode {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name = "Singleplayer";

    @Override
    public void init(GameEntity game, boolean publicStatus) {
        game.setLobbyId(null);
        game.setUserIds(Arrays.asList(game.getCreatorUserId()));
    }

    @Override
    public void start(GameEntity game) {
        // set roundStart time 
        super.start(game);

        List<Long> users = game.getUserIds();
        if (users.size() != 1) {
            throw new PreconditionFailedException("Single: Number of Users playing incorrect");
        }

        Score score = new Score();
        Long userId = game.getCreatorUserId();
        score.setUserId(userId);
        scoreService.save(score);

    }

    @Override
    public void nextRoundPrep(GameEntity game, long currentTime){
        game.setRound(game.getRound() + 1);
        game.setRoundStart(currentTime + (game.getBreakDuration() * 1000)) ;
        game.setUsersAnswered(Arrays.asList());
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
