package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
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

        GameEntity game = gameById(gameId);

        // HardCoded Question
        Question question = new Question();
        question.setZoomLevel(1);
        question.setCoordinate(new Coordinate(1.0, 2.0));
        questionRepository.saveAndFlush(question);
        game.setQuestions(Arrays.asList(question.getQuestionId()));

        UserMode uMode = game.getUserMode();
        uMode.setScoreService(scoreService);
        uMode.start(game);

        game.setCurrentTime();
        game.setRound(game.getRound() + 1);

        return game;
    }

    public Score makeGuess(Answer answer) {
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

        // TODO: calculate time score
        answer.setTimeFactor(1.0f);

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

        return score;
    }


    private boolean isTimeValid(GameEntity game, long currentTime) {
        // TODO
        return true;
    }

    public void exitGame(GameEntity game) {
        ListIterator<Score> scores = scoresByGame(game);

        while(scores.hasNext()){
            Score score = scores.next();

            Long totalScore = score.getTotalScore();
            User user = userService.getUserByUserId(score.getUserId());
            Map<String, Integer> highScores = user.getHighScores();
            String key = game.getGameMode().getName();

            Integer highest = highScores.get(key);

            if (totalScore > highest){
                highScores.put(key, totalScore.intValue());
                user.setHighScores(highScores);
            }
            
        }

        // is this the right behavior?
        gameRepository.delete(game);
        gameRepository.flush();
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
    
    public ListIterator<Score> scoresByGame(GameEntity game){
        List<Long> userIds = game.getUserIds();
        ArrayList<Score> scores = new ArrayList<>(); 
        for(Long userId : userIds){
            scores.add(scoreService.findById(userId));
        }
        return scores.listIterator();
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
