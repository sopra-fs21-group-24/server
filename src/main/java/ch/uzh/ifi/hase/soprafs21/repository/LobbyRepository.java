package ch.uzh.ifi.hase.soprafs21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;

import java.util.List;

@Repository("lobbyRepository")
public interface LobbyRepository extends JpaRepository<Lobby, Long> {
    Lobby findByid(Long lobbyid);
    @Query("select r.id from Lobby r where r.isPublic = true")
    List findAllByIsPublicTrue();

    Lobby findByCreator(Long creator);
}
