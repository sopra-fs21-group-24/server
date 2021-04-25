package ch.uzh.ifi.hase.soprafs21.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.gameModeEnum;
import ch.uzh.ifi.hase.soprafs21.repository.LeaderboardRepository;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LeaderboardService {

    private final Logger log = LoggerFactory.getLogger(LeaderboardService.class);

    private final LeaderboardRepository leaderboardRepository;

    @Autowired
    public LeaderboardService(@Qualifier("leaderboardRepository") LeaderboardRepository leaderboardRepository) {
        this.leaderboardRepository = leaderboardRepository;
    }
    public Leaderboard getScoresForGameMode(gameModeEnum gameMode){
        return leaderboardRepository.findBygameMode(gameMode);

    }

}
