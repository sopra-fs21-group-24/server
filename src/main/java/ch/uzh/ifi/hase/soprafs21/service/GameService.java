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
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
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

    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository, 
    UserRepository userRepository, 
    QuestionRepository questionRepository, 
    ScoreService scoreService,
    LobbyService lobbyService 
    ) {
        this.questionRepository = questionRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.scoreService = scoreService;
        this.lobbyService = lobbyService;
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

    public GameEntity startGame(Long userId, Long gameId) {
        Optional<GameEntity> found = gameRepository.findById(gameId);
        if (found.isEmpty()) {
            throw new NotFoundException("Game Entity is not found, to start the game");
        } 

        // TODO
        // - evtl. lobby hier killen
        // - set questions?

        GameEntity game = found.get();

        if (!userId.equals(game.getCreatorUserId())) {
            throw new NotCreatorException("User starting the game is not the game-creator");
        }

        UserMode uMode = game.getUserMode();
        uMode.setScoreService(scoreService);
        uMode.start(game);

        game.setCurrentTime();
        game.setRound(game.getRound() + 1);

        return game;
    }

    public Long makeGuess(Answer answer) {
        long currentTime = System.currentTimeMillis();

        Optional<GameEntity> found = gameRepository.findById(answer.getGameId());
        if (found.isEmpty()) {
            throw new NotFoundException("Game Entity is not found");
        } 

        GameEntity game = found.get();

        List<Long> users = game.getUserIds();
        if (!users.contains(answer.getUserId())) {
            throw new UnauthorizedException("User is not a player of this game");
        }

        if (game.getRound() > 3) {
            throw new PreconditionFailedException("Rounds are exceeding max");
        }
            
        Optional<Question> foundQuestion = questionRepository.findById(answer.getQuestionId());
        if (foundQuestion.isEmpty()){
            throw new PreconditionFailedException("Question with this questionId is not found");
        } 
        
        List<Long> questions = game.getQuestions();
        if(!questions.contains(answer.getQuestionId())){
            throw new PreconditionFailedException("Questionid is not part of the game questions");
        } 

        Question question = foundQuestion.get();
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

    public int exitGame() {
        // TODO
        // exit Lobby
        // new Highscore?
        throw new UnsupportedOperationException();
    }

    public void moveLobbyUsers(GameEntity game){
        if (game.getLobbyId() != null){
            Lobby lobby = lobbyService.getLobbyWithId(game.getLobbyId());
            game.setUserIds(lobby.getUsers());
        }
    }

    public List<GameEntity> getAllGames(){
        return gameRepository.findAll();
    }


    public Question questionById(Long questionId) {
        Optional<Question> found = questionRepository.findById(questionId);
        if (found.isEmpty()) {
            throw new NotFoundException("");
        } else {
            return found.get();
        }
    }
}
