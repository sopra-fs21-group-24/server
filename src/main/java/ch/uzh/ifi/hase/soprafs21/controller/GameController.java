package ch.uzh.ifi.hase.soprafs21.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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
import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTOCreate;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ScoreGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;
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
    private final QuestionService questionService;

    GameController(GameService gameService, UserService userService, QuestionService questionService) {
        this.gameService = gameService;
        this.userService = userService;
        this.questionService = questionService;
    }

    // evtl implementieren fürs debugging
    @GetMapping("/games")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<GameGetDTO> getAllGames() {

        List<GameGetDTO> allGames = new ArrayList<>();

        for (GameEntity game : gameService.getAllGames()) {
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
    public GameGetDTO getGame(
            @PathVariable Long gameId,
            @RequestHeader Map<String, String> header)
            throws UnauthorizedException, NotFoundException {

        gameService.checkAuth(header);
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

        // case 1: game creator exits
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

        User user = gameService.checkAuth(header);
        // check if user is part of game, reactivate if isse is fixed

        Answer answer = DTOMapper.INSTANCE.convertAnwserPostDTOtoAnswer(answerPostDTO);
        answer.setUserId(user.getId());
        answer.setGameId(gameId);
        Score score = gameService.makeGuess(answer);

        ScoreGetDTO scoreDTO = DTOMapper.INSTANCE.convertScoreEntityToScoreGetDTO(score);
        scoreDTO.setSolutionCoordinate(answer.getCoordQuestion());
        scoreDTO.setUsername(user.getUsername());

        return scoreDTO;
    }

    @GetMapping("/games/{gameId}/scores")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ScoreGetDTO> getGameScores(
            @PathVariable Long gameId,
            @RequestHeader Map<String, String> header)
            throws NotFoundException, UnauthorizedException {

        GameEntity game = gameService.gameById(gameId);
        User user = gameService.checkAuth(header);
        gameService.checkPartofGame(game, user);

        // hacking
        Coordinate solution;
        List<Long> questions = game.getQuestions();
        if (game.getRound() < 4) {
            solution = gameService.questionById(questions.get(game.getRound() - 1)).getCoordinate();
        }
        else {
            solution = gameService.questionById(questions.get(2)).getCoordinate();
        }

        List<ScoreGetDTO> scoresDTO = new ArrayList<>();
        ListIterator<Score> scores = gameService.scoresByGame(game);
        while (scores.hasNext()) {
            ScoreGetDTO scoreGetDTO = DTOMapper.INSTANCE.convertScoreEntityToScoreGetDTO(scores.next());
            User scoreUser = userService.getUserByUserId(scoreGetDTO.getUserId());
            scoreGetDTO.setSolutionCoordinate(solution);
            scoreGetDTO.setUsername(scoreUser.getUsername());
            scoresDTO.add(scoreGetDTO);
        }

        return scoresDTO;
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

    // just for debug resons
    @GetMapping("/games/questions")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Question> getGameQuestions() throws NotFoundException, UnauthorizedException {
        return questionService.getAllQuestions();
    }

    @PostMapping("/games/{gameId}/questions/{questionId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getGameQuestionsSpecific(@PathVariable Long gameId, @PathVariable Long questionId,
                                           @RequestBody QuestionGetDTO qDTO, @RequestHeader Map<String, String> header)
            throws NotFoundException, UnauthorizedException, PreconditionFailedException, MalformedURLException {

        GameEntity game = gameService.gameById(gameId);
        // checkAuth
        // checkPartofGame
        User user = gameService.checkAuth(header);
        gameService.checkPartofGame(game, user);

        List<Long> questions = game.getQuestions();

        questionService.checkQuestionIdInQuestions(questions, questionId);
        Question question = gameService.questionById(questionId);

        return questionService.getMapImage(qDTO.getHeight(), qDTO.getWidth(), question);

    }
}
