package ch.uzh.ifi.hase.soprafs21;


import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GameModeTest {

    GameMode gameModeTest;
    Answer answerTest;

    @BeforeEach
    public void setup() {
        gameModeTest = new Time();
        answerTest = new Answer();
        answerTest.setGameId(1L);
        answerTest.setDifficultyFactor(2);
        answerTest.setQuestionId(12L);
        answerTest.setCoordGuess(new Coordinate(12.12,12.12));
        answerTest.setCoordQuestion(new Coordinate(14.14,14.14));
        answerTest.setTimeFactor((float) 0.512);


    }
    @Test
    public void calculateScoreTest_success(){
        //random points
        Assertions.assertEquals(gameModeTest.calculateScore(answerTest),456);

    }
    @Test
    public void calculateScoreTestMax_success(){

        //identiccal points and fast answer
        answerTest.setCoordQuestion(new Coordinate(12.12,12.12));
        answerTest.setTimeFactor((float) 1);
        Assertions.assertEquals(gameModeTest.calculateScore(answerTest),500);

    }

    @Test
    public void calculateScoreTestMin_success(){

        //opposite points of world
        answerTest.setCoordQuestion(new Coordinate(56.702027, -123.545375));
        answerTest.setCoordGuess(new Coordinate(-51.485475, 63.724099));
        answerTest.setTimeFactor((float) 0.001);
        Assertions.assertEquals(gameModeTest.calculateScore(answerTest),0);

    }


    @Test
    public void calculateScoreTest_fail_timeFactor_invalid() throws PreconditionFailedException {
        //test for invalid time factor --> throws exeption
        answerTest.setTimeFactor((float) -2);
        assertThrows(PreconditionFailedException.class, () -> {gameModeTest.calculateScore(answerTest);});

    }

    @Test
    public void checkTimeValid_test_invalidRoundStart() throws PreconditionFailedException {
        //test for invalid round start --> throws exeption

        GameEntity gameEntity = new GameEntity();
        gameEntity.setRoundStart(12L);
        gameEntity.setRoundDuration(2);

        assertThrows(PreconditionFailedException.class, () -> {gameModeTest.checkTimeValid(gameEntity,11L);});

    }
    @Test
    public void checkTimeValid_test_invalidRoundDuration() throws PreconditionFailedException {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setRoundStart(10L);
        gameEntity.setRoundDuration(1);

        assertThrows(PreconditionFailedException.class, () -> {gameModeTest.checkTimeValid(gameEntity,110000L);});

    }

    @Test
    public void calculateDistanceFactorTest() {
        //max factor
        assertEquals(1, gameModeTest.calculateDistanceFactor(new Coordinate(12.0,12.0), new Coordinate(12.1,12.1)));
        //min factor
        assertEquals(0, gameModeTest.calculateDistanceFactor(new Coordinate(-51.485475, 63.724099), new Coordinate(56.702027, -123.545375)));

    }

    @Test
    public void calculateTimeFactorTest() {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setRoundStart(12L);
        gameEntity.setRoundDuration(2);


        assertEquals(1, gameModeTest.calculateTimeFactor(gameEntity,12L));

    }
    @Test
    public void calculateTimeFactor2Test() {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setRoundStart(100L);
        gameEntity.setRoundDuration(1);

        //formula for calculation
        assertEquals(1.0f-((1000L-gameEntity.getRoundStart())/((float)gameEntity.getRoundDuration()*1000)), gameModeTest.calculateTimeFactor(gameEntity,1000L));

    }

    @Test
    public void haversineDistanceTest() {
        //test if correct output
        assertEquals(9.65749167620074, gameModeTest.haversineDistance(new Coordinate(12.0,12.0),new Coordinate(12.1,12.1)));

    }


}
