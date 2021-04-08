package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.entity.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LeaderboardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LeaderboardService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public LeaderboardGetDTO getLeaderboardByGameMode(@PathVariable("GameMode") GameMode gameMode){
        Leaderboard foundLeaderboard = leaderboardService.getScoresForGameMode(gameMode.gameModeId);
        return DTOMapper.INSTANCE.convertEntityToLeaderboardGetDTO(foundLeaderboard);
    }

}
