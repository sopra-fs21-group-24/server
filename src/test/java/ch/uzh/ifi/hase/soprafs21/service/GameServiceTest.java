package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

public class GameServiceTest {
    @Mock
    private ScoreService scoreService;

    @Mock
    private QuestionService questionService;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private UserService userService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GameService gameService;

    private GameEntity testGame;
    private User user;
    private Answer answer;
    private Question question;
    private Score score;
    private Lobby lobby;



    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // sample User
        user = new User();
        user.setId(2L);
        user.setUsername("Max");
        user.setToken("maximilian");
        user.setPassword("sicher");

        // sampe Anwser
        answer = new Answer();
        answer.setGameId(1L);
        answer.setUserId(1L);
        answer.setCoordGuess(new Coordinate(1.0, 1.2));
        answer.setQuestionId(0L);

        question = new Question();
        question.setCoordinate(new Coordinate(-44.247274, 168.828488));
        question.setZoomLevel(15);
        question.setQuestionId(10L);

        score = new Score();
        score.setTempScore(100L);
        score.setTotalScore(50L);
        score.setLastCoordinate(new Coordinate(15.15,15.15));

        // given
        testGame = new GameEntity();
        testGame.setCreatorUserId(1L);
        testGame.setGameId(1L);
        testGame.setGameMode(new Time());
        testGame.setLobbyId(null);
        testGame.setBreakDuration(40);
        testGame.setUserMode(new SinglePlayer());
        List<Long> userAnsweredList = new ArrayList<>();
        userAnsweredList.add(user.getId());
        testGame.setUsersAnswered(userAnsweredList);
        testGame.setCurrentTime();
        testGame.setRoundStart(System.currentTimeMillis()-10);


        lobby = new Lobby();
        lobby.setCreator(9L);
        lobby.setId(14L);
        lobby.setGameId(testGame.getGameId());
        lobby.setPublicStatus(true);

