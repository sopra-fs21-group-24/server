package ch.uzh.ifi.hase.soprafs21.service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gameSetting;
import ch.uzh.ifi.hase.soprafs21.entity.userSetting;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
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

    public Optional<GameEntity> gameById(Long gameId) {
        return gameRepository.findById(gameId);
    }

    public GameEntity createGame(Long userId, userSetting uSetting, gameSetting gSetting, boolean publicStatus) {
        UserMode uMode;
        GameMode gMode;
        // TODO

        Optional<User> creator = userRepository.findById(userId);
        if (creator.isEmpty()) {
            // throw Error
        }

        if (uSetting == userSetting.SINGLEPLAYER) {
            uMode = new SinglePlayer();
        } else {
            uMode = new MultiPlayer();
        }

        switch (gSetting) {
        case CLOUDS:
            gMode = new Clouds();
            break;
        case PIXEL:
            gMode = new Pixelation();
            break;
        case TIME:
            gMode = new Time();
            break;
        default:
            gMode = new Time();
        }

        GameEntity game = new GameEntity();

        uMode.init(game, publicStatus);
        game.setUserMode(uMode);

        game.setGameMode(gMode);
        game.setCreatorUserId(userId);
        return game;
    }

    public void startGame(Long userId, Long gameId) {
        Optional<GameEntity> found = gameRepository.findById(gameId);
        if (found.isEmpty()) {
            throw new NotFoundException("Game Entity is not found");
        } else {
            GameEntity game = found.get();
            if (userId.equals(game.getCreatorUserId())) {
                throw new NotFoundException("User starting the game is not the game-creator");
            }

            UserMode uMode = game.getUserMode();
            uMode.start(game);

            // set questions?

            game.setCurrentTime();
            game.setRound(game.getRound() + 1);
        }
    }

    public int makeGuess(Long userId, Long gameId, float lon, float lat, float difficultyFactor) {
        long currentTime = System.currentTimeMillis();

        Optional<GameEntity> found = gameRepository.findById(gameId);
        if (found.isEmpty()) {
            throw new NotFoundException("Game Entity is not found");
        } else {
            GameEntity game = found.get();

            Set<Long> users = game.getUserIds();
            if (!users.contains(userId)) {
                throw new NotFoundException("User is not a player of this game");
            }

            if (game.getRound() > 3) {
                throw new NotFoundException("Rounds are exceeding max");
            }

            // check for timelegitimacy
            if(!isTimeValid(game, currentTime)){
                throw new NotFoundException("Request outside of round timeframe");
            }

            GameMode gMode = game.getGameMode();
            int tempScore = gMode.calculateScore(lon, lat, difficultyFactor);

            // Sofort Score holen? (table), per userid?
            Map<Long, Score> scores = game.getScores();
            Score score = scores.get(userId);
            score.setTempScore(tempScore);
            score.setTotalScore(score.getTotalScore() + tempScore);
            score.setLastLat(lat);
            score.setLastLong(lon);

            return tempScore;
        }

    }

    private boolean isTimeValid(GameEntity game, long currentTime) {
        // TODO
        return true;
    }

    public int exitGame() {
        // TODO
        // exit Lobby
        // new Highscore?
        throw new UnsupportedOperationException();
    }

    public Question allQuestions() {
        throw new UnsupportedOperationException();
    }

    public Question questionById() {
        throw new UnsupportedOperationException();
    }

}
