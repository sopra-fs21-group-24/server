package ch.uzh.ifi.hase.soprafs21;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

public class MultiPlayerTest {


    private Answer answerTest;
    private GameEntity game;
    private User testUser;

    @InjectMocks
    private MultiPlayer multiPlayer;

    @BeforeEach
    public void setup() {

        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("MaPassword");
        testUser.setToken("token");
        testUser.setInLobby(true);

        game = new GameEntity();
        game.setGameId(1L);
        game.setCreatorUserId(2L);
        game.setRound(1);
        game.setRoundDuration(20);
        game.setCurrentTime();
        game.setRoundStart(12L);
        game.setGameMode(new Pixelation());

        multiPlayer = new MultiPlayer();
        answerTest = new Answer();
        answerTest.setGameId(1L);
        answerTest.setDifficultyFactor(2);
        answerTest.setQuestionId(12L);
        answerTest.setCoordGuess(new Coordinate(12.12, 12.12));
        answerTest.setCoordQuestion(new Coordinate(14.14, 14.14));
        answerTest.setTimeFactor((float) 0.512);


    }


    @Test
    public void nextRoundPrep_success() {
        //check if next round is prepped correctly
        int preNewRound = game.getRound();
        multiPlayer.nextRoundPrep(game,12L);
        Assertions.assertEquals(game.getRoundStart(), 12 + game.getBreakDuration()* 1000L);
        Assertions.assertEquals(preNewRound+1,game.getRound());


    }

    @Test
    public void adjustThreshold_success() {
        //check if threshold is set corrertly
        game.setThreshold(3);
        int gameThreshold = game.getThreshold();
        List<Long> userAnsweredList = new ArrayList<>();
        userAnsweredList.add(1L);
        userAnsweredList.add(4L);
        game.setUsersAnswered(userAnsweredList);
        multiPlayer.adjustThreshold(game,testUser);

        Assertions.assertEquals(gameThreshold-1,game.getThreshold());


    }

}