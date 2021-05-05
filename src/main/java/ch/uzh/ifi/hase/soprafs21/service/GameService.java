package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final LeaderboardService leaderboardService;
    private final QuestionService questionService;
    private final ScoreService scoreService;
    private final LobbyService lobbyService;
    private final UserService userService;


    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository,
                       UserRepository userRepository,
                       LeaderboardService leaderboardService,
                       QuestionService questionService,
                       ScoreService scoreService,
                       LobbyService lobbyService,
                       UserService userService
    ) {
        this.leaderboardService = leaderboardService;
        this.questionService = questionService;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.scoreService = scoreService;
        this.lobbyService = lobbyService;
        this.userService = userService;
    }

    public User checkAuth(Map<String, String> header) {
        String token = header.get("token");
        try {
            return userService.getUserByToken(token);
        }
        catch (NotFoundException e) {
            throw new UnauthorizedException(e.getMessage());
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

    public void closeLooseEnds() {
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

    public boolean existsGameByCreatorModded(Long userId) {
        // TODO
        // remove this closeLooseEnds 
        // remove check for ended games
        closeLooseEnds();

        Optional<GameEntity> found = gameRepository.findByCreatorUserId(userId);
        if (found.isPresent()) {
            // remove this - leave if present -> preconditionError
            GameEntity game = found.get();
            if (game.getRound() == 4) {
                exitGame(game);
            }
            else {
                throw new PreconditionFailedException("User has already created a game");
            }
        }
        return false;
    }

    public GameEntity createGame(GameEntity gameRaw, boolean publicStatus) {
        Long userId = gameRaw.getCreatorUserId();
        Optional<User> creator = userRepository.findById(userId);
        if (creator.isEmpty()) {
            throw new NotFoundException("[createGame] A user with this userId " + gameRaw.getCreatorUserId() + "doesn't exist");
        }
        existsGameByCreatorModded(userId);

        GameEntity game = gameRepository.save(gameRaw);

        UserMode uMode = gameRaw.getUserMode();
        uMode.setLobbyService(lobbyService);
        uMode.init(gameRaw, publicStatus);

        return game;
    }

    public List<Long> createQuestionList(){
        // TODO
        // set of distinct questions, zukunft mit users
        long questionId;
        List<Long> questions = new ArrayList<>();

        Random random = new Random();
        for (int i=0; i < 3; i++){
            questionId = random.nextInt((int)questionService.count());
            questions.add(questionId);
        }
        return questions;
    }

    public GameEntity startGame(Long gameId) {

        GameEntity game = gameById(gameId);

        game.setQuestions(createQuestionList());

        game.setCurrentTime();
        game.setRound(game.getRound() + 1);


        UserMode uMode = game.getUserMode();
        uMode.setLobbyService(lobbyService);
        uMode.setScoreService(scoreService);
        uMode.start(game);

        return game;
    }

    public Score makeGuess(Answer answer) {
        long currentTime = System.currentTimeMillis();

        GameEntity game = gameById(answer.getGameId());

        // sanity check
        if (game.getRound() > 3) {
            throw new PreconditionFailedException("Rounds are exceeding max");
        }

        Long answerQuestionId = answer.getQuestionId();

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

        // set solution in answer
        Question question = questionService.questionById(answer.getQuestionId());
        answer.setCoordQuestion(question.getCoordinate());

        // timeFactor
        GameMode gMode = game.getGameMode();
        float timeFactor = gMode.calculateTimeFactor(game, currentTime);

        answer.setTimeFactor(timeFactor);


        // score calculation
        Long tempScore = gMode.calculateScore(answer);

        // user answered
        game.addUserAnswered(answer.getUserId());

        // save in Score
        Score score = scoreService.findById(answer.getUserId());
        score.setTempScore(tempScore);
        score.setTotalScore(score.getTotalScore() + tempScore);
        score.setLastCoordinate(answer.getCoordGuess());

        // gameContiune
        UserMode uMode = game.getUserMode();
        uMode.nextRoundPrep(game, currentTime);

        return score;
    }


    public void exitGame(GameEntity game) {
        // case 1: game has started
        if (game.getRound() > 0){
            ListIterator<Score> scores = scoresByGame(game);

            leaderboardService.updateLeaderboard(game.getGameMode().getName(), scores);

            while (scores.hasNext()) {
                Score score = scores.next();

                long totalScore = score.getTotalScore();
                User user = userService.getUserByUserId(score.getUserId());
                Map<String, Integer> highScores = user.getHighScores();
                String key = game.getGameMode().getName();

                Integer highest = highScores.get(key);

                if (totalScore > highest) {
                    highScores.put(key, (int)totalScore);
                    user.setHighScores(highScores);
                }
            }
        }
        // case 2: game has not started yet
        else {
            for(Long userId : game.getUserIds()){
                User user = userService.getUserByUserId(userId);
                user.setInLobby(false);
            }

            // case 3: delete lobby only if multiplayer
            UserMode uMode = game.getUserMode();
            if (uMode.getName().equals("Multiplayer")){
                lobbyService.deleteLobby(game.getLobbyId());
            }
        }            


        gameRepository.delete(game);
        gameRepository.flush();
        userRepository.flush();
    }

    public void exitGameUser(GameEntity game, User user) {

        Long userId = user.getId();

        // delete Score
        Score score = scoreService.findById(userId);
        scoreService.delete(score);

        // remove user
        List<Long> users = new ArrayList<>(game.getUserIds());
        users.remove(userId);
        game.setUserIds(users);

        if (users.size() == 1) {
            exitGame(game);
        } 
        else {
            MultiPlayer uMode = (MultiPlayer) game.getUserMode();
            uMode.adjustThreshold(game, user);
        }
    }

    public void moveLobbyUsers(GameEntity game,  Lobby lobby) {
        if (game.getLobbyId() != null) {
            game.setUserIds(lobby.getUsers());
        }
    }

    public List<GameEntity> getAllGames() {
        return gameRepository.findAll();
    }

    public GameEntity update(GameEntity game, Boolean publicStatus) {
        GameEntity gameLocal = gameById(game.getGameId());
        if (gameLocal.getRound() != 0) {
            throw new PreconditionFailedException("Game has already started, Can not change running game");
        }

        String nameUserModeLocal = gameLocal.getUserMode().getName();
        String nameUserModePut = game.getUserMode().getName();
        String nameGameModeLocal = gameLocal.getGameMode().getName();
        String nameGameModePut = game.getGameMode().getName();
        Lobby lobbyLocal = lobbyService.getLobbyById(gameLocal.getLobbyId());

        // evt. wegnehmen
        if (!nameUserModeLocal.equals(nameUserModePut)) {
            gameLocal.setUserMode(game.getUserMode());
        }

        if (!nameGameModeLocal.equals(nameGameModePut)) {
            gameLocal.setGameMode(game.getGameMode());
        }

        if (!lobbyLocal.getPublicStatus().equals(publicStatus)) {
            lobbyLocal.setPublicStatus(publicStatus);
        }

        return gameLocal;
    }

    public ListIterator<Score> scoresByGame(GameEntity game) {
        List<Long> userIds = game.getUserIds();
        ArrayList<Score> scores = new ArrayList<>();
        for (Long userId : userIds) {
            scores.add(scoreService.findById(userId));
        }
        return scores.listIterator();
    }

    
}
