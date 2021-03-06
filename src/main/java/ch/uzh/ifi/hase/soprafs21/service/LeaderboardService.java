package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;
import ch.uzh.ifi.hase.soprafs21.repository.LeaderboardRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LeaderboardGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class LeaderboardService implements Observer {

    private final LeaderboardRepository leaderboardRepository;
    private final UserService userService;

    @Autowired
    public LeaderboardService(@Qualifier("leaderboardRepository") LeaderboardRepository leaderboardRepository, UserService userService) {
        this.leaderboardRepository = leaderboardRepository;
        this.userService = userService;
    }

    @Override
    public void updateLeaderboard(String gameMode, ListIterator<Score> scores) {
        while (scores.hasNext()) {
            Score score = scores.next();
            Leaderboard update = new Leaderboard();
            update.setUsername(userService.getUserByUserId(score.getUserId()).getUsername());
            update.setScore(score.getTotalScore());
            update.setGameMode(gameMode);
            leaderboardRepository.save(update);
            }

        leaderboardRepository.flush();
    }

    public ArrayList<LeaderboardGetDTO> getScoresForGameMode(String gameMode){
        ArrayList<LeaderboardGetDTO> finalList = new ArrayList<>();
        for (Leaderboard l :leaderboardRepository.findTop5ByGameModeOrderByScoreDesc(gameMode)){
            finalList.add(DTOMapper.INSTANCE.convertEntityToLeaderboardGetDTO(l));
        }
        return finalList;

    }
}
