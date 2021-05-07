package ch.uzh.ifi.hase.soprafs21.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class LeaderboardRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @Test
    public void findAll_success() {
        // given
       /*  Leaderboard leaderboard = new Leaderboard();
        leaderboard.setId(1L);
        leaderboard.setGameMode("Clouds");
        leaderboard.setScore(1000L);
        leaderboard.setUsername("user1");
        entityManager.persistAndFlush(leaderboard);

        Leaderboard leaderboard2 = new Leaderboard();
        leaderboard2.setId(2L);
        leaderboard2.setGameMode("Clouds");
        leaderboard2.setScore(1001L);
        leaderboard2.setUsername("user2");
        entityManager.persistAndFlush(leaderboard2);

        Leaderboard leaderboard3 = new Leaderboard();
        leaderboard3.setId(3L);
        leaderboard3.setGameMode("Clouds");
        leaderboard3.setScore(1001L);
        leaderboard3.setUsername("user3");
        entityManager.persistAndFlush(leaderboard3);

        Leaderboard leaderboard4 = new Leaderboard();
        leaderboard4.setId(4L);
        leaderboard4.setGameMode("Clouds");
        leaderboard4.setScore(1002L);
        leaderboard4.setUsername("user4");
        entityManager.persistAndFlush(leaderboard4);

        Leaderboard leaderboard5 = new Leaderboard();
        leaderboard5.setId(5L);
        leaderboard5.setGameMode("Clouds");
        leaderboard5.setScore(1003L);
        leaderboard5.setUsername("user5");
        entityManager.persistAndFlush(leaderboard5);

        Leaderboard leaderboard6 = new Leaderboard();
        leaderboard6.setId(6L);
        leaderboard6.setGameMode("Time");
        leaderboard6.setScore(1004L);
        leaderboard6.setUsername("user6");
        entityManager.persistAndFlush(leaderboard6);

        // when
        List<Leaderboard> found = leaderboardRepository.findTop5ByGameMode("Clouds");
        // then
        assertEquals(found.size(),5);
        found.forEach(l->{
            assertEquals(true, l.getGameMode() == "Clouds");
        });*/
    } 
}
