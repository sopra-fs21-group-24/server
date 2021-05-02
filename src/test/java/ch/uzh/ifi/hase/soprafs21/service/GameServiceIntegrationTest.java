package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.SinglePlayer;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class GameServiceIntegrationTest {

    @Qualifier("gameRepository")
    @Autowired
    private GameRepository gameRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        lobbyRepository.deleteAll();
        gameRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void createGame_creatorExists() {

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUsername");
        testUser.setInLobby(true);

        User createdUser = userService.createUser(testUser);

        GameEntity game = new GameEntity();
        game.setRound(1);
        game.setGameMode(new Time());
        game.setCreatorUserId(testUser.getId());
        game.setUserMode(new SinglePlayer());
        game.setLobbyId(1L);
        game.setBreakDuration(40);


        // when

        GameEntity createdGame = gameService.createGame(game, true);

        // then
        assertEquals(game.getRound(), createdGame.getRound());
        assertEquals(game.getGameMode(), createdGame.getGameMode());
        assertEquals(game.getCreatorUserId(), createdGame.getCreatorUserId());
        assertEquals(game.getUserMode(), createdGame.getUserMode());
        assertEquals(game.getLobbyId(), createdGame.getLobbyId());
        assertEquals(game.getBreakDuration(), createdGame.getBreakDuration());

    }

    @Test
    public void createGame_creatorNotExists() {

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUsername");
        testUser.setInLobby(true);

        User createdUser = userService.createUser(testUser);

        GameEntity game = new GameEntity();
        game.setRound(1);
        game.setGameMode(new Time());
        game.setCreatorUserId(69L);
        game.setUserMode(new SinglePlayer());
        game.setLobbyId(1L);
        game.setBreakDuration(40);

        // when
        assertThrows(NotFoundException.class, () -> gameService.createGame(game, true));
    }


    @Test
    public void exitGame_success() {
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUsername");
        testUser.setInLobby(true);
        HashMap highScores = new HashMap<>();
        highScores.put("Time", 0);
        highScores.put("Clouds", 0);
        highScores.put("Pixelation", 0);
        testUser.setHighScores(highScores);

        User createdUser = userService.createUser(testUser);

        Score testScore= new Score();
        testScore.setTempScore(1L);
        testScore.setTotalScore(5L);
        testScore.setUserId(createdUser.getId());
        testScore.setLastCoordinate(new Coordinate(4.5,3.2));
        scoreService.save(testScore);

        GameEntity game = new GameEntity();
        game.setRound(1);
        game.setGameMode(new Time());
        game.setCreatorUserId(createdUser.getId());
        game.setUserMode(new SinglePlayer());
        game.setLobbyId(1L);
        game.setBreakDuration(40);


        // when

        GameEntity createdGame = gameService.createGame(game, true);
        gameService.exitGame(createdGame);

        // then
        assertTrue(gameRepository.findById(createdGame.getGameId()).isEmpty()==true);

    }

    @Test
    public void movePlayerFromLobbyToGame_success() {

        // Create first User / Creator
        User firstUser = new User();
        firstUser.setUsername("testUsername");
        firstUser.setPassword("password");
        firstUser.setInLobby(false);
        User createdUser1 = userService.createUser(firstUser);

        // Create second User
        User secondUser = new User();
        secondUser.setPassword("password");
        secondUser.setUsername("testUsername2");
        secondUser.setInLobby(false);
        User createdUser2 = userService.createUser(secondUser);

        // Create Game Entity linking to the Creator
        GameEntity game = new GameEntity();
        game.setRound(1);
        game.setGameMode(new Time());
        game.setCreatorUserId(firstUser.getId());
        game.setUserMode(new SinglePlayer());
        game.setBreakDuration(40);
        GameEntity createdGame = gameService.createGame(game, true);

        // Create Lobby and linking it to Game & User
        Lobby lobby = new Lobby();
        lobby.setRoomKey(3000L);
        lobby.setCreator(firstUser.getId());
        lobby.setGameId(game.getGameId());
        lobby.setPublicStatus(true);
        Lobby createdLobby = lobbyService.createLobby(lobby);
        // Link Lobby to Game
        createdGame.setLobbyId(createdLobby.getId());
        // Add second user to lobby
        lobbyService.addUserToExistingLobby(createdUser2,lobby);

        // when
        // Transfer Users to Game
        gameService.moveLobbyUsers(game, createdLobby);

        // then
        assertArrayEquals(createdGame.getUserIds().toArray(), lobby.getUsers().toArray());
    }

}
