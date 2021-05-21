package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreService scoreService;

    private Score testScore;
    private Score testScore2;
    private GameEntity gameEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testScore = new Score();
        testScore.setTempScore(1L);
        testScore.setTotalScore(5L);
        testScore.setUserId(3L);
        testScore.setLastCoordinate(new Coordinate(4.5,3.2));

        testScore2 = new Score();
        testScore2.setTempScore(2L);
        testScore2.setTotalScore(6L);
        testScore2.setUserId(4L);
        testScore2.setLastCoordinate(new Coordinate(4.5,3.2));

        gameEntity = new GameEntity();
        gameEntity.setGameMode(new Clouds());
        gameEntity.setGameId(6L);


        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(scoreRepository.save(Mockito.any())).thenReturn(testScore);
    }

    @Test
    public void findScoreById_success() {

        scoreService.save(testScore);

        Mockito.when(scoreRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testScore));
        Score foundScore = scoreService.findById(testScore.getUserId());


        assertEquals(testScore.getTempScore(), foundScore.getTempScore());
        assertEquals(testScore.getTotalScore(), foundScore.getTotalScore());
        assertEquals(testScore.getUserId(), foundScore.getUserId());
        assertEquals(testScore.getLastCoordinate(), foundScore.getLastCoordinate());
    }

    @Test
    public void findScoreById_failure() {

        scoreService.save(testScore);

        // then
        assertThrows(NotFoundException.class, () -> scoreService.findById(9L));
    }

    @Test
    public void scoresByGameTest() {
        Mockito.when(scoreRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testScore));
        for (ListIterator<Score> it = scoreService.scoresByGame(gameEntity); it.hasNext(); ) {
            Score s = it.next();
            assertEquals(s, testScore);
        }
    }
    @Test
    public void getScoreGetDTOsTest() {
        Mockito.when(scoreRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testScore));
        for (ListIterator<Score> it = scoreService.scoresByGame(gameEntity); it.hasNext(); ) {
            Score s = it.next();
            assertEquals(s, testScore);
        }
    }

}
