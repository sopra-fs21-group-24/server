package ch.uzh.ifi.hase.soprafs21.controller;

import java.util.Map;
import java.util.Optional;

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
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTOCreate;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    // evtl implementieren fürs debugging
    @PostMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getAllGames(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @GetMapping("/games")
    public ResponseEntity<GamePostDTOCreate> createGame(@RequestBody GamePostDTOCreate gamePostDTOCreate,
    @RequestHeader Map<String, String> header) {

        GameEntity gameRaw = DTOMapper.INSTANCE.convertGamePostDTOCreateToGameEntity(gamePostDTOCreate);

        String token = header.get("token");

        try {
            userService.getUserByToken(token);
            return ResponseEntity.created().RequestBody(gamePostDTOCreate);
        }
        catch (NotFoundException e) {

        }


        gameService.createGame(gameRaw);
        // catch errors and return other code

        return gamePostDTOCreate;
    }


    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable Long gameId) {
        Optional<GameEntity> found = gameService.gameById(gameId);
        if(found.isEmpty()){
            throw new NotFoundException("Game with this gameId does not exist");
        }

        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(found.get());
    
    }

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public UserGetDTO changeGameInfo(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @PostMapping("/games/{gameId}/guess")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO makeGuess(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @GetMapping("/games/{gameId}/questions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO gameQuestions(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @GetMapping("/games/{gameId}/questions/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO gameQuestionsSpecific(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }
}
