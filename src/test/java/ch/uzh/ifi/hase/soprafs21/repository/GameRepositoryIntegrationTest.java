package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class GameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameRepository gameRepository;

  @Test
    public void findByGameId_success() {
        // given
        GameEntity game = new GameEntity();
        game.setGameMode(new Time());
        //game.setGameId(1L);
        game.setGameMode(new Time());
        game.setUserMode(new MultiPlayer());
        game.setCreatorUserId(1l);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        Optional<GameEntity> found = gameRepository.findById(game.getGameId());
        // then
        assertNotNull(found);

        GameEntity foundGameEntity = found.get();
        assertEquals(foundGameEntity.getGameMode(), game.getGameMode());
        assertEquals(foundGameEntity.getGameId(), game.getGameId());
        assertEquals(foundGameEntity.getLobbyId(), game.getLobbyId());
    }

    @Test
    public void findByGameId_failure() {
        // given
        GameEntity game = new GameEntity();

        game.setGameMode(new Time());
        //game.setGameId(1L);
        game.setGameMode(new Time());
        game.setUserMode(new MultiPlayer());
        game.setCreatorUserId(1l);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        Optional<GameEntity> found = gameRepository.findById(game.getGameId() -1);
        // then
        assertEquals(found.isEmpty(), true);
    }
  /*
    @Test
    public void findByToken_success() {
        // given
        User user = new User();

        user.setUsername("firstname@lastname");
        user.setPassword("mapassword");
        user.setToken("1");
        entityManager.persist(user);
        entityManager.flush();
        // when
        Optional<User> found = userRepository.findByToken(user.getToken());
        // then
        assertNotNull(found);
        User foundUser = found.get();
        assertEquals(foundUser.getUsername(), user.getUsername());
        assertEquals(foundUser.getToken(), user.getToken());
    }

    @Test
    public void findByToken_failure() {
        // given
        User user = new User();

        user.setUsername("firstname@lastname");
        user.setPassword("mapassword");
        user.setToken("1");
        entityManager.persist(user);
        entityManager.flush();
        // when
        Optional<User> found = userRepository.findByToken("Non existing Token");
        // then
        assertEquals(found.isEmpty(), true);
    }*/
}
