package ch.uzh.ifi.hase.soprafs21.entity.usermodes;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;

public class MultiPlayer extends UserMode {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name = "Multiplayer";
    private int playersFinished = 0;

    @Override
    public void init(GameEntity game, boolean publicStatus) {
        Lobby lobby = new Lobby();
        Long creator = game.getCreatorUserId();
        lobby.setCreator(creator);
        lobby.setPublicStatus(publicStatus);
        lobby.setGameId(game.getGameId());
        lobby = lobbyService.createLobby(lobby);
        game.setLobbyId(lobby.getId());
        game.setUserIds(Arrays.asList(creator));
    }

    @Override
    public void start(GameEntity game) {
        // set roundStart time 
        super.start(game);

        Lobby lobby = lobbyService.getLobbyById(game.getLobbyId());
        List<Long> users = new ArrayList<>(lobby.getUsers());
        
        if (users.size() < 2) {
            throw new PreconditionFailedException("User is starting the game prematurly");
        }

        game.setUserIds(users);
        game.setThreshold(users.size()-1);

        for (Long user : users) {
            Score score = new Score();
            score.setUserId(user);
            scoreService.save(score);
        }

        // killt lobby
        lobbyService.deleteLobby(game.getLobbyId());
        
    }  

    public void nextRoundPrep(GameEntity game, long currentTime) {
        int threshold = game.getThreshold();

        if (this.playersFinished == threshold){
            game.setRoundStart(currentTime + (game.getBreakDuration() * 1000));
            game.setRound(game.getRound() + 1);
            game.setUsersAnswered(Arrays.asList());
            this.playersFinished = 0;

        } else {
            this.playersFinished++; 
        }
    }

    public void adjustThreshold(GameEntity game, User user){
        int threshold = game.getThreshold();

        if (game.getUsersAnswered().contains(user.getId())){
            this.playersFinished--;
        } 

        game.setThreshold(threshold - 1);
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
