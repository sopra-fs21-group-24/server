package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class LobbyServiceTest {

        @Mock
        @Qualifier("lobbyRepository")
        private LobbyRepository lobbyRepository;

        @Mock
        @Qualifier("userRepository")
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
            testUser.setInLobby(false);
            testUser2 = new User();
            testUser2.setPassword("123");
            testUser2.setUsername("testUsername");
            testUser2.setInLobby(false);

            testlobby = new Lobby();
            testlobby.setCreator(1L);
            testlobby.addUser(testUser.getId());
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
        Mockito.verify(lobbyRepository, Mockito.times(2)).saveAndFlush(Mockito.any());
        //Mockito.verify(lobbyService, Mockito.times(1)).addUserToExistingLobby(testUser2,createdLobby);
        Mockito.verify(userRepository, Mockito.times(2)).saveAndFlush(Mockito.any());

        assertEquals(testlobby.getUsers(), createdLobby.getUsers());
        assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        assertEquals(true, testUser.getInLobby());
        assertEquals(true, testUser2.getInLobby());
        assertEquals(testlobby.getPublicStatus(), createdLobby.getPublicStatus());


    }

    @Test
    public void getLobbyById() {


        userService.createUser(testUser);

        Lobby createdLobby = lobbyService.createLobby(testlobby);
        // then
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Mockito.verify(lobbyRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
        Mockito.when(lobbyService.getLobbyById(Mockito.any())).thenReturn(createdLobby);


    }

    @Test
    public void generateRoomKeytest() {


        userService.createUser(testUser);

        Lobby createdLobby = lobbyService.createLobby(testlobby);

        //rookey == 3000-generated id
        assertEquals(2999, createdLobby.getRoomKey());



    }

    @Test
    public void userExitLobbyTest() {

        userService.createUser(testUser);
        userService.createUser(testUser2);


        Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.addUserToExistingLobby(testUser2,createdLobby);
        lobbyService.userExitLobby(testUser2.getId(),createdLobby.getId());  // breaks

        // then
        Mockito.verify(lobbyRepository, Mockito.times(2)).saveAndFlush(Mockito.any());
        //Mockito.verify(lobbyService, Mockito.times(1)).addUserToExistingLobby(testUser2,createdLobby);
        Mockito.verify(userRepository, Mockito.times(2)).saveAndFlush(Mockito.any());




    }



}

