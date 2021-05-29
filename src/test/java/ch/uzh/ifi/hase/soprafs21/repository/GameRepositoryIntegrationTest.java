package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        game.setCreatorUserId(1L);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        Optional<GameEntity> found = gameRepository.findById(game.getGameId());
        // then
        assertTrue(found.isPresent());

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
        game.setCreatorUserId(1L);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        Optional<GameEntity> found = gameRepository.findById(game.getGameId() -1);
        // then
        assertTrue(found.isEmpty());
    }

    @Test
    public void findByCreatorId_success() {
        // given
        GameEntity game = new GameEntity();
        game.setGameMode(new Time());
        //game.setGameId(1L);
        game.setGameMode(new Time());
        game.setUserMode(new MultiPlayer());
        game.setCreatorUserId(1L);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        Optional<GameEntity> found = gameRepository.findByCreatorUserId(game.getCreatorUserId());
        // then
        assertTrue(found.isPresent());

        GameEntity foundGameEntity = found.get();
        assertEquals(foundGameEntity.getGameMode(), game.getGameMode());
        assertEquals(foundGameEntity.getGameId(), game.getGameId());
        assertEquals(foundGameEntity.getLobbyId(), game.getLobbyId());
    }

    @Test
    public void findByCreatorId_failure() {
        // given
        GameEntity game = new GameEntity();

        game.setGameMode(new Time());
        //game.setGameId(1L);
        game.setGameMode(new Time());
        game.setUserMode(new MultiPlayer());
        game.setCreatorUserId(1L);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        Optional<GameEntity> found = gameRepository.findByCreatorUserId(game.getCreatorUserId() -1);
        // then
        assertTrue(found.isEmpty());
    }

    @Test
    public void findByRoundEquals_success() {
        // given
        GameEntity game = new GameEntity();
        game.setGameMode(new Time());
        //game.setGameId(1L);
        game.setGameMode(new Time());
        game.setUserMode(new MultiPlayer());
        game.setCreatorUserId(1L);
        game.setRound(2);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persist(game);
        entityManager.flush();
        // when
        List<GameEntity> found = gameRepository.findByRoundEquals(game.getRound());
        // then
        found.forEach(l-> assertEquals(l.getRound(),game.getRound()));
    }

    @Test
    public void findAll_success() {
        // given
        GameEntity game = new GameEntity();
        game.setGameMode(new Time());
        //game.setGameId(1L);
        game.setGameMode(new Time());
        game.setUserMode(new MultiPlayer());
        game.setCreatorUserId(1L);
        game.setRound(2);
        game.setBreakDuration(5);
        game.setLobbyId(5L);


        entityManager.persistAndFlush(game);


        GameEntity game2 = new GameEntity();
        game2.setGameMode(new Time());
        game2.setUserMode(new MultiPlayer());
        game2.setCreatorUserId(4L);
        game2.setRound(3);
        game2.setBreakDuration(5);
        game2.setLobbyId(5L);

        entityManager.persistAndFlush(game2);
        // when
        List<GameEntity> found = gameRepository.findAll();
        // then
        assertEquals(found.size(),2);
    }
}
