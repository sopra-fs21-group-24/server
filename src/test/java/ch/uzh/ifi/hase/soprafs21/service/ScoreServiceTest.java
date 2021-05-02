package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.repository.ScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScoreServiceTest {

    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private ScoreService scoreService;

    private Score testScore;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testScore= new Score();
        testScore.setTempScore(1L);
        testScore.setTotalScore(5L);
        testScore.setUserId(3L);
        testScore.setLastCoordinate(new Coordinate(4.5,3.2));


        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(scoreRepository.save(Mockito.any())).thenReturn(testScore);
    }

    @Test
    public void findScoreById_success() {

        // when -> any object is being save in the userRepository -> return the dummy testUser
        scoreService.save(testScore);

        // then
        Mockito.when(scoreRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testScore));
        Score foundScore = scoreService.findById(testScore.getUserId());


        assertEquals(testScore.getTempScore(), foundScore.getTempScore());
        assertEquals(testScore.getTotalScore(), foundScore.getTotalScore());
        assertEquals(testScore.getUserId(), foundScore.getUserId());
        assertEquals(testScore.getLastCoordinate(), foundScore.getLastCoordinate());
    }

    @Test
    public void findScoreById_failure() {

        // when -> any object is being save in the userRepository -> return the dummy testUser
        scoreService.save(testScore);

        // then
        assertThrows(NotFoundException.class, () -> scoreService.findById(9L));
    }


}
