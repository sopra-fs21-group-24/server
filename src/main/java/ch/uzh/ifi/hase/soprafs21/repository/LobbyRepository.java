package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findByid(Long lobbyid);
    @Query("select r.id from Lobby r where r.isPublic = true")
    List<Lobby> findAllByIsPublicTrue();
    Lobby findByRoomKey(Long roomKey);
    Lobby findByCreator(Long creator);
}
