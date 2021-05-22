package ch.uzh.ifi.hase.soprafs21.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
@WebMvcTest(LobbyController.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @MockBean
    private LobbyService lobbyService;

    @MockBean
    private GameService gameService;


    @Test
    public void getLobbyWithIdSuccessful() throws Exception {

        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setGameId(1L);
        lobby.setCreator(1L);
        lobby.setPublicStatus(true);
        lobby.setRoomKey(123L);

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());

        LobbyGetDTO lobbyGetDTO = new LobbyGetDTO();
        lobbyGetDTO.setId(1L);
        lobbyGetDTO.setGameId(1L);
        lobbyGetDTO.setCreator(1L);
        lobbyGetDTO.setPublicStatus(true);
        lobbyGetDTO.setRoomKey(123L);
        lobbyGetDTO.setGamemode(game.getGameMode());
        lobbyGetDTO.setUsers(Collections.emptyList());
   
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.getLobbyGetDTO(Mockito.any(), Mockito.any())).willReturn(lobbyGetDTO);
        given(gameService.gameById(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder getRequest = get("/lobby/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("initial", "true");

        MvcResult asyncListener = mockMvc
                .perform(getRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(asyncListener))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.gameId", is(1)))
                .andExpect(jsonPath("$.creator", is(1)))
                .andExpect(jsonPath("$.roomKey", is(123)))
                .andExpect(jsonPath("$.publicStatus", is(true)))
                .andExpect(jsonPath("$.users", is(Collections.emptyList())))
                .andExpect(jsonPath("$.gamemode.name", is("Pixelation")));
    }

    @Test
    public void getLobbyWithIdFailed() throws Exception {

        given(lobbyService.getLobbyById(Mockito.any())).willThrow(new NotFoundException("Lobby with this lobbyid: 1 not found"));

        MockHttpServletRequestBuilder getRequest = get("/lobby/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("initial", "true");

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Lobby with this lobbyid: 1 not found", result.getResolvedException().getMessage()));

    }

    @Test
    public void getAllLobbiesSuccessful() throws Exception {

//        Lobby lobby = new Lobby();
//        lobby.setId(1L);
//        lobby.setGameId(1L);
//        lobby.setCreator(1L);
//        lobby.setPublicStatus(true);
//        lobby.setRoomKey(123L);
//
//        LobbyGetDTOAllLobbies lobbyGetDTOAllLobbies = DTOMapper.INSTANCE.convertEntityToLobbyGetDTOAllLobbies(lobby);
//        List<LobbyGetDTOAllLobbies> lobbies = new ArrayList<LobbyGetDTOAllLobbies>();
//        lobbies.add(lobbyGetDTOAllLobbies);
//
//        doReturn(lobbies).when(lobbyService).getAllLobbies();
//
//        MockHttpServletRequestBuilder getRequest = get("/lobby")
//                .contentType(MediaType.APPLICATION_JSON);
//
//        mockMvc.perform(getRequest)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.[0].id", is(1)))
//                .andExpect(jsonPath("$.[0].username", is(1)))
//                .andExpect(jsonPath("$.[0].users", is(123)))
//                .andExpect(jsonPath("$.[0].publicStatus", is(true)));

    }

    @Test
    public void getAllLobbiesFailed() throws Exception {

    }

    //
    @Test
    public void joinLobbySuccessful() throws Exception {
/* 
        MockHttpServletRequestBuilder postRequest = post("/lobby/123/roomkey")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");


        mockMvc.perform(postRequest)
                .andExpect(status().isOk()); */

    }

    @Test
    public void joinLobbyFailed() throws Exception {

        doThrow(new NotFoundException("User is already in Lobby")).when(lobbyService).addUserToExistingLobby(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/lobby/123/roomkey")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("User is already in Lobby", result.getResolvedException().getMessage()));

    }

    @Test
    public void joinLobbyWithRoomIdSuccessful() throws Exception {

        MockHttpServletRequestBuilder postRequest = post("/lobby/123")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());

    }

    @Test
    public void joinLobbyWithRoomIdFailed() throws Exception {

        doThrow(new NotFoundException("User is already in Lobby")).when(lobbyService).addUserToExistingLobby(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder postRequest = post("/lobby/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("User is already in Lobby", result.getResolvedException().getMessage()));

    }

    @Test
    public void getLobbyWithRoomKeySuccessful() throws Exception {

        Lobby lobby = new Lobby();
        lobby.setId(1L);
        lobby.setGameId(1L);
        lobby.setCreator(1L);
        lobby.setPublicStatus(true);
        lobby.setRoomKey(123L);

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());

        LobbyGetDTO lobbyGetDTO = new LobbyGetDTO();
        lobbyGetDTO.setId(1L);
        lobbyGetDTO.setGameId(1L);
        lobbyGetDTO.setCreator(1L);
        lobbyGetDTO.setPublicStatus(true);
        lobbyGetDTO.setRoomKey(123L);
        lobbyGetDTO.setGamemode(game.getGameMode());
        lobbyGetDTO.setUsers(Collections.emptyList());

        given(lobbyService.getLobbyByRoomkey(Mockito.any())).willReturn(lobby);
        given(lobbyService.getLobbyById(Mockito.any())).willReturn(lobby);
        given(lobbyService.getLobbyGetDTO(Mockito.any(), Mockito.any())).willReturn(lobbyGetDTO);
        given(gameService.gameById(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder getRequest = get("/lobby/roomKey/123")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.gameId", is(1)))
                .andExpect(jsonPath("$.creator", is(1)))
                .andExpect(jsonPath("$.roomKey", is(123)))
                .andExpect(jsonPath("$.publicStatus", is(true)))
                .andExpect(jsonPath("$.gamemode.name", is("Pixelation")));

    }

    @Test
    public void getLobbyWithRoomKeyFailed() throws Exception {

        given(lobbyService.getLobbyById(Mockito.any())).willThrow(new NotFoundException("Lobby with this roomkey: 123 not found"));

        MockHttpServletRequestBuilder getRequest = get("/lobby/123")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Lobby with this roomkey: 123 not found", result.getResolvedException().getMessage()));
    }

    @Test
    public void userExitLobbySuccessful() throws Exception {

        MockHttpServletRequestBuilder postRequest = post("/lobby/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isOk());

    }

    @Test
    public void userExitLobbyFailed() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("Ben");
        user.setPassword("1234");
        user.setToken("1");

        given(userService.getUserByUserId(Mockito.any())).willReturn(user);
        given(lobbyService.checkAuth(Mockito.any())).willReturn(user);
        doThrow(new PreconditionFailedException("User is not in a lobby!")).when(lobbyService).userExitLobby(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/lobby/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isPreconditionFailed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PreconditionFailedException))
                .andExpect(result -> assertEquals("User is not in a lobby!", result.getResolvedException().getMessage()));

    }


    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }
}

