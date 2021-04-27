package ch.uzh.ifi.hase.soprafs21.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.entity.Answer;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTOCreate;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ScoreGetDTO;
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
            String token = header.get("token");
            try {
                return userService.getUserByToken(token);
            }
            catch (NotFoundException e) {
                throw new UnauthorizedException(e.getMessage());
            }
    }

    private void checkPartofGame(GameEntity game, User user){
        if(!game.getUserIds().contains(user.getId())){
            throw new UnauthorizedException("Non player is trying to acess an only-player component");
        }
            
    }

    private void checkGameCreator(GameEntity game, User user){
       if (!user.getId().equals(game.getCreatorUserId())) {
            throw new NotCreatorException("User starting the game is not the game-creator");
        } 
    }

    // evtl implementieren fürs debugging
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllGames(){

        List<GameGetDTO> allGames = new ArrayList<>();

        for(GameEntity game : gameService.getAllGames()){
            allGames.add(DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game));
        }

        return allGames;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(
        @RequestBody GamePostDTOCreate gamePostDTOCreate,
        @RequestHeader Map<String, String> header) 
        throws NotFoundException, NotCreatorException, PreconditionFailedException{

        checkAuth(header);
        
        GameEntity gameRaw = DTOMapper.INSTANCE.convertGamePostDTOCreateToGameEntity(gamePostDTOCreate);
        gameRaw.setGameModeFromName(gamePostDTOCreate.getGamemode());
        gameRaw.setUserModeFromName(gamePostDTOCreate.getUsermode());

        GameEntity game = gameService.createGame(gameRaw, gamePostDTOCreate.getPublicStatus());

        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
    }


    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(
        @PathVariable Long gameId, 
        @RequestHeader Map<String, String> header) 
        throws UnauthorizedException, NotFoundException {

        checkAuth(header);
        GameEntity game = gameService.gameById(gameId);
        // checkPartofGame, momentan abgeschaltet

        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
    }

    @GetMapping("/games/{gameId}/start")
    public GameGetDTO startGame(
        @PathVariable Long gameId, 
        @RequestHeader Map<String, String> header) 
        throws PreconditionFailedException, NotCreatorException, NotFoundException {

        GameEntity game = gameService.gameById(gameId);
        User user= checkAuth(header);
        checkGameCreator(game, user);


        try {
            gameService.startGame(gameId);
            return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
        }
        catch (NumberFormatException e){
            throw new PreconditionFailedException("userId is in a wrong format");
        }
    }

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO changeGameInfo(
        @PathVariable Long gameId,
        @RequestBody GamePutDTO gamePutDTO,
        @RequestHeader Map<String, String> header) 
        throws UnauthorizedException, NotCreatorException{

        GameEntity game = DTOMapper.INSTANCE.convertGamePutDTOToGameEntity(gamePutDTO);

        User user = checkAuth(header);
        checkGameCreator(game, user);

        game.setGameId(gameId);
        game.setGameModeFromName(gamePutDTO.getGamemode());
        game.setUserModeFromName(gamePutDTO.getUsermode());
        GameEntity gameLocal = gameService.update(game, gamePutDTO.getPublicStatus());

        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(gameLocal);
    }

    @PostMapping("/games/{gameId}/guess")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ScoreGetDTO makeGuess(
        @PathVariable Long gameId,
        @RequestBody AnswerPostDTO answerPostDTO,
        @RequestHeader Map<String, String> header) 
    throws UnauthorizedException, PreconditionFailedException {

        User user = checkAuth(header);
        // check if user is part of game

        Answer answer = DTOMapper.INSTANCE.convertAnwserPostDTOtoAnswer(answerPostDTO);
        answer.setUserId(user.getId());
        gameService.makeGuess(answer);

        return new ScoreGetDTO();
    }

    @GetMapping("/games/{gameId}/questions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Long> gameQuestions(
        @PathVariable Long gameId, 
        @RequestHeader Map<String, String> header) throws NotFoundException, UnauthorizedException {
        GameEntity game = gameService.gameById(gameId);
        User user = checkAuth(header);
        checkPartofGame(game, user);

        // evtl dto creaierten
        return game.getQuestions();
    }

    @GetMapping("/games/{gameId}/questions/{questionId}") // evtl überflüsssig
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Question gameQuestionsSpecific(
        @PathVariable Long gameId,
        @PathVariable Long questionId,
        @RequestHeader Map<String, String> header) throws NotFoundException, UnauthorizedException, PreconditionFailedException {

        GameEntity game = gameService.gameById(gameId);
        User user = checkAuth(header);
        checkPartofGame(game, user);

        List<Long> questions = game.getQuestions();

        if (!questions.contains(questionId)){
            throw new PreconditionFailedException("Question with this id is not part of the game");
        }

        return gameService.questionById(questionId);
    }
}
