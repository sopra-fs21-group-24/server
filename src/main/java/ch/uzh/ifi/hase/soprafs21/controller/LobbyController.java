package ch.uzh.ifi.hase.soprafs21.controller;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.naming.spi.DirStateFactory.Result;
import javax.xml.bind.annotation.W3CDomHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.context.request.async.DeferredResult;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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

    Logger logger = LoggerFactory.getLogger(LobbyController.class);

    private final LobbyService lobbyService;
    private final UserService userService;
    private final GameService gameService;

    private ExecutorService lobbyClerk = Executors.newFixedThreadPool(5);

    LobbyController(LobbyService lobbyService, UserService userService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/lobby/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LobbyGetDTO getLobbyWithId(@PathVariable("id") Long lobbyid) {
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
    public DeferredResult<List<LobbyGetDTOAllLobbies>> getAllLobbies() throws IllegalStateException{
        // authentication?

        final DeferredResult<List<LobbyGetDTOAllLobbies>> result = new DeferredResult<>(10000L, Collections.emptyList());
        if (!lobbyService.existRequestAllLobbies(result)){
            lobbyService.addRequestToQueueLobbies(result);
            logger.info("Im here!");
            result.setResult(lobbyService.getLobbyGetDTOAllLobbies());
        }

        result.onTimeout(() -> logger.info("timout of request"));
        result.onCompletion(() -> logger.info("Completion of request"));


        lobbyClerk.execute(() -> {
            try {
                // result.setResult(lobbyService.getLobbyGetDTOAllLobbies());
                Thread.sleep(9000L);
            }
            catch (Exception e){
                result.setErrorResult(result);
            }
        });
        
        return result;

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
        return getLobbyWithId(lobby.getId());
    }

    @PutMapping("/lobby/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void userExitLobby(
            @PathVariable Long lobbyId,
            @RequestHeader Map<String, String> header) {
        User user = lobbyService.checkAuth(header);
        
        // when host leaves lobby
        Optional<GameEntity> found = gameService.gameByCreatorUserIdOptional(user.getId());
        if (found.isPresent()){
            gameService.exitGame(found.get());
        }
        else {
            lobbyService.userExitLobby(user,lobbyId);
        }
    }
}