        // user list initial
        List<Long> usersInitial = new ArrayList<>();
        usersInitial.add(testGame.getCreatorUserId());
        testGame.setUserIds(usersInitial);


        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(gameRepository.save(Mockito.any())).thenReturn(testGame);
    }

    @AfterEach
    void tearDown() {
    }


    // Some Checks and Helper functions
    @Test
    void checkAuthSuccess() {
        // sample header, not null for token
        Map<String, String> header = new HashMap<>();
        header.put("token", "maximilian");

        // precondition: invalid input in header gets catched in userService tests
        when(userService.getUserByToken(Mockito.any())).thenReturn(user);

        assertDoesNotThrow(() -> gameService.checkAuth(header));
    }

    @Test
    void checkAuthFail() {
        // sample header, not null for token
        Map<String, String> header = new HashMap<>();
        header.put("token", "");

        // precondition: invalid input in header gets catched in userService tests
        when(userService.getUserByToken(Mockito.any())).thenThrow(new NotFoundException("Failed"));

        // logic and token not in header
        assertAll( 
            () -> assertThrows(UnauthorizedException.class, () -> gameService.checkAuth(header)),
            () -> assertThrows(UnauthorizedException.class, () -> gameService.checkAuth(new HashMap<>()))
        );
    }

    @Test
    void checkPartofGameSuccess() {
        // add user to game (global)
        List<Long> userIds = new ArrayList<>();
        userIds.add(user.getId());
        testGame.setUserIds(userIds);

        assertDoesNotThrow(() -> gameService.checkPartofGame(testGame, user));

    }

    void checkPartofGameFailure() {
        // user wich is not in game
        assertThrows(UnauthorizedException.class,  () -> gameService.checkPartofGame(testGame, user));
    }

    @Test
    void checkGameCreatorSuccess() {
        // user (global) is gamecreator
        testGame.setCreatorUserId(user.getId());

        assertDoesNotThrow(() -> gameService.checkGameCreator(testGame, user));
    }

    @Test
    void checkGameCreatorFailure() {
        // user wich is not the game creator
        assertThrows(NotCreatorException.class,  () -> gameService.checkGameCreator(testGame, user));
    }

    @Test
    void gameByIdSuccess() {
        // precondition on database
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        
        assertDoesNotThrow(() -> gameService.gameById(testGame.getGameId()));
        // there is no need to check correctness of return
    }

    @Test
    void gameByIdFailure() {
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class,  () -> gameService.gameById(111L));
    }

    // ExistsGameByCreatorModded
    @Test
    void existsGameByCreatorModded() {
    }

    // Create Game Functionality
    @Test
    void createGameGeneralSuccess() {
        // setting missing components
        testGame.setCreatorUserId(user.getId()); // visual, but userService doesn't return

        // sanity check
        GameEntity game = assertDoesNotThrow(() -> gameService.createGame(testGame, true));

        // userlist of one user (host)
        List<Long> expectedList = new ArrayList<>();
        expectedList.add(testGame.getCreatorUserId());
        assertEquals(expectedList, game.getUserIds());

        // basic stuff
        assertEquals(testGame.getGameId(), game.getGameId());       
        assertEquals(testGame.getGameMode(), game.getGameMode());
        assertEquals(testGame.getLobbyId(), game.getLobbyId());
        assertEquals(testGame.getBreakDuration(), game.getBreakDuration());
        assertEquals(testGame.getUserMode(), game.getUserMode());

    }

    @Test
    void createGameMultiplayerSuccess() {
        // setting missing components
        testGame.setCreatorUserId(user.getId()); // visual, but userService doesn't return
        testGame.setUserMode(new MultiPlayer());


        // dummy lobby
        Lobby lobby = new Lobby();
        lobby.setId(1337L);
        lobby.setCreator(testGame.getCreatorUserId());
        lobby.setPublicStatus(true);
        lobby.setGameId(testGame.getGameId());
        when(lobbyService.createLobby(Mockito.any())).thenReturn(lobby);

        // sanity check
        GameEntity game = assertDoesNotThrow(() -> gameService.createGame(testGame, true));

        // checks on returned object
        assertNotNull(game.getLobbyId());

        // check general again 
        createGameGeneralSuccess();
    }

    @Test
    void createGameSingleplayerSuccess() {
        // setting missing components
        testGame.setCreatorUserId(user.getId()); // visual, but userService doesn't return

        // sanity check
        GameEntity game = assertDoesNotThrow(() -> gameService.createGame(testGame, true));

        // checks on returned object
        assertNull(game.getLobbyId());

        // check general again
        createGameGeneralSuccess();
    }

    @Test
    void createGameFailureOnUser() {
        // setting missing components
        testGame.setCreatorUserId(user.getId());

        // creatorid is not in the database 
        when(userService.getUserByUserId(Mockito.any())).thenThrow(NotFoundException.class);

        // user is not in db
        assertThrows(NotFoundException.class, () -> gameService.createGame(testGame, true));
    }

    @Test
    void createGameAlreadyExists() {
        // setting missing components
        testGame.setCreatorUserId(user.getId());

        // simulate: return existing game in db
        when(gameRepository.findByCreatorUserId(Mockito.any())).thenReturn(Optional.of(testGame));

        // fail on existsGameCreatorModded
        // newGame with same creatorUserId in db
        GameEntity newGame = new GameEntity();
        newGame.setCreatorUserId(user.getId());
        newGame.setGameId(11L);
        newGame.setGameMode(new Time());
        newGame.setLobbyId(null);
        newGame.setBreakDuration(40);
        newGame.setUserMode(new SinglePlayer());

        assertThrows(PreconditionFailedException.class, () -> gameService.createGame(newGame, true));
    }

    @Test
    void createQuestionList() {
        // test not necessary
        when(questionService.count()).thenReturn(1L);
        
        List<Long> expected = new ArrayList<>();
        expected.add(0L);
        expected.add(0L);
        expected.add(0L);

        assertEquals(gameService.createQuestionList(), expected);
    }

    // StartGame Functionality
    // TODO
    @Test
    void startGameGeneralSucess() {
        // given: game is in system 
        when(questionService.count()).thenReturn(3L);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));

        gameService.startGame(testGame.getGameId());

        assertAll(
            () -> assertEquals(testGame.getRound(), 1),
            () -> assertNotNull(testGame.getRoundStart()),
            () -> assertNotNull(testGame.getGameStartTime()),
            () -> assertNotNull(testGame.getQuestions())
        );

    }

    @Test
    void startGameSinglePlayerSuccess() { 
        // Singleplayer settings
        List<Long> expected = new ArrayList<>();
        expected.add(testGame.getCreatorUserId());

        // general check
        startGameGeneralSucess();

        // singleplayer specific tests
        assertEquals(expected, testGame.getUserIds());
    }

    @Test
    void startGameSinglePlayerFailure() { 
        // singleplayer failure
        testGame.getUserIds().add(3L);

        // given game in system
        when(questionService.count()).thenReturn(3L);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));

        assertThrows(PreconditionFailedException.class, () ->  gameService.startGame(testGame.getGameId()));
    }

    @Test
    void startGameMultiPlayerSuccess() { 
        // multiplayer settings
        testGame.setUserMode(new MultiPlayer());

        List<Long> expected = new ArrayList<>();
        expected.add(testGame.getCreatorUserId());
        expected.add(3L);

        Lobby lobby = new Lobby();
        lobby.setCreator(testGame.getCreatorUserId());
        lobby.setGameId(testGame.getGameId());
        lobby.setPublicStatus(true);
        lobby.setUsers(expected);

        // given
        when(lobbyService.getLobbyById(Mockito.any())).thenReturn(lobby);

        // general check
        startGameGeneralSucess();

        // multiplayer specifc tests
        assertAll(
            () -> assertEquals(expected, testGame.getUserIds()),
            () -> assertNull(testGame.getLobbyId())
        );

    }

    @Test
    void startGameInvalidGameIdFailure() {
        // given
        when(questionService.count()).thenReturn(3L);

        // invalid gameId
        Long gameId = 1337L;

        // throws exception
        assertThrows(NotFoundException.class, () -> gameService.startGame(gameId));;

    }

    @Test
    void startGameAlreadyStartedFailure() {
        // given
        when(questionService.count()).thenReturn(3L);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));

        // start game
        gameService.startGame(testGame.getGameId());

        // should throw error
        assertThrows(PreconditionFailedException.class, () -> gameService.startGame(testGame.getGameId()));;
    }

    // MakeGuess Functionality
    @Test
    void makeGuessGeneralSucess() {

        //set some things for passing of checks, check for success
        List<Long> questionsList = new ArrayList<Long>();
        questionsList.add(question.getQuestionId());
        questionsList.add(11L);
        testGame.setQuestions(questionsList);
        answer.setQuestionId(1L);
        testGame.setRound(1);
        answer.setQuestionId(question.getQuestionId());

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);

        Score returnedScore = gameService.makeGuess(answer);
        assertTrue(returnedScore instanceof Score);
        assertEquals(returnedScore.getTotalScore(), score.getTotalScore());
        assertEquals(returnedScore.getLastCoordinate(), score.getLastCoordinate());

    }

    @Test
    void makeGuessTimeSuccess() {


        //longer time to answer - 50 score
        testGame.setRoundStart(System.currentTimeMillis()-20000);

        //set some things for passing of checks, check for success
        List<Long> questionsList = new ArrayList<Long>();
        questionsList.add(question.getQuestionId());
        questionsList.add(11L);
        testGame.setQuestions(questionsList);
        answer.setQuestionId(1L);
        testGame.setRound(1);
        testGame.setGameMode(new Time());
        answer.setQuestionId(question.getQuestionId());

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);


        long scorePreChange = score.getTotalScore();
        Score returnedScore = gameService.makeGuess(answer);
        assertTrue(returnedScore instanceof Score);
        assertEquals(returnedScore.getTotalScore(), score.getTotalScore());
        System.out.println((returnedScore.getTotalScore()));
        assertEquals(returnedScore.getUserId(), score.getUserId());
        assertEquals(answer.getDifficultyFactor(),1);
        assertEquals(returnedScore.getLastCoordinate(), score.getLastCoordinate());

    }

    @Test
    void makeGuessCloudsSuccess() {


        //set some things for passing of checks, check for success
        List<Long> questionsList = new ArrayList<Long>();
        questionsList.add(question.getQuestionId());
        questionsList.add(11L);
        testGame.setQuestions(questionsList);
        answer.setQuestionId(1L);
        testGame.setRound(1);
        testGame.setGameMode(new Clouds());
        answer.setQuestionId(question.getQuestionId());

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);

        Score returnedScore = gameService.makeGuess(answer);
        assertTrue(returnedScore instanceof Score);
        assertEquals(returnedScore.getTotalScore(), score.getTotalScore());
        assertEquals(returnedScore.getLastCoordinate(), score.getLastCoordinate());

    }

    @Test
    void makeGuessPixelationSuccess() {
        //set some things for passing of checks, check for success
        answer.setQuestionId(1L);
        testGame.setRound(1);
        testGame.setGameMode(new Pixelation());

        answer.setQuestionId(question.getQuestionId());

        List<Long> questionsList = new ArrayList<Long>();
        questionsList.add(question.getQuestionId());
        questionsList.add(11L);
        testGame.setQuestions(questionsList);

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);

        Score returnedScore = gameService.makeGuess(answer);
        assertTrue(returnedScore instanceof Score);
        assertEquals(returnedScore.getTotalScore(), score.getTotalScore());
        assertEquals(returnedScore.getLastCoordinate(), score.getLastCoordinate());

    }

    @Test
    void makeGuessTimeFailure_TakeToLongToAnswer() {
        //set round start to far in the past
        testGame.setRoundStart(System.currentTimeMillis()-200000);

        //set some things for passing of checks, check for success
        List<Long> questionsList = new ArrayList<Long>();
        questionsList.add(question.getQuestionId());
        questionsList.add(11L);
        testGame.setQuestions(questionsList);
        answer.setQuestionId(1L);
        testGame.setRound(1);
        testGame.setGameMode(new Time());
        answer.setQuestionId(question.getQuestionId());

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);


        assertThrows(PreconditionFailedException.class, () ->  gameService.makeGuess(answer));
    }

    @Test
    void makeGuessCloudsFailure() {

        //empty question list given will throw error
        List<Long> questionsList = new ArrayList<Long>();
        testGame.setQuestions(questionsList);
        answer.setQuestionId(1L);
        testGame.setRound(1);
        testGame.setGameMode(new Clouds());
        answer.setQuestionId(question.getQuestionId());

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);


        assertThrows(PreconditionFailedException.class, () ->  gameService.makeGuess(answer));

    }

    @Test
    void makeGuessPixelationFailure() {
        //Wrong question id, rest stays the same
        List<Long> questionsList = new ArrayList<Long>();
        questionsList.add(88L);
        questionsList.add(99L);
        testGame.setQuestions(questionsList);
        answer.setQuestionId(1L);
        testGame.setRound(3);
        testGame.setGameMode(new Pixelation());
        answer.setQuestionId(question.getQuestionId());


        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);


        assertThrows(PreconditionFailedException.class, () ->  gameService.makeGuess(answer));

    }

    @Test
    void makeZeroScoreGuessSuccess() {


        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);
        Score returnedScore = gameService.makeZeroScoreGuess(answer);

        assertTrue(returnedScore instanceof Score);
        assertEquals(score.getTotalScore(), returnedScore.getTotalScore());
        //check if score 0 was given
        assertEquals(0, returnedScore.getTempScore());


    }

    @Test
    void checkGuessPreconditionsSucess() {
    }

    @Test
    void checkGuessPreconditionsFailure() {
    }

    @Test
    void apresGuess() {
    }


    //--------

    // exitGame Functionality
    @Test
    void exitGame() {
    }

    // exitGame Functionality
    @Test
    void exitGameUser() {
        testGame.setUserMode(new MultiPlayer());

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testGame));
        when(scoreService.findById(Mockito.any())).thenReturn(score);

        gameService.exitGameUser(testGame, user);
        List<Long> preExit = testGame.getUserIds();
        preExit.remove(user.getId());
        //check if user was removed from userlist
        assertEquals(testGame.getUserIds(), preExit);

    }

    @Test
    void closeLooseEnds() {
    }

    @Test
    void update() {

        //create new game which is the "template" for the one that has to be updated.
        GameEntity testGame2 = new GameEntity();
        testGame2.setCreatorUserId(13L);
        testGame2.setGameId(testGame.getGameId());
        testGame2.setGameMode(new Clouds());
        testGame2.setLobbyId(null);
        testGame2.setBreakDuration(40);
        testGame2.setUserMode(new SinglePlayer());


        testGame2.setRound(0);
        testGame.setRound(0);

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(testGame.getGameId())).thenReturn(Optional.ofNullable(testGame));
        when(gameRepository.findById(testGame2.getGameId())).thenReturn(Optional.ofNullable(testGame2));
        when(scoreService.findById(Mockito.any())).thenReturn(score);
        when(lobbyService.getLobbyById(Mockito.any())).thenReturn(lobby);


        GameEntity gameReturned =  gameService.update(testGame2, false);
        //check if gameRepo was called en game was updated in the database.
        Mockito.verify(gameRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        //check if GameMode was updated to Clouds
        assertEquals(gameReturned.getGameMode().getName(),"Clouds");
        //check if lobby was set to private
        assertEquals(lobby.getPublicStatus(),false);

    }


    @Test
    void update_fail() {

        //create new game which is the "template" for the one that has to be updated.
        GameEntity testGame2 = new GameEntity();
        testGame2.setCreatorUserId(13L);
        testGame2.setGameId(testGame.getGameId());
        testGame2.setGameMode(new Clouds());
        testGame2.setLobbyId(null);
        testGame2.setBreakDuration(40);
        testGame2.setUserMode(new SinglePlayer());

        //Game has already started
        testGame.setRound(1);
        testGame2.setRound(1);

        when(questionService.count()).thenReturn(3L);
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(gameRepository.findById(testGame.getGameId())).thenReturn(Optional.ofNullable(testGame));
        when(gameRepository.findById(testGame2.getGameId())).thenReturn(Optional.ofNullable(testGame2));
        when(scoreService.findById(Mockito.any())).thenReturn(score);
        when(lobbyService.getLobbyById(Mockito.any())).thenReturn(lobby);


        //check if error is thrown, when i try to update an already started game
        assertThrows(PreconditionFailedException.class, () ->  gameService.update(testGame2, false));


    }



    @Test
    void handleGame() {
    }

    @Test
    void handleScores() {
    }

    @Test
    void removeRequestFromGameMap() {
        // wrapper around util.Map method, no need for tests
    }

    @Test
    void addRequestToQueueGameMap() {
        // wrapper around util.Map method, no need for tests
    }

    @Test
    void removeRequestFromAllScoreMap() {
        // wrapper around util.Map method, no need for tests
    }

    @Test
    void addRequestAllScoreMap() {
        // wrapper around util.Map method, no need for tests
    }

    @Test
    void getQuestionsOfGame() {
        // wrapper around getter, no need for tests
    }

    @Test
    void gameByCreatorUserIdOptional() {
        // wrapper around getter, no need for tests
    }

    @Test
    void getAllGames() {
        // wrapper around getter, no need for tests
    }

}
