package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LobbyServiceTest {

        @Mock
        private LobbyRepository lobbyRepository;

        @Mock
        private UserRepository userRepository;

        @InjectMocks
        private LobbyService lobbyService;
        @Mock
        private UserService userService;


        private Lobby testlobby;
        private User testUser;

        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);

            // given
            testUser = new User();
            testUser.setPassword("123");
            testUser.setUsername("testUsername");

            testlobby = new Lobby();
            testlobby.setCreator(1L);
            testlobby.setPublic(true);


            // when -> any object is being save in the userRepository -> return the dummy testUser
            Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
            Mockito.when(lobbyRepository.save(Mockito.any())).thenReturn(testlobby);

        }

        @Test
        public void createLobby_validInputs_success() {


        userService.createUser(testUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        Lobby createdLobby = lobbyService.createLobby(testlobby);

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        //assertEquals(testlobby.getUsers(), createdLobby.getUsers());
        assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        assertNotNull(createdLobby.getId());
        assertEquals(testlobby.getPublic(), createdLobby.getPublic());


        }
}
