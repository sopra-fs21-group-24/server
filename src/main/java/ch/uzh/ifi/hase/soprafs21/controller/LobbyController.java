package ch.uzh.ifi.hase.soprafs21.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTOAllLobbies;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTOWithoutToken;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
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

    LobbyController(LobbyService lobbyService, UserService userService) {
        this.lobbyService = lobbyService;

        this.userService = userService;
    }



    private User checkAuth(Map<String, String> header){
        String token = header.get("token");
        try {
            return userService.getUserByToken(token);
        }
        catch (NotFoundException e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }


    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobbyWithId(@PathVariable("id") Long lobbyid) {
        Lobby lobby = lobbyService.getLobbyById(lobbyid);
        LobbyGetDTO lobbyDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);
        List<UserGetDTOWithoutToken> userlist = new ArrayList<>();
        for (Long i : lobby.getUsers()) {
            userlist.add(DTOMapper.INSTANCE.convertEntityToUserGetDTOWithoutToken(userService.getUserByUserId(i)));

        }
        lobbyDTO.setUsers(userlist);
        return lobbyDTO;
    }
    @GetMapping("/lobby")
    @ResponseStatus(HttpStatus.OK)
    public List<LobbyGetDTOAllLobbies> getAllLobbies(){
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
        return getLobbyWithId(createdLobby.getId());

    }

    @PostMapping("/lobby/{roomKey}/roomkey")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobby(@PathVariable long roomKey, @RequestHeader Map<String, String> header) {
        User user = checkAuth(header);
        Lobby lobbyToJoin = lobbyService.getLobbyByRoomkey(roomKey);
        lobbyService.addUserToExistingLobby(user,lobbyToJoin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobbyWithRoomId(@PathVariable long lobbyId,@RequestHeader Map<String, String> header) {
        User user = checkAuth(header);
        Lobby lobbyToJoin = lobbyService.getLobbyById(lobbyId);
        lobbyService.addUserToExistingLobby(user,lobbyToJoin);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/lobby/roomKey/{roomKey}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobbyWithRoomKey(@PathVariable("roomKey") Long roomKey) {
        Lobby lobby = lobbyService.getLobbyByRoomkey(roomKey);
        return getLobbyWithId(lobby.getId());
    }

    @PutMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void userExitLobby(
            @PathVariable Long lobbyId,
            @RequestHeader Map<String, String> header) {
        User user = checkAuth(header);
        lobbyService.userExitLobby(user.getId(),lobbyId);

    }


}
