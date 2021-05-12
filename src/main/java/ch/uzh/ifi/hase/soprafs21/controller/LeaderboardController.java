package ch.uzh.ifi.hase.soprafs21.controller;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.rest.dto.LeaderboardGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.LeaderboardService;

/**
 * Leaderboard.java  Controller
 * This class is responsible for handling all REST request that are related to the leaderboard.
 * The controller will receive the request and delegate the execution to the LeaderboardService and finally return the result.
 */
@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    // Getting a leaderboard by Gamemode
    @GetMapping("/leaderboard/{GameMode}")
    @ResponseStatus(HttpStatus.OK)
    public ArrayList<LeaderboardGetDTO> getLeaderboardByGameMode(@PathVariable("GameMode") String gameMode){
        return leaderboardService.getScoresForGameMode(gameMode);

    }

}
