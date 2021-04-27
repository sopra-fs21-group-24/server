package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Optional<Lobby> findByid(Long lobbyid);
    Optional<Lobby> findByRoomKey(Long roomKey);
    @Query("select r from Lobby r where r.isPublic = true")
    List<Lobby> findAllByIsPublicTrue();
    Lobby findByCreator(Long creator);
}
