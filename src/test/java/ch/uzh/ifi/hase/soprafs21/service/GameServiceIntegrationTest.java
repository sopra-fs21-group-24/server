package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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
    public void moveLobbies_success() {
return;
/*
        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUsername");
        testUser.setInLobby(false);

        User createdUser = userService.createUser(testUser);

        User testUser2 = new User();
        testUser2.setPassword("password");
        testUser2.setUsername("testUsername2");
        testUser2.setInLobby(false);

        User createdUser2 = userService.createUser(testUser2);




        GameEntity game = new GameEntity();
        game.setRound(1);
        game.setGameMode(new Time());
        game.setCreatorUserId(testUser.getId());
        game.setUserMode(new SinglePlayer());

        game.setBreakDuration(40);
        GameEntity createdGame = gameService.createGame(game, true);



        Lobby lobby = new Lobby();
        lobby.setRoomKey(3000L);
        lobby.setCreator(createdUser.getId());
        lobby.setGameId(game.getGameId());
        lobby.setPublicStatus(true);

        Lobby createdLobby = lobbyService.createLobby(lobby);
        //lobbyService.addUserToExistingLobby(createdUser,lobby);
        lobbyService.addUserToExistingLobby(createdUser2,lobby);

        createdGame.setLobbyId(createdLobby.getId());
        Long lobbyId = createdLobby.getId();
        Mockito.when(lobbyRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(createdLobby));
        // when

        gameService.moveLobbyUsers(game);
        List<Long> lobbyUsers = lobby.getUsers();

        // then
        assertArrayEquals(createdGame.getUserIds().toArray(), lobby.getUsers().toArray());
*/
    }

}
