package ch.uzh.ifi.hase.soprafs21.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.gameSetting;
import ch.uzh.ifi.hase.soprafs21.entity.userSetting;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public Optional<GameEntity> gameById(Long gameId){
        return gameRepository.findById(gameId);
    }

    public GameEntity createGame(Long userId, userSetting uSetting, gameSetting gSetting){
        UserMode uMode;
        GameMode gMode;
        // TODO

        Optional<User> creator = userRepository.findById(userId);
        if (creator.isEmpty()){
           // throw Error 
        }

        if(uSetting == userSetting.SINGLEPLAYER){
            uMode = new SinglePlayer();
        } else { 
            uMode = new MultiPlayer();
        }

        switch(gSetting){
            case CLOUDS:
                gMode = new Clouds();
                break;
            case PIXEL:
                gMode = new Pixelation();
                break;
            case TIME:
                gMode = new Time();
                break;
        }

        GameEntity game = new GameEntity();
        game.setGameMode(gMode);
        game.setUserMode(uMode);
        game.setCreatorUserId(userId);
        return game;
    }

    public Game exitGame(){
        throw new UnsupportedOperationException();
    }
    public Question allQuestions(){
        throw new UnsupportedOperationException();
    }

    public Question questionById(){
        throw new UnsupportedOperationException();
    }

    public Long makeGuess(){
        throw new UnsupportedOperationException();
    }

}
