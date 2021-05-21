package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Optional<Lobby> findByid(Long lobbyid);
    Optional<Lobby> findByRoomKey(Long roomKey);
    List<Lobby> findAllByPublicStatusTrue();
}
