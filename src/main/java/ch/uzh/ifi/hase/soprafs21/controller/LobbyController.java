package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTOWithoutToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLeobbyWithId(@PathVariable("id") Long lobbyid) {
        Lobby lobby = lobbyService.getLobbyWithId(lobbyid);
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
    public List<LobbyGetDTO> getAllLobbies(){
        List<LobbyGetDTO> finalLobbyList = new ArrayList<>();
        for (Lobby i : lobbyService.getAllLobbies()) {
            LobbyGetDTO lobbyDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(i);
            for (Long x : i.getUsers()){
                List<UserGetDTOWithoutToken> userlist = new ArrayList<>();
            userlist.add(DTOMapper.INSTANCE.convertEntityToUserGetDTOWithoutToken(userService.getUserByUserId(x)));

            lobbyDTO.setUsers(userlist);
            }
            finalLobbyList.add(lobbyDTO);
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

    @PostMapping("/lobby/{roomKey}/{Userid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobby(@PathVariable long roomKey,@PathVariable long Userid) {
        Lobby lobbyToJoin = lobbyService.getLobbyWithRoomKey(roomKey);
        lobbyService.addUserToExistingLobby(userService.getUserByUserId(Userid),lobbyToJoin);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/lobby/roomKey/{roomKey}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobbyWithRoomKey(@PathVariable("roomKey") Long roomKey) {
        Lobby lobby = lobbyService.getLobbyWithRoomKey(roomKey);
        return getLeobbyWithId(lobby.getId());
    }


}
