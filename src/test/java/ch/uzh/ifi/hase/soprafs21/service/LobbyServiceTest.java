package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.util.Optional;

import ch.uzh.ifi.hase.soprafs21.exceptions.MissingInformationException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
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
import static org.mockito.BDDMockito.given;
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
        testlobby.setId(4L);
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
    public void createLobby_invalidInputs_failNotFoundException() {


        userService.createUser(testUser);

        //no user suer found for lobby
        testlobby.setCreator(null);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        // then

        assertThrows(NotFoundException.class, () -> lobbyService.createLobby(testlobby));

    }

    @Test
    public void createLobby_invalidInputs_failPreconditionFailedException() {


        userService.createUser(testUser);

        //no user suer found for lobby
        testUser.setInLobby(true);
        testlobby.setCreator(null);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));
        // then

        assertThrows(PreconditionFailedException.class, () -> lobbyService.createLobby(testlobby));

    }


    @Test
    public void adduserToExistingLobby() {


        userService.createUser(testUser);
        userService.createUser(testUser2);


        Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.addUserToExistingLobby(testUser2, createdLobby);

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
    public void generateRoomKeytest() {


        userService.createUser(testUser);

        Lobby createdLobby = lobbyService.createLobby(testlobby);

        //roomkey == 3000-generated id
        assertEquals(2999, createdLobby.getRoomKey());


    }
}

   /* @Test
    public void deleteLobbyTest() {

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        userService.createUser(testUser);

        when(lobbyRepository.findByid(Mockito.any())).thenReturn(Optional.ofNullable(testlobby));

        Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.deleteLobby(createdLobby.getId());
        // then
        Mockito.verify(lobbyRepository, Mockito.times(2)).saveAndFlush(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any());


        assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        assertEquals(false, testUser.getInLobby());


    }


}*/





