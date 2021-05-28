package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;

@Repository("leaderboardRepository")
public interface LeaderboardRepository extends JpaRepository<Leaderboard, String> {
    @Query("select l from Leaderboard l where l.gameMode = ?1 and l.score in(select max(n.score) from Leaderboard n where l.username = n.username and n.gameMode = ?1) order by l.score desc")
    List<Leaderboard> findTop5ByGameModeOrderByScoreDesc(String gameMode);
    List<Leaderboard> findByUsername(String username);
}
