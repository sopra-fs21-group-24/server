package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;
import ch.uzh.ifi.hase.soprafs21.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Map;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class GameController {

    final Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;
    private final QuestionService questionService;
    private final ScoreService scoreService;

    GameController(GameService gameService, QuestionService questionService, ScoreService scoreService){
        this.gameService = gameService;
        this.questionService = questionService;
        this.scoreService = scoreService;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(
            @RequestBody GamePostDTOCreate gamePostDTOCreate,
            @RequestHeader Map<String, String> header)
            throws NotFoundException, NotCreatorException, PreconditionFailedException {

        gameService.checkAuth(header);

        GameEntity gameRaw = DTOMapper.INSTANCE.convertGamePostDTOCreateToGameEntity(gamePostDTOCreate);
        gameRaw.setGameModeFromName(gamePostDTOCreate.getGamemode());
        gameRaw.setUserModeFromName(gamePostDTOCreate.getUsermode());

        GameEntity game = gameService.createGame(gameRaw, gamePostDTOCreate.getPublicStatus());

        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
    }


    @GetMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DeferredResult<GameGetDTO> getGame(
            @PathVariable Long gameId,
            @RequestHeader Map<String, String> header)
            throws UnauthorizedException, NotFoundException {

        gameService.checkAuth(header);
        GameEntity game = gameService.gameById(gameId);
        // TODO
        // checkPartofGame, momentan abgeschaltet

        final DeferredResult<GameGetDTO> result = new DeferredResult<>(null);
        gameService.addRequestToQueueGameMap(result, game.getGameId());
        
        result.onTimeout(() -> {
            logger.info("timout of request");
            result.setResult(DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game));
        });

        result.onCompletion(() -> gameService.removeRequestFromGameMap(result));

        if(header.get("initial") == null){
            // TODO
            // logger.info("Initial not found in getGame");
            result.setResult(DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game));
            return result;
        }

        else if (header.get("initial").equals("true")){
            result.setResult(DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game));
        }

        return result;
    }

    @GetMapping("/games/{gameId}/start")
    public GameGetDTO startGame(
            @PathVariable Long gameId,
            @RequestHeader Map<String, String> header)
            throws PreconditionFailedException, NotCreatorException, NotFoundException {

        GameEntity game = gameService.gameById(gameId);
        User user = gameService.checkAuth(header);
        gameService.checkGameCreator(game, user);

        gameService.startGame(gameId);

        return DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(game);
    }

    @GetMapping("/games/{gameId}/exit")
    public void exitGame(
            @PathVariable Long gameId,
            @RequestHeader Map<String, String> header)
            throws PreconditionFailedException, NotCreatorException, NotFoundException {

        GameEntity game = gameService.gameById(gameId);
        User user = gameService.checkAuth(header);
        gameService.checkPartofGame(game, user);

        // case 1: game creator 
        if (user.getId().equals(game.getCreatorUserId())) {
            gameService.exitGame(game);
        }
        else { 
            gameService.exitGameUser(game, user);

        }
    }

    @PutMapping("/games/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO changeGameInfo(
            @PathVariable Long gameId,
            @RequestBody GamePutDTO gamePutDTO,
            @RequestHeader Map<String, String> header)
            throws UnauthorizedException, NotCreatorException {

        GameEntity game = DTOMapper.INSTANCE.convertGamePutDTOToGameEntity(gamePutDTO);

        User user = gameService.checkAuth(header);
        gameService.checkGameCreator(game, user);

        game.setGameId(gameId);
        game.setGameModeFromName(gamePutDTO.getGamemode());
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
            throws UnauthorizedException, PreconditionFailedException, NotFoundException{

        User user = gameService.checkAuth(header);
        // check if user is part of game, reactivate if isse is fixed

        Coordinate guessCoord = answerPostDTO.getCoordGuess();

        Answer answer = DTOMapper.INSTANCE.convertAnwserPostDTOtoAnswer(answerPostDTO);
        answer.setUserId(user.getId());
        answer.setGameId(gameId);

        Score score;

        // case 1: user left, without exit 
        if (guessCoord.getLat() == null || guessCoord.getLon() == null){
            score = gameService.makeZeroScoreGuess(answer);
        } 
        // case 2: user make guess
        else {
            score = gameService.makeGuess(answer);
        }

        ScoreGetDTO scoreDTO = DTOMapper.INSTANCE.convertScoreEntityToScoreGetDTO(score);
        scoreDTO.setSolutionCoordinate(answer.getCoordQuestion());
        scoreDTO.setUsername(user.getUsername());


        return scoreDTO;
    }

    @GetMapping("/games/{gameId}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DeferredResult<List<ScoreGetDTO>> getGameScores(
            @PathVariable Long gameId,
            @RequestHeader Map<String, String> header)
            throws NotFoundException, UnauthorizedException {
        
        GameEntity game = gameService.gameById(gameId);
        User user = gameService.checkAuth(header);
        gameService.checkPartofGame(game, user);

        final DeferredResult<List<ScoreGetDTO>> result = new DeferredResult<>(null);
        gameService.addRequestAllScoreMap(result, game.getGameId());
    
        result.onTimeout(() -> {
            logger.info("timout of request");
            result.setResult(scoreService.getScoreGetDTOs(game));
        });

        result.onCompletion(() -> gameService.removeRequestFromAllScoreMap(result));

        if(header.get("initial") == null){
            result.setResult(scoreService.getScoreGetDTOs(game));
            return result;
        }

        else if (header.get("initial").equals("true")){
            result.setResult(scoreService.getScoreGetDTOs(game));
        }

        return result;
    }

    @GetMapping("/games/{gameId}/questions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Long> getGameQuestions(@PathVariable Long gameId, @RequestHeader Map<String, String> header)
            throws NotFoundException, UnauthorizedException {
        GameEntity game = gameService.gameById(gameId);
        User user = gameService.checkAuth(header);
        gameService.checkPartofGame(game, user);

        // evtl dto creaierten
        return gameService.getQuestionsOfGame(game);
    }

    @PostMapping("/games/{gameId}/questions/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGameQuestionsSpecific(@PathVariable Long gameId, @PathVariable Long questionId,
                                           @RequestBody QuestionGetDTO qDTO, @RequestHeader Map<String, String> header)
            throws NotFoundException, UnauthorizedException, PreconditionFailedException {

        GameEntity game = gameService.gameById(gameId);
        // checkAuth
        // checkPartofGame
        User user = gameService.checkAuth(header);
        gameService.checkPartofGame(game, user);

        List<Long> questions = game.getQuestions();

        questionService.checkQuestionIdInQuestions(questions, questionId);
        Question question = questionService.questionById(questionId);

        return questionService.getMapImage(qDTO.getHeight(), qDTO.getWidth(), question);

    }
}
