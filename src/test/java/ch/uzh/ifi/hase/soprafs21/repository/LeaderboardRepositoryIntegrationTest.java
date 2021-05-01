package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.gameModeEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class LeaderboardRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Test
    public void findAll_success() {
        // given
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setId(1);
        leaderboard.setGameMode(gameModeEnum.CLOUDS);
        leaderboard.setScore(1000);
        leaderboard.setUsername("user1");
        entityManager.persistAndFlush(leaderboard);

        Leaderboard leaderboard2 = new Leaderboard();
        leaderboard2.setId(2);
        leaderboard2.setGameMode(gameModeEnum.CLOUDS);
        leaderboard2.setScore(1001);
        leaderboard2.setUsername("user2");
        entityManager.persistAndFlush(leaderboard2);

        Leaderboard leaderboard3 = new Leaderboard();
        leaderboard3.setId(3);
        leaderboard3.setGameMode(gameModeEnum.CLOUDS);
        leaderboard3.setScore(1001);
        leaderboard3.setUsername("user3");
        entityManager.persistAndFlush(leaderboard3);

        Leaderboard leaderboard4 = new Leaderboard();
        leaderboard4.setId(4);
        leaderboard4.setGameMode(gameModeEnum.CLOUDS);
        leaderboard4.setScore(1002);
        leaderboard4.setUsername("user4");
        entityManager.persistAndFlush(leaderboard4);

        Leaderboard leaderboard5 = new Leaderboard();
        leaderboard5.setId(5);
        leaderboard5.setGameMode(gameModeEnum.CLOUDS);
        leaderboard5.setScore(1003);
        leaderboard5.setUsername("user5");
        entityManager.persistAndFlush(leaderboard5);

        Leaderboard leaderboard6 = new Leaderboard();
        leaderboard6.setId(6);
        leaderboard6.setGameMode(gameModeEnum.TIME);
        leaderboard6.setScore(1004);
        leaderboard6.setUsername("user6");
        entityManager.persistAndFlush(leaderboard6);
        // when
        List<Leaderboard> found = leaderboardRepository.findTop5ByGameMode(gameModeEnum.CLOUDS);
        // then
        assertEquals(found.size(),5);
        found.forEach(l->{
            assertEquals(true, l.getGameMode() == gameModeEnum.CLOUDS);
        });
    }
}
