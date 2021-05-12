package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    Optional<GameEntity> findById(Long id);
    Optional<GameEntity> findByCreatorUserId(Long id);
    Optional<GameEntity> findByLobbyId(Long id);

    List<GameEntity> findAll();
    List<GameEntity> findByRoundEquals(int round);
}