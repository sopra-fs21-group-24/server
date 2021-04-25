package ch.uzh.ifi.hase.soprafs21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.gameModeEnum;

@Repository("leaderboardRepository")
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Leaderboard findBygameMode(gameModeEnum gameMode);
}
