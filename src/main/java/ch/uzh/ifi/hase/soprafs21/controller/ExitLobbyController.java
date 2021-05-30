package ch.uzh.ifi.hase.soprafs21.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;

@RestController
public class ExitLobbyController {
    
    private final LobbyService lobbyService;
    private final GameService gameService;

    ExitLobbyController(LobbyService lobbyService, GameService gameService) {
        this.lobbyService = lobbyService;
        this.gameService = gameService;
    }

    @PutMapping("/v2/lobby/{lobbyId}")
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
