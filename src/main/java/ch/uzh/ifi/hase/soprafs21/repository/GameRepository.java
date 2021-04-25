package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    // optional to avoid null
    Optional<GameEntity> findById(Long id);
}
