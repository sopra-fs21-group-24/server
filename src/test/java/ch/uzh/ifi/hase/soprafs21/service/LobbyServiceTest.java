package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

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
        private User testUser2;


        @BeforeEach
        public void setup() {
            MockitoAnnotations.openMocks(this);

            // given
            testUser = new User();
            testUser.setPassword("123");
            testUser.setUsername("testUsername");
            testUser2 = new User();
            testUser2.setPassword("123");
            testUser2.setUsername("testUsername");

            testlobby = new Lobby();
            testlobby.setCreator(1L);
            testlobby.setPublicStatus(true);
            testlobby.setGameId(3L);


            // when -> any object is being save in the userRepository -> return the dummy testUser
            when(userRepository.save(Mockito.any())).thenReturn(testUser);
            when(lobbyRepository.saveAndFlush(Mockito.any())).thenReturn(testlobby);
            when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        }

        @Test
        public void createLobby_validInputs_success() {


        userService.createUser(testUser);


        Lobby createdLobby = lobbyService.createLobby(testlobby);

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

        assertEquals(testlobby.getUsers(), createdLobby.getUsers());
        assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        assertEquals(true, testUser.getInLobby());
        assertEquals(true, testUser.getInLobby());
        assertEquals(testlobby.getPublicStatus(), createdLobby.getPublicStatus());


        }

    @Test
    public void adduserToExistingLobby() {


        userService.createUser(testUser);
        userService.createUser(testUser2);


        Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.addUserToExistingLobby(testUser2,createdLobby);

        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Mockito.verify(lobbyService, Mockito.times(1)).addUserToExistingLobby(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

        assertEquals(testlobby.getUsers(), createdLobby.getUsers());
        assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        assertEquals(true, testUser.getInLobby());
        assertEquals(true, testUser2.getInLobby());
        assertEquals(testlobby.getPublicStatus(), createdLobby.getPublicStatus());


    }
}
