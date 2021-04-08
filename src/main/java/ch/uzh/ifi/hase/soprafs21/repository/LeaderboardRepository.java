package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("leaderboardRepository")
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    Leaderboard findBygameModeId(int name);
    //User findByUsername(String username);
}
