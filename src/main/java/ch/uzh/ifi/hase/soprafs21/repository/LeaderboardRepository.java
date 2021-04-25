package ch.uzh.ifi.hase.soprafs21.repository;

import org.hibernate.sql.Select;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.gameModeEnum;

import java.awt.print.Pageable;
import java.util.List;

@Repository("leaderboardRepository")
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {
    List<Leaderboard> findTop5ByGameMode(gameModeEnum gameMode);
}
