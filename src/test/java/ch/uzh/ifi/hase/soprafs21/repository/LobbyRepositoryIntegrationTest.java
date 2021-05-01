package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class LobbyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LobbyRepository lobbyRepository;

    @Test
    public void findLobbyByRoomKey_success(){
        //given
        Lobby lobby = new Lobby();
        lobby.setGameId(1L);
        lobby.setCreator(1L);
        //lobby.setId(1L);
        lobby.setPublicStatus(true);
        lobby.setRoomKey(2999L);
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Optional<Lobby> found = lobbyRepository.findByRoomKey(lobby.getRoomKey());

        //then
        assertNotNull(found);
        Lobby foundLobby = found.get();
        assertEquals(foundLobby.getCreator(), lobby.getCreator());
        assertEquals(foundLobby.getGameId(), lobby.getGameId());
        assertEquals(foundLobby.getId(), lobby.getId());
        assertEquals(foundLobby.getRoomKey(), lobby.getRoomKey());
        //assertEquals(foundLobby.getPublicStatus(), lobby.getPublicStatus());
        //assertEquals(foundLobby.getUsers(), lobby.getUsers());

    }
    @Test
    public void findLobbyByRoomKey_failure(){
        //given
        Lobby lobby = new Lobby();
        lobby.setGameId(1L);
        lobby.setCreator(1L);
        //lobby.setId(1L);
        lobby.setPublicStatus(true);
        lobby.setRoomKey(2999L);
        entityManager.persist(lobby);
        entityManager.flush();

        // when
        Optional<Lobby> found = lobbyRepository.findByRoomKey(lobby.getRoomKey() + 1);

        //then
        assertEquals(found.isEmpty(), true);

    }

    @Test
    public void findOnlyPublicLobbies_success(){
        //given
        Lobby lobby = new Lobby();
        lobby.setGameId(1L);
        lobby.setCreator(1L);
        //lobby.setId(1L);
        lobby.setPublicStatus(false);
        lobby.setRoomKey(2999L);
        entityManager.persist(lobby);


       /* Lobby lobby2 = new Lobby();
        lobby2.setGameId(1L);
        lobby2.setCreator(1L);
        //lobby.setId(1L);
        lobby2.setPublicStatus(true);
        lobby2.setRoomKey(2999L);
        entityManager.persist(lobby2);
        entityManager.flush();*/

        // when
        List<Lobby> found = lobbyRepository.findAllByPublicStatusTrue();

        //then
        found.forEach(l->{
            assertEquals(l.getPublicStatus(),true);
        });

    }


}
