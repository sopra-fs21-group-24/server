package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Leaderboard  Controller
 * This class is responsible for handling all REST request that are related to the leaderboard.
 * The controller will receive the request and delegate the execution to the LeaderboardService and finally return the result.
 */
@RestController
public class LeaderboardController {

    private final Leaderboard leaderboard;

    LeaderboardController(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    // Getting a leaderboard by Gamemode
    @GetMapping("/leaderboard/{GameMode}")
    @ResponseStatus(HttpStatus.OK)
    public Score[] getLeaderboardByGameMode(@PathVariable("GameMode") int gameModeId){
        return leaderboard.getScoresForGameMode(gameModeId);

    }

}
