package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ScoreService scoreService;
    private final LobbyService lobbyService;
    private final UserService userService;

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, 
    UserRepository userRepository, 
    QuestionRepository questionRepository, 
    ScoreService scoreService,
    LobbyService lobbyService,
    UserService userService
    ) {
        this.questionRepository = questionRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.scoreService = scoreService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    public GameEntity gameById(Long gameId) {
        Optional<GameEntity> found = gameRepository.findById(gameId);
        if(found.isEmpty()){
            throw new NotFoundException("Game with this gameId does not exist");
        }
        return found.get();
    }

    public boolean existsGameByCreatorUserId(Long gameId) {
        Optional<GameEntity> found = gameRepository.findByCreatorUserId(gameId);
        if(found.isPresent()){
            throw new PreconditionFailedException("User has already created a game");
        } 
        return false;
    }

    public GameEntity createGame(GameEntity gameRaw, boolean publicStatus) {
        Long userId = gameRaw.getCreatorUserId();
        Optional<User> creator = userRepository.findById(userId);
        if (creator.isEmpty()) {
            throw new NotFoundException("[createGame] A user with this userId " + gameRaw.getCreatorUserId() + "doesn't exist");
        }
        existsGameByCreatorUserId(userId);

        UserMode uMode = gameRaw.getUserMode();
        uMode.setLobbyService(lobbyService);
        uMode.init(gameRaw, publicStatus);

        GameEntity game = gameRepository.save(gameRaw);
        gameRepository.flush();
        return game;
    }

    public GameEntity startGame(Long gameId) {
        Optional<GameEntity> found = gameRepository.findById(gameId);
        if (found.isEmpty()) {
            throw new NotFoundException("Game Entity is not found, to start the game");
        } 

        // TODO
        // - evtl. lobby hier killen
        // - set questions?

        GameEntity game = found.get();
        UserMode uMode = game.getUserMode();
        uMode.setScoreService(scoreService);
        uMode.start(game);

        game.setCurrentTime();
        game.setRound(game.getRound() + 1);

        return game;
    }

    public Long makeGuess(Answer answer) {
        long currentTime = System.currentTimeMillis();

        GameEntity game = gameById(answer.getGameId());

        if (game.getRound() > 3) {
            throw new PreconditionFailedException("Rounds are exceeding max");
        }
            
        List<Long> questions = game.getQuestions();
        if(!questions.contains(answer.getQuestionId())){
            throw new PreconditionFailedException("Questionid is not part of the game questions");
        } 

        Question question = questionById(answer.getQuestionId());
        answer.setCoordQuestion(question.getCoordinate());

        // calculate time score

        // check for timelegitimacy
        if(!isTimeValid(game, currentTime)){
            throw new PreconditionFailedException("Request outside of round timeframe");
        }

        GameMode gMode = game.getGameMode();
        Long tempScore = gMode.calculateScore(answer);

        Score score = scoreService.findById(answer.getUserId());
        score.setTempScore(tempScore);
        score.setTotalScore(score.getTotalScore() + tempScore);
        score.setLastCoordinate(answer.getCoordGuess());

        return tempScore;
    }


    private boolean isTimeValid(GameEntity game, long currentTime) {
        // TODO
        return true;
    }

    public int exitGame(GameEntity game) {
        // TODO
        // new Highscore?
        List<Long> userIds = game.getUserIds();
        for(Long userId : userIds){
            Score score = scoreService.findById(userId);
            Long totalScore = score.getTotalScore();
            User user = userService.getUserByUserId(userId);
            // TODO
            // hashtable für users
        }

        throw new UnsupportedOperationException();
    }

    public void moveLobbyUsers(GameEntity game){
        if (game.getLobbyId() != null){
            Lobby lobby = lobbyService.getLobbyById(game.getLobbyId());
            game.setUserIds(lobby.getUsers());
        }
    }

    public List<GameEntity> getAllGames(){
        return gameRepository.findAll();
    }

    public GameEntity update(GameEntity game, Boolean publicStatus){
        GameEntity gameLocal = gameById(game.getGameId());
        if(gameLocal.getRound() != 0){
            throw new PreconditionFailedException("Game has already started, Can not change running game");
        } 

        String nameUserModeLocal = gameLocal.getUserMode().getName();
        String nameUserModePut = game.getUserMode().getName();
        String nameGameModeLocal= gameLocal.getGameMode().getName();
        String nameGameModePut= game.getGameMode().getName();
        Lobby lobbyLocal = lobbyService.getLobbyById(gameLocal.getLobbyId());

        // evt. wegnehmen
        if(!nameUserModeLocal.equals(nameUserModePut)){
            gameLocal.setUserMode(game.getUserMode());
        }

        if(!nameGameModeLocal.equals(nameGameModePut)){
            gameLocal.setGameMode(game.getGameMode());
        }

        if(!lobbyLocal.getPublic().equals(publicStatus)){
            lobbyLocal.setPublic(publicStatus);
        }

        return gameLocal;
    }


    public Question questionById(Long questionId) {
        Optional<Question> found = questionRepository.findById(questionId);
        if (found.isEmpty()) {
            throw new NotFoundException("Question with this questionId is not found");
        } else {
            return found.get();
        }
    }
}
