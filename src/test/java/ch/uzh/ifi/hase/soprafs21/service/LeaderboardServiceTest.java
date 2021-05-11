package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.repository.LeaderboardRepository;
import ch.uzh.ifi.hase.soprafs21.repository.ScoreRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LeaderboardGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LeaderboardServiceTest {

    @Mock
    private LeaderboardRepository leaderboardRepository;

    @Mock
    private ScoreService scoreService;

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private LeaderboardService leaderboardService;

    private Leaderboard testleadderboard;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testleadderboard = new Leaderboard();
        testleadderboard.setUsername("TestUser");
        testleadderboard.setGameMode("Clouds");
        testleadderboard.setScore(1234L);
        testleadderboard.setId(1L);

        Mockito.when(leaderboardRepository.saveAndFlush(Mockito.any())).thenReturn(testleadderboard);
    }

    @Test
    public void getLeaderboard_validInputs_success() {

        ArrayList<LeaderboardGetDTO> leaderboardList = leaderboardService.getScoresForGameMode("Clouds");

        Mockito.verify(leaderboardRepository, Mockito.times(1)).findTop5ByGameModeOrderByScoreDesc(Mockito.any());
        for (LeaderboardGetDTO l: leaderboardList) {
            assertEquals(l.getGameMode(), testleadderboard.getGameMode());
            assertEquals(l.getUsername(),testleadderboard.getUsername());
            assertEquals(l.getScore(),testleadderboard.getScore());
        }
    }


    @Test
    public void updateLeaderboard_validInputs_success() {
        Leaderboard testleadderboard2 = new Leaderboard();

        Score score1 = new Score();
        score1.setTotalScore(2000L);
        score1.setUserId(2L);
        ListIterator<Score> scores = new ListIterator<Score>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Score next() {
                return null;
            }

            @Override
            public boolean hasPrevious() {
                return false;
            }

            @Override
            public Score previous() {
                return null;
            }

            @Override
            public int nextIndex() {
                return 0;
            }

            @Override
            public int previousIndex() {
                return 0;
            }

            @Override
            public void remove() {

            }

            @Override
            public void set(Score score) {

            }

            @Override
            public void add(Score score) {

            }
        };
        scores.add(score1);
        leaderboardService.updateLeaderboard("Clouds", scores);

        ArrayList<LeaderboardGetDTO> leaderboardList = leaderboardService.getScoresForGameMode("Clouds");

        Mockito.verify(leaderboardRepository, Mockito.times(1)).findTop5ByGameModeOrderByScoreDesc(Mockito.any());
        for (LeaderboardGetDTO l: leaderboardList) {
            assertEquals(l.getGameMode(), testleadderboard.getGameMode());
            assertEquals(l.getUsername(),testleadderboard.getUsername());
            assertEquals(l.getScore(),testleadderboard.getScore());
        }
    }


}