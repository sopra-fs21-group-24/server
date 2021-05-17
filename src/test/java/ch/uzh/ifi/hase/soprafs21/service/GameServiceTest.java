package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

public class GameServiceTest {
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // sample User
        user = new User();
        user.setId(2L);
        user.setUsername("Max");
        user.setToken("maximilian");
        user.setPassword("sicher");


        // given
        testGame = new GameEntity();
        testGame.setGameId(1L);
        testGame.setGameMode(new Time());
        testGame.setLobbyId(null);
        testGame.setBreakDuration(40);
        testGame.setUserMode(new SinglePlayer());


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

        // fail on existsGameCreatorModded
        // fail on getUsermode/setLobbyService
        
    }

    void createGameAlreadyExists() {
        // setting missing components
        testGame.setCreatorUserId(user.getId());

        // fail on existsGameCreatorModded
        
    }

    @Test
    void createGameNoUserModeFailure() {
        // setting missing components
        testGame.setCreatorUserId(user.getId());

        // fail on getUsermode/setLobbyService
        
    }


    @Test
    void createGameMultiplayerFailure() {
        // setting missing components
        testGame.setCreatorUserId(user.getId());

        // when(userService.getUserByUserId(Mockito.any())).thenThrow(NotFoundException.class);
        // assertThrows(NotFoundException.class, () -> gameService.createGame(testGame, true));

        // fail on initi 
        
    }

    @Test
    void createGameSingleplayerFailure() {
        // setting missing components
        testGame.setCreatorUserId(user.getId());

        // when(userService.getUserByUserId(Mockito.any())).thenThrow(NotFoundException.class);
        // assertThrows(NotFoundException.class, () -> gameService.createGame(testGame, true));

        // fail on initi 
    }

    @Test
    void createQuestionList() {
    }

    // StartGame Functionality
    @Test
    void startGame() {
    }

    // MakeGuess Functionality
    @Test
    void makeGuess() {
    }

    // exitGame Functionality
    @Test
    void exitGame() {
    }

    // exitGame Functionality
    @Test
    void exitGameUser() {
    }

    @Test
    void moveLobbyUsers() {
    }

    @Test
    void scoresByGame() {
    }

    @Test
    void closeLooseEnds() {
    }

    @Test
    void update() {
    }

    @Test
    void handleGame() {
    }

    @Test
    void handleScores() {
    }

    @Test
    void getScoreGetDTOs() {
    }

    @Test
    void questionSolution() {
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
