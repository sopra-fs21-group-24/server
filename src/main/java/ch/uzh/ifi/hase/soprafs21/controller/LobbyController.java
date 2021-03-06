package ch.uzh.ifi.hase.soprafs21.controller;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;


@RestController
public class LobbyController {
    final Logger logger = LoggerFactory.getLogger(LobbyController.class);

    private final LobbyService lobbyService;
    private final GameService gameService;

    LobbyController(LobbyService lobbyService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.gameService = gameService;
    }

    @GetMapping("/lobby/{id}") @ResponseStatus(HttpStatus.OK)
    public DeferredResult<LobbyGetDTO> getLobbyWithId(
        @PathVariable("id") Long lobbyId, @RequestHeader Map<String, String> header) {

        Lobby lobby = lobbyService.getLobbyById(lobbyId);
        GameEntity game = gameService.gameById(lobby.getGameId());

        final DeferredResult<LobbyGetDTO> result = new DeferredResult<>(5000L);
        lobbyService.addRequestToQueueLobbyMap(result, lobby.getId());

        result.onTimeout(() -> {
            logger.info("timout of request");
            result.setResult(lobbyService.getLobbyGetDTO(lobby, game.getGameMode()));
        });

        result.onCompletion(() -> lobbyService.removeRequestFromLobbyMap(result));

        if(header.get("initial") == null){
            result.setResult(lobbyService.getLobbyGetDTO(lobby, game.getGameMode()));
        }

        if (header.get("initial").equals("true")){
            result.setResult(lobbyService.getLobbyGetDTO(lobby, game.getGameMode()));
        }

        return result;
    }

    @GetMapping("/lobby")
    @ResponseStatus(HttpStatus.OK)
    public DeferredResult<List<LobbyGetDTOAllLobbies>> getAllLobbies(@RequestHeader Map<String, String> header) 
    throws IllegalStateException {
        // TODO
        // authentication?

        final DeferredResult<List<LobbyGetDTOAllLobbies>> result = new DeferredResult<>(3000L);
        lobbyService.addRequestToQueueLobbies(result);

        result.onTimeout(() -> result.setResult(lobbyService.getLobbyGetDTOAllLobbies()));

        result.onCompletion(() -> lobbyService.removeRequestFromQueueLobbies(result));

        if(header.get("initial") == null){
            result.setResult(lobbyService.getLobbyGetDTOAllLobbies());
        }

        if (header.get("initial").equals("true")){
            result.setResult(lobbyService.getLobbyGetDTOAllLobbies());
        }

        return result;

    }


    @PostMapping("/lobby/{roomKey}/roomkey")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> joinLobby(@PathVariable long roomKey, @RequestHeader Map<String, String> header) {
        User user = lobbyService.checkAuth(header);
        Lobby lobbyToJoin = lobbyService.getLobbyByRoomkey(roomKey);
        lobbyService.addUserToExistingLobby(user,lobbyToJoin);
        String response = String.format("{\"lobbyId\": \"%s\"}", lobbyToJoin.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PutMapping("/lobby/{lobbyId}/leave")
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
