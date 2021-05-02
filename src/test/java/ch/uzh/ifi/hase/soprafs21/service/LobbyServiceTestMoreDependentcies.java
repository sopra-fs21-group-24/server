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
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

public class LobbyServiceTestMoreDependentcies {

        @Mock
        @Qualifier("lobbyRepository")
        private LobbyRepository lobbyRepository;

        @Mock
        @Qualifier("userRepository")
        private UserRepository userRepository;

        @Mock
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
            testUser2.setInLobby(true);

            testlobby = new Lobby();
            testlobby.setCreator(1L);
            testlobby.setId(4L);
            testlobby.addUser(testUser.getId());
            testlobby.addUser(testUser2.getId());
            testlobby.setPublicStatus(true);
            testlobby.setGameId(3L);


            // when -> any object is being save in the userRepository -> return the dummy testUser
            when(userRepository.findById(testUser2.getId())).thenReturn(Optional.ofNullable(testUser2));
            when(lobbyRepository.saveAndFlush(Mockito.any())).thenReturn(testlobby);
           // when(lobbyRepository.findByid(Mockito.any())).thenReturn(Optional.of(testlobby));
            when(userService.getUserByUserId(testUser2.getId())).thenReturn(testUser2);
            when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));


        }

    @Test
    public void userExitLobbyTest() {
        given(lobbyRepository.findByid(Mockito.any())).willReturn(Optional.ofNullable(testlobby));


        userService.createUser(testUser);
        userService.createUser(testUser2);


        //Lobby createdLobby = lobbyService.createLobby(testlobby);
        //lobbyService.addUserToExistingLobby(testUser2,createdLobby);
        //lobbyService.userExitLobby(testUser2.getId(), testlobby.getId());  // breaks

        // then
       // Mockito.verify(lobbyRepository, Mockito.times(2)).saveAndFlush(Mockito.any());

       // Mockito.verify(userRepository, Mockito.times(2)).saveAndFlush(Mockito.any());




    }
    @Test
    public void getLobbyById() {


        userService.createUser(testUser);

        Lobby createdLobby = lobbyService.createLobby(testlobby);
        given(lobbyRepository.findByid(testlobby.getId())).willReturn(Optional.ofNullable(createdLobby));
        Lobby returnedLobby  = lobbyService.getLobbyById(testlobby.getId());

        assertEquals(returnedLobby, createdLobby);

    }


}
