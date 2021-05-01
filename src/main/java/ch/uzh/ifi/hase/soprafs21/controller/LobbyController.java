package ch.uzh.ifi.hase.soprafs21.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTOAllLobbies;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTOWithoutToken;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class LobbyController {

    private final LobbyService lobbyService;
    private final UserService userService;
    private final GameService gameService;

    LobbyController(LobbyService lobbyService, UserService userService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.gameService = gameService;
    }



    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLeobbyWithId(@PathVariable("id") Long lobbyid) {
        Lobby lobby = lobbyService.getLobbyById(lobbyid);
        GameEntity game = gameService.gameById(lobby.getGameId());

        LobbyGetDTO lobbyDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        List<UserGetDTOWithoutToken> userlist = new ArrayList<>();
        for (Long i : lobby.getUsers()) {
            userlist.add(DTOMapper.INSTANCE.convertEntityToUserGetDTOWithoutToken(userService.getUserByUserId(i)));

        }

        lobbyDTO.setGamemode(game.getGameMode());
        lobbyDTO.setUsers(userlist);
        return lobbyDTO;
    }
    @GetMapping("/lobby")
    @ResponseStatus(HttpStatus.OK)
    public List<LobbyGetDTOAllLobbies> getAllLobbies() {
        List<LobbyGetDTOAllLobbies> finalLobbyList = new ArrayList<>();
        for (Lobby i : lobbyService.getAllLobbies()) {
            LobbyGetDTOAllLobbies lobbyGetDTOAllLobbies = DTOMapper.INSTANCE.convertEntityToLobbyGetDTOAllLobbies(i);
            lobbyGetDTOAllLobbies.setUsers(i.getUsers().size());
            lobbyGetDTOAllLobbies.setUsername(userService.getUserByUserId(i.getCreator()).getUsername());
            finalLobbyList.add(lobbyGetDTOAllLobbies);
        }
        return finalLobbyList;
    }

    //only for testing
    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);
        Lobby createdLobby = lobbyService.createLobby(lobby);
        return getLeobbyWithId(createdLobby.getId());

    }

    @PostMapping("/lobby/{roomKey}/roomkey")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobby(@PathVariable long roomKey, @RequestHeader Map<String, String> header) {
        User user = lobbyService.checkAuth(header);
        Lobby lobbyToJoin = lobbyService.getLobbyByRoomkey(roomKey);
        lobbyService.addUserToExistingLobby(user,lobbyToJoin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobbyWithRoomId(@PathVariable long lobbyId,@RequestHeader Map<String, String> header) {
        User user = lobbyService.checkAuth(header);
        Lobby lobbyToJoin = lobbyService.getLobbyById(lobbyId);
        lobbyService.addUserToExistingLobby(user,lobbyToJoin);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/lobby/roomKey/{roomKey}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobbyWithRoomKey(@PathVariable("roomKey") Long roomKey) {
        Lobby lobby = lobbyService.getLobbyByRoomkey(roomKey);
        return getLeobbyWithId(lobby.getId());
    }

    @PutMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void userExitLobby(
            @PathVariable Long lobbyId,
            @RequestHeader Map<String, String> header) {
        User user = lobbyService.checkAuth(header);
        lobbyService.userExitLobby(user.getId(),lobbyId);

    }


}
