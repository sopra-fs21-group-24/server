package ch.uzh.ifi.hase.soprafs21.controller;

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
    public Lobby getLeobbyWithId(@PathVariable("id") Long lobbyid){

        return lobbyService.getLobbyWithId(lobbyid);

    }

    @GetMapping("/lobby")
    @ResponseStatus(HttpStatus.OK)
    public List getAllLobbies(){

        return lobbyService.getAllLobbies();

    }

    //only for testing
    @PostMapping("/lobby")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);
        Lobby createdLobby = lobbyService.createLobby(lobby);

        return DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(createdLobby);
    }

    @PostMapping("/lobby/{Lobbyid}/{Userid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobby(@PathVariable long Lobbyid,@PathVariable long Userid) {
        Lobby lobbyToJoin = lobbyService.getLobbyWithId(Lobbyid);
        lobbyService.addUserToExistingLobby(userService.getUserByUserId(Userid),lobbyToJoin);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
