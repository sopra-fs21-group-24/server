package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.MissingInformationException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PerformingUnauthenticatedAction;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserAlreadyExistsException;
import ch.uzh.ifi.hase.soprafs21.repository.LeaderboardRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Leaderboard getScoresForGameMode(int gameModeId){
        return leaderboardRepository.findBygameModeId(gameModeId);

    }

}
