package ch.uzh.ifi.hase.soprafs21.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getAllGames(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createGame(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getGame(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
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
