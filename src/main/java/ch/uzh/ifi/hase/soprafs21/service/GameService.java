package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ScoreGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

@Service
@Transactional
public class GameService {

    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final Map<DeferredResult<GameGetDTO>, Long> singleGameRequests = new ConcurrentHashMap<>();
    private final Map<DeferredResult<List<ScoreGetDTO>>, Long> singleAllScoreRequests = new ConcurrentHashMap<>();

    private final GameRepository gameRepository;
    private final LeaderboardService leaderboardService;
    private final QuestionService questionService;
    private final ScoreService scoreService;
    private final LobbyService lobbyService;
    private final UserService userService;
    
    private final Random random = new Random();



    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       LeaderboardService leaderboardService,
                       QuestionService questionService,
                       ScoreService scoreService,
                       LobbyService lobbyService,
                       UserService userService
    ) {
        this.leaderboardService = leaderboardService;
        this.questionService = questionService;
        this.gameRepository = gameRepository;
        this.scoreService = scoreService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    public User checkAuth(Map<String, String> header) {
        String token = header.get("token");
        if (token == null){
            throw new UnauthorizedException("Token was not found");
        }
        try {
            return userService.getUserByToken(token);
        }
        catch (NotFoundException e) {
            throw new UnauthorizedException("User with this token was not found");
        }
    }

    public void checkPartofGame(GameEntity game, User user) {
        if (!game.getUserIds().contains(user.getId())) {
            throw new UnauthorizedException("Non player is trying to acess an only-player component");
        }
    }

    public void checkGameCreator(GameEntity game, User user) {
        if (!user.getId().equals(game.getCreatorUserId())) {
            throw new NotCreatorException("User is not the game-creator");
        }
    }

    public GameEntity gameById(Long gameId) {
        Optional<GameEntity> found = gameRepository.findById(gameId);
        if (found.isEmpty()) {
            throw new NotFoundException("Game with this gameId does not exist");
        }
        return found.get();
    }

    public List<Long> getQuestionsOfGame(GameEntity game) {
        return game.getQuestions();
    }

    @Scheduled(fixedDelay = 300000)
    public void closeLooseEnds() {
        logger.info("[HomemadeGarbageCollector] Closed open Games");
        List<GameEntity> endedGames = gameRepository.findByRoundEquals(4);
        Long currentTime = System.currentTimeMillis();
        for (GameEntity game : endedGames) {
            if ((game.getRoundStart() - currentTime) > 5 * 1000) {
                exitGame(game);
            }
        }
    }

    public Optional<GameEntity> gameByCreatorUserIdOptional(Long userId){
        return gameRepository.findByCreatorUserId(userId);
    }

    public void closeFinishedGames(Long userId) {
        Optional<GameEntity> found = gameRepository.findByCreatorUserId(userId);
        if (found.isPresent()) {
            GameEntity game = found.get();
            if (game.getRound() == 4) {
                exitGame(game);
            }
            else {
                throw new PreconditionFailedException("User has already created a game");
            }
        }
    }

    public GameEntity createGame(GameEntity gameRaw, boolean publicStatus) {
        Long userId = gameRaw.getCreatorUserId();

        // does user exist?
        userService.getUserByUserId(userId);

        // user owns another running/unclose game?
        closeFinishedGames(userId);

        GameEntity game = gameRepository.save(gameRaw);

        // Usermode specific Settings
        UserMode uMode = game.getUserMode();
        uMode.setLobbyService(lobbyService);
        uMode.init(game, publicStatus);
        gameRepository.flush();

        // callback
        lobbyService.handleLobbies();

        return game;
    }

    public List<Long> createQuestionList(){
        long questionId;
        List<Long> questions = new ArrayList<>();

        for (int i=0; i < 3; i++){
            questionId = random.nextInt((int)questionService.count());
            questions.add(questionId);
        }
        return questions;
    }

    public GameEntity startGame(Long gameId) {

        GameEntity game = gameById(gameId);

        // game has already started
        if (game.getRound() > 0){
            throw new PreconditionFailedException("Game has already started!");
        }

        // basic settings
        game.setQuestions(createQuestionList());
        game.setCurrentTime();
        game.setRound(1);

        // Usermode Settings
        UserMode uMode = game.getUserMode();
        uMode.setLobbyService(lobbyService);
        uMode.setScoreService(scoreService);
        uMode.start(game);

        // update callbacks 
        handleGame(game);
        handleScores(game);

        return game;
    }

    // ------------- Make Guess --------------- // 
    public void checkGuessPreconditions(GameEntity game, Answer answer){
        Long answerQuestionId = answer.getQuestionId();

        // sanity check
        if (game.getRound() > 3) {
            throw new PreconditionFailedException("Rounds are exceeding max");
        }

        // anwserQuestion in questions of game
        List<Long> questions = game.getQuestions();
        if (!questions.contains(answerQuestionId)) {
            throw new PreconditionFailedException("Questionid is not part of the game questions");
        }

        // question matching round
        if (!questions.get(game.getRound() - 1).equals(answerQuestionId)) {
            throw new PreconditionFailedException("Answer is not for the right Question");
        }

        // check if already answered
        if (game.getUsersAnswered().contains(answer.getUserId())) {
            throw new PreconditionFailedException("User has already answered this question");
        }

    }

    public Score apresGuess(GameEntity game, Answer answer, Long currentTime, Long tempScore) throws NotFoundException{
        // mark question as answered by "user"
        game.addUserAnswered(answer.getUserId());

        // save in Score
        Score score = scoreService.findById(answer.getUserId());
        score.setTempScore(tempScore);
        score.setTotalScore(score.getTotalScore() + tempScore);
        score.setLastCoordinate(answer.getCoordGuess());

        // prep
        UserMode uMode = game.getUserMode();
        uMode.nextRoundPrep(game, currentTime);

        // update callbacks 
        handleGame(game);
        handleScores(game);

        return score;
    }

    public Score makeGuess(Answer answer) {
        long currentTime = System.currentTimeMillis();
        GameEntity game = gameById(answer.getGameId());

        checkGuessPreconditions(game, answer);

        // set solution in answer
        Question question = questionService.questionById(answer.getQuestionId());
        answer.setCoordQuestion(question.getCoordinate());

        // timeFactor
        GameMode gMode = game.getGameMode();
        float timeFactor = gMode.calculateTimeFactor(game, currentTime);
        answer.setTimeFactor(timeFactor);

        // score calculation
        Long tempScore = gMode.calculateScore(answer);

        return apresGuess(game, answer, currentTime, tempScore);
    }

    public Score makeZeroScoreGuess(Answer answer){
        long currentTime = System.currentTimeMillis();
        GameEntity game = gameById(answer.getGameId());
        Long tempScore = 0L;

        // set solution in answer
        Question question = questionService.questionById(answer.getQuestionId());
        answer.setCoordQuestion(question.getCoordinate());

        return apresGuess(game, answer, currentTime, tempScore);
    }


    public void exitGame(GameEntity game) {
        // TODO
        // update personalHighscores in eine eigene method machen

        // case 1: game has started
        if (game.getRound() > 0){
            ListIterator<Score> scores = scoreService.scoresByGame(game);

            String gModeName = game.getGameMode().getName();
            leaderboardService.updateLeaderboard(gModeName, scores);

            while (scores.hasPrevious()) {
                Score score = scores.previous();

                long totalScore = score.getTotalScore();
                User user = userService.getUserByUserId(score.getUserId());
                Map<String, Integer> highScores = user.getHighScores();

                Integer highest = highScores.get(gModeName);

                if (totalScore > highest) {
                    highScores.put(gModeName, (int)totalScore);
                    user.setHighScores(highScores);
                }

                scoreService.delete(score);
                scores.remove();
            }
        }
        
        // case 2: game has not started yet
        else {
            // delete lobby only if multiplayer
            UserMode uMode = game.getUserMode();
            if (uMode.getName().equals("Multiplayer")){
                lobbyService.deleteLobby(game.getLobbyId());
                lobbyService.handleLobbies();
            }
        }            

        // callback game
        handleGame(game);

        gameRepository.delete(game);
        gameRepository.flush();

        // callback lobby
        lobbyService.handleLobbies();
    }

    public void exitGameUser(GameEntity game, User user) {
        Long userId = user.getId();

        // remove user
        List<Long> users = new ArrayList<>(game.getUserIds());
        users.remove(userId);
        game.setUserIds(users);

        // case: two already guessed, third leaves without
        List<Long> usersAnswered = game.getUsersAnswered();
        if (!usersAnswered.contains(user.getId())) {
            Answer answer = new Answer();
            answer.setGameId(game.getGameId());
            answer.setUserId(user.getId());
            answer.setCoordGuess(new Coordinate(null, null));

            // make 0 score guess
            apresGuess(game, answer, System.currentTimeMillis(), 0L);
        }

        // delete Score
        Score score = scoreService.findById(userId);
        scoreService.delete(score);

        // adjust the playercount for next round
        MultiPlayer uMode = (MultiPlayer) game.getUserMode();
        uMode.adjustThreshold(game, user);
    }

    public GameEntity update(GameEntity game, Boolean publicStatus) {
        GameEntity gameLocal = gameById(game.getGameId());
        if (gameLocal.getRound() != 0) {
            throw new PreconditionFailedException("Game has already started, Can not change running game");
        }

        String nameGameModeLocal = gameLocal.getGameMode().getName();
        String nameGameModePut = game.getGameMode().getName();
        Lobby lobbyLocal = lobbyService.getLobbyById(gameLocal.getLobbyId());

        if (!nameGameModeLocal.equals(nameGameModePut)) {
            gameLocal.setGameMode(game.getGameMode());
        }

        if (!lobbyLocal.getPublicStatus().equals(publicStatus)) {
            lobbyLocal.setPublicStatus(publicStatus);
        }

        gameRepository.saveAndFlush(gameLocal);

        // update callback
        handleGame(game);
        lobbyService.handleLobbies();
        lobbyService.handleLobby(lobbyService.getLobbyById(gameLocal.getLobbyId()), gameLocal.getGameMode());

        return gameLocal;
    }


    // ------------- Lobby long polling --------------- // 

    // --- handler --- //
    public void handleGame(GameEntity game){
        GameGetDTO gameDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
        for (Map.Entry<DeferredResult<GameGetDTO>, Long> entry : singleGameRequests.entrySet()){
           if(entry.getValue().equals(game.getGameId())){
               entry.getKey().setResult(gameDTO);
           }
        }
    }

    public void handleScores(GameEntity game){
        List<ScoreGetDTO> result = scoreService.getScoreGetDTOs(game);
        for (Map.Entry<DeferredResult<List<ScoreGetDTO>>, Long> entry : singleAllScoreRequests.entrySet()){
           if(entry.getValue().equals(game.getGameId())){
               entry.getKey().setResult(result);
           }
        }
    }

    // --- helper --- //


    public void removeRequestFromGameMap(DeferredResult<GameGetDTO> request){
        singleGameRequests.remove(request);
    }

    public void addRequestToQueueGameMap(DeferredResult<GameGetDTO> request, Long gameId){
       singleGameRequests.put(request, gameId);
    }

    public void removeRequestFromAllScoreMap(DeferredResult<List<ScoreGetDTO>> request){
    singleAllScoreRequests.remove(request);
    }

    public void addRequestAllScoreMap(DeferredResult<List<ScoreGetDTO>> request, Long gameId){
       singleAllScoreRequests.put(request, gameId);
    }
    
}
