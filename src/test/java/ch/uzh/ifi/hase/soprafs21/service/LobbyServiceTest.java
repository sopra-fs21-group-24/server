package ch.uzh.ifi.hase.soprafs21.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTOAllLobbies;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
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
import org.springframework.beans.factory.annotation.Qualifier;


public class LobbyServiceTest {

    @Mock
    @Qualifier("lobbyRepository")
    private LobbyRepository lobbyRepository;

    @Mock
    @Qualifier("userRepository")
    private UserRepository userRepository;


    @Mock
    @Qualifier("gameRepository")
    private GameRepository gameRepository;

    @InjectMocks
    private LobbyService lobbyService;

    @Mock
    private UserService userService;

    @Mock
    private GameEntity gameEntity;


    private Lobby testlobby;
    private User testUser;
    private User testUser2;
    private GameEntity game;
    private GameEntity game2;
    private GameMode clouds;
    private Lobby testlobby2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        testUser = new User();
        testUser.setPassword("123");
        testUser.setUsername("testUsername");
        testUser.setId(1L);
        testUser.setInLobby(false);
        testUser2 = new User();
        testUser2.setPassword("123");
        testUser2.setId(5L);
        testUser2.setUsername("testUsername");
        testUser2.setInLobby(false);
        testlobby = new Lobby();
        testlobby.setCreator(1L);
        testlobby.setId(4L);
        testlobby.setPublicStatus(true);
        testlobby.setGameId(3L);
        game = new GameEntity();
        clouds = new Clouds();
        game.setGameMode(clouds);
        game2 = new GameEntity();
        game2.setGameMode(clouds);
        testlobby2 = new Lobby();
        testlobby2.setCreator(testUser2.getId());
        testlobby2.setId(7L);
        testlobby2.setPublicStatus(true);
        testlobby2.setGameId(8L);


        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser, testUser2);
        Mockito.when(lobbyRepository.saveAndFlush(Mockito.any())).thenReturn(testlobby);
        Mockito.when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testlobby));
        Mockito.when(lobbyRepository.findByRoomKey(Mockito.any())).thenReturn(Optional.ofNullable(testlobby));
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(userRepository.findById(testUser2.getId())).thenReturn(Optional.ofNullable(testUser2));
        Mockito.when(gameRepository.findByLobbyId(testlobby.getId())).thenReturn(Optional.of(game));
        Mockito.when(gameRepository.findByLobbyId(testlobby2.getId())).thenReturn(Optional.of(game2));
    }



    @Test
    public void createLobby_validInputs_success() {


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


        GameEntity game = new GameEntity();
        Mockito.when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(Optional.of(game));

        Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.addUserToExistingLobby(testUser2, createdLobby);

        // then verify the repositorys
        Mockito.verify(lobbyRepository, Mockito.times(2)).saveAndFlush(Mockito.any());
        Mockito.verify(userRepository, Mockito.times(2)).saveAndFlush(Mockito.any());

        //Check with assertions
        assertEquals(testlobby.getUsers(), createdLobby.getUsers());
        assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        //Are both users set to inLobby == true?
        assertEquals(true, testUser.getInLobby());
        assertEquals(true, testUser2.getInLobby());
        assertEquals(testlobby.getPublicStatus(), createdLobby.getPublicStatus());

    }

    @Test
    public void adduserToExistingLobbyFail() {


        Lobby testlobby2 = new Lobby();
        testlobby2.setCreator(testUser2.getId());
        testlobby2.setId(7L);
        testlobby2.setPublicStatus(true);
        testlobby2.setGameId(8L);
        testUser2.setInLobby(false);
        userService.createUser(testUser);
        userService.createUser(testUser2);


        Lobby createdLobby = lobbyService.createLobby(testlobby);
        Mockito.when(lobbyService.createLobby(testlobby2)).thenReturn(testlobby2);
        assertThrows(NotFoundException.class, () -> {lobbyService.addUserToExistingLobby(testUser2, createdLobby);});




    }


    @Test
    public void generateRoomKeytest() {


        userService.createUser(testUser);

        Lobby createdLobby = lobbyService.createLobby(testlobby);

        //roomkey == 3000-generated id
        assertEquals(2999, createdLobby.getRoomKey());


    }


    @Test
    public void deleteLobbyTest() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        userService.createUser(testUser);
        testlobby.addUser(testUser.getId());

        // Mockito.when(lobbyRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testlobby));

        Mockito.when(userService.getUserByUserId(Mockito.anyLong())).thenReturn(testUser);
        // Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.deleteLobby(testlobby.getId());
        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();
        // Mockito.verify(userRepository, Mockito.times(1)).saveAndFlush(Mockito.any());

        // assertEquals(testlobby.getCreator(), createdLobby.getCreator());
        assertEquals(false, testUser.getInLobby());

    }


    @Test
    public void userExitLobbyTest() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        userService.createUser(testUser);
        testlobby.addUser(testUser.getId());

        //change setup so user is in lobby
        testUser.setInLobby(true);

        Mockito.when(userService.getUserByUserId(Mockito.anyLong())).thenReturn(testUser);

        lobbyService.addUserToExistingLobby(testUser2, testlobby);
        lobbyService.userExitLobby(testUser,testlobby.getId());
        // then
        Mockito.verify(lobbyRepository, Mockito.times(1)).flush();


        assertFalse( testUser.getInLobby());
        assertTrue(testUser2.getInLobby());

    }

    @Test
    public void userExitLobbyTestFailUserNotInLobby() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        userService.createUser(testUser);
        testlobby.addUser(testUser.getId());

        Mockito.when(userService.getUserByUserId(Mockito.anyLong())).thenReturn(testUser);
        // Lobby createdLobby = lobbyService.createLobby(testlobby);
        lobbyService.addUserToExistingLobby(testUser2, testlobby);
        assertThrows(PreconditionFailedException.class, () -> {lobbyService.userExitLobby(testUser,testlobby.getId());});


    }


    @Test
    public void userExitLobbyTestFailUserNotInThisLobbyWillThrowException() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
        userService.createUser(testUser);
        testlobby.addUser(testUser.getId());

        //set user in lobby to true, "Mock that he is a lobby"
        testUser.setInLobby(true);

        Mockito.when(userService.getUserByUserId(Mockito.anyLong())).thenReturn(testUser);


        assertThrows(PreconditionFailedException.class, () -> {lobbyService.userExitLobby(testUser2,testlobby.getId());});


    }
    @Test
    public void gameByLobbyIdTest() {

        assertEquals(lobbyService.gameByLobbyId(testlobby.getId()), game);


    }

    @Test
    public void gameByLobbyIdTestWillThrowException() {
        Mockito.when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {lobbyService.gameByLobbyId(testlobby.getId());});

    }

    @Test
    public void getLobbyGetDTOAllLobbiesTest() {

        Lobby createdLobby = lobbyService.createLobby(testlobby);
        Lobby createdLobby2 = lobbyService.createLobby(testlobby2);
        List<Lobby> listForRepo = new ArrayList<>();
        listForRepo.add(createdLobby);
        listForRepo.add(createdLobby2);

        Mockito.when(lobbyRepository.findAllByPublicStatusTrue()).thenReturn(listForRepo);
        Mockito.when(userService.getUserByUserId(Mockito.any())).thenReturn(testUser, testUser2);
        Mockito.when(gameEntity.getGameMode()).thenReturn(clouds);


        List<LobbyGetDTOAllLobbies> lobbyList = new ArrayList<>();
        lobbyList.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTOAllLobbies(createdLobby));
        lobbyList.add(DTOMapper.INSTANCE.convertEntityToLobbyGetDTOAllLobbies(createdLobby2));
        int ind = 0;
        for (LobbyGetDTOAllLobbies lobby : lobbyService.getLobbyGetDTOAllLobbies()){
            assertEquals(lobby.getId(), lobbyList.get(ind).getId());
            assertEquals(lobby.getPublicStatus(), lobbyList.get(ind).getPublicStatus());
            ind+=1;
        }
    }
    @Test
    public void getLobbyGetDTOTest() {

        assertEquals(testlobby.getCreator(), lobbyService.getLobbyGetDTO(testlobby,clouds).getCreator());
        assertEquals(testlobby.getGameId(), lobbyService.getLobbyGetDTO(testlobby,clouds).getGameId());
        assertEquals(testlobby.getPublicStatus(), lobbyService.getLobbyGetDTO(testlobby,clouds).getPublicStatus());
        assertEquals(testlobby.getUsers(), lobbyService.getLobbyGetDTO(testlobby,clouds).getUsers());

    }

    @Test
    public void getLobbyByRoomkeyTest() {
        assertEquals(testlobby, lobbyService.getLobbyByRoomkey(testlobby.getRoomKey()));

    }

    @Test
    public void getLobbyByRoomkeyTestWillThrowException() {
        Mockito.when(lobbyRepository.findByRoomKey(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {lobbyService.getLobbyByRoomkey(testlobby.getId());});
    }

    @Test
    public void getLobbyByIdTest() {

        assertEquals(testlobby, lobbyService.getLobbyById(testlobby.getId()));

    }


}





