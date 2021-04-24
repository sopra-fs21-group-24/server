package ch.uzh.ifi.hase.soprafs21.controller;

import java.util.List;
import java.util.Map;

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

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.User;
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

    Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;
    private final UserService userService;

    GameController(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    private User checkAuth(Map<String, String> header){
        try {
            String token = header.get("token");
            return userService.getUserByToken(token);
        }
        catch (NotFoundException e) {
            return null;
        }
    }

    // evtl implementieren fürs debugging
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getAllGames(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @PostMapping("/games")
    public ResponseEntity<GamePostDTOCreate> createGame(
        @RequestBody GamePostDTOCreate gamePostDTOCreate,
        @RequestHeader Map<String, String> header
    ) {

        if(checkAuth(header) == null){
            return ResponseEntity.status(403).body(null);
        }
        
        try {
            GameEntity gameRaw = DTOMapper.INSTANCE.convertGamePostDTOCreateToGameEntity(gamePostDTOCreate);
            gameService.createGame(gameRaw);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.status(400).body(gamePostDTOCreate);
        }

        return ResponseEntity.status(201).body(gamePostDTOCreate);
    }


    @GetMapping("/games/{gameId}")
    public ResponseEntity<GameGetDTO> getGame(
        @PathVariable Long gameId, 
        @RequestHeader Map<String, String> header
    ) {

        try {
            GameEntity game = gameService.gameById(gameId);
            User user = checkAuth(header);

            if(user == null){
                return ResponseEntity.status(403).body(null);
            }

            if(!game.getUserIds().contains(user.getId())){
                return ResponseEntity.status(401).body(null);
            }

            GameGetDTO responseDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
            return ResponseEntity.status(200).body(responseDTO);
        } 
        catch(NotFoundException e){
            return ResponseEntity.status(400).body(null);
        }
    }

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public UserGetDTO changeGameInfo(@RequestBody UserPostDTO userPostDTO) {
        return new UserGetDTO();
    }

    @PostMapping("/games/{gameId}/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<Long>> makeGuess(@RequestBody UserPostDTO userPostDTO) {
        return ResponseEntity.status(200).body(null);
    }

    @GetMapping("/games/{gameId}/questions")
    public ResponseEntity<List<Long>> gameQuestions(
        @PathVariable Long gameId, 
        @RequestHeader Map<String, String> header
    ) {

        try {
            GameEntity game = gameService.gameById(gameId);
            User user = checkAuth(header);

            if(user == null){
                return ResponseEntity.status(403).body(null);
            }

            if(!game.getUserIds().contains(user.getId())){
                return ResponseEntity.status(401).body(null);
            }

            return ResponseEntity.status(200).body(game.getQuestions());
        } 
        catch(NotFoundException e){
            return ResponseEntity.status(400).body(null);
        }
    }

    @GetMapping("/games/{gameId}/questions/{questionId}")
    public ResponseEntity<Question> gameQuestionsSpecific(
        @PathVariable Long gameId,
        @PathVariable Long questionId,
        @RequestHeader Map<String, String> header
    ) {

        try {
            GameEntity game = gameService.gameById(gameId);
            User user = checkAuth(header);

            if(user == null){
                return ResponseEntity.status(403).body(null);
            }

            if(!game.getUserIds().contains(user.getId())){
                return ResponseEntity.status(401).body(null);
            }

            List<Long> questions = game.getQuestions();
            if (!questions.contains(questionId)){
                return ResponseEntity.status(401).body(null);
            }

            Question question = gameService.questionById(questionId);
            return ResponseEntity.status(200).body(question);
        } 
        catch(NotFoundException e){
            return ResponseEntity.status(400).body(null);
        }
    }
}
