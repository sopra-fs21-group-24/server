package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePostDTOCreate;
import ch.uzh.ifi.hase.soprafs21.rest.dto.GamePutDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;
import ch.uzh.ifi.hase.soprafs21.service.ScoreService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ScoreService scoreService;

    @MockBean
    private GameService gameService;

    @MockBean
    private UserService userService;

    @MockBean
    private QuestionService questionService;

    @Test
    public void getAllGamesSuccess() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());

        GameEntity game2 = new GameEntity();
        game2.setGameId(2L);
        game2.setGameMode(new Pixelation());

        List<GameEntity> gamelist = new ArrayList<>();
        gamelist.add((game));
        gamelist.add((game2));

        given(gameService.getAllGames()).willReturn(gamelist);
        given(gameService.gameById(Mockito.any())).willReturn(game,game2);


        MockHttpServletRequestBuilder getRequest = get("/games")
                .contentType(MediaType.APPLICATION_JSON);

        for (int i=0; i< gamelist.size();i++ ){
            String s = String.valueOf(i);

            mockMvc.perform(getRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath(String.format("$[%s].gameId",s), is(i+1)))
                    .andExpect(jsonPath(String.format("$[%s].gameMode.name",s), is("Pixelation"))) ;
        }



    }
    

    @Test
    public void createGameSuccess() throws Exception {


        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());

        User user = new User();


        given(gameService.checkAuth(Mockito.any())).willReturn(user);
        given(gameService.createGame(Mockito.any(), Mockito.anyBoolean())).willReturn(game);
        given(gameService.gameById(Mockito.any())).willReturn(game);

        GamePostDTOCreate gamepostdo = new GamePostDTOCreate();
        gamepostdo.setPublicStatus(true);
        gamepostdo.setGamemode("Pixelation");
        gamepostdo.setUserId(1L);
        gamepostdo.setUsermode("Singleplayer");

        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamepostdo))
                .header("token","1");

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.gameId", is(1)));

    }

    @Test
    public void createGameFailedUnauthorizedException() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());



        given(gameService.checkAuth(Mockito.any())).willThrow(new UnauthorizedException("Not Authorized"));


        GamePostDTOCreate gamepostdo = new GamePostDTOCreate();
        gamepostdo.setPublicStatus(true);
        gamepostdo.setGamemode("Pixelation");
        gamepostdo.setUserId(1L);
        gamepostdo.setUsermode("Singleplayer");

        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamepostdo))
                .header("token","1");

        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException));

    }

    @Test
    public void createGameFailedNotFoundException() throws Exception {

        given(gameService.createGame(Mockito.any(), Mockito.anyBoolean())).willThrow(new NotFoundException("Not found"));


        GamePostDTOCreate gamepostdo = new GamePostDTOCreate();
        gamepostdo.setPublicStatus(true);
        gamepostdo.setGamemode("Pixelation");
        gamepostdo.setUserId(1L);
        gamepostdo.setUsermode("Singleplayer");

        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamepostdo))
                .header("token","1");

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

    }

    @Test
    public void createGameFailedPreconditionFailedException() throws Exception {

        given(gameService.createGame(Mockito.any(), Mockito.anyBoolean())).willThrow(new PreconditionFailedException("Precondition failed"));


        GamePostDTOCreate gamepostdo = new GamePostDTOCreate();
        gamepostdo.setPublicStatus(true);
        gamepostdo.setGamemode("Pixelation");
        gamepostdo.setUserId(1L);
        gamepostdo.setUsermode("Singleplayer");

        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamepostdo))
                .header("token","1");

        mockMvc.perform(postRequest)
                .andExpect(status().isPreconditionFailed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PreconditionFailedException));

    }



    @Test
    public void getGameSuccess() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());


        given(gameService.gameById(Mockito.any())).willReturn(game);


        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("initial", "true");


        MvcResult asyncListener = mockMvc
                .perform(getRequest)
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(asyncListener))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(1)))
                .andExpect(jsonPath("$.gameMode.name", is("Pixelation"))) ;
    }

    @Test
    public void getGameFailedNotFoundException() throws Exception {

        given(gameService.gameById(Mockito.any())).willThrow(new NotFoundException("Not found"));

        MockHttpServletRequestBuilder getRequest = get("/games/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

    }

    @Test
    public void startGameSuccess() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setGameMode(new Pixelation());

        User user = new User();


        given(gameService.checkAuth(Mockito.any())).willReturn(user);
        given(gameService.startGame(Mockito.any())).willReturn(game);
        given(gameService.gameById(Mockito.any())).willReturn(game);

        GamePostDTOCreate gamepostdo = new GamePostDTOCreate();
        gamepostdo.setPublicStatus(true);
        gamepostdo.setGamemode("Pixelation");
        gamepostdo.setUserId(1L);
        gamepostdo.setUsermode("Singleplayer");

        MockHttpServletRequestBuilder getRequest = get("/games/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId", is(1)))
                .andExpect(jsonPath("$.gameMode.name", is("Pixelation")));

    }

    @Test
    public void startGameFailedNotFoundException() throws Exception {

        given(gameService.startGame(Mockito.any())).willThrow(new NotFoundException("Not found"));

        MockHttpServletRequestBuilder getRequest = get("/games/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException));

    }

    @Test
    public void startGameFailedPreconditionFailedException() throws Exception {

        given(gameService.startGame(Mockito.any())).willThrow(new PreconditionFailedException("Precondition failed"));

        MockHttpServletRequestBuilder getRequest = get("/games/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isPreconditionFailed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PreconditionFailedException));

    }

    @Test
    public void startGameFailedUnauthorizedException() throws Exception {

        given(gameService.startGame(Mockito.any())).willThrow(new UnauthorizedException("Not authorized"));

        MockHttpServletRequestBuilder getRequest = get("/games/1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException));

    }


    @Test
    public void exitGameSuccess() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setCreatorUserId(2L);
        game.setGameMode(new Pixelation());

        User user = new User();
        user.setId(2L);


        given(gameService.checkAuth(Mockito.any())).willReturn(user);
        doNothing().when(gameService).exitGame(Mockito.any());
        given(gameService.gameById(Mockito.any())).willReturn(game);
        given(gameService.gameById(Mockito.any())).willReturn(game);

        MockHttpServletRequestBuilder getRequest = get("/games/1/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());

    }

    @Test
    public void exitGameFailedByCheckAuth() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setCreatorUserId(2L);
        game.setGameMode(new Pixelation());

        User user = new User();
        user.setId(3L);

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        when(gameService.checkAuth(Mockito.any())).thenThrow(new UnauthorizedException("Not authorized"));


        MockHttpServletRequestBuilder getRequest = get("/games/1/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException));

    }



    @Test
    public void exitGameFailedByCheckPartOfGame() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setCreatorUserId(2L);
        game.setGameMode(new Pixelation());

        User user = new User();
        user.setId(3L);


        given(gameService.checkAuth(Mockito.any())).willReturn(user);
        given(gameService.gameById(Mockito.any())).willReturn(game);
        doThrow(new UnauthorizedException("Precondition Failed")).when(gameService).checkPartofGame(Mockito.any(), Mockito.any());


        MockHttpServletRequestBuilder getRequest = get("/games/1/exit")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token","1");

        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException));

    }

    @Test
    public void changeGameInfoSuccess() throws Exception {
        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setCreatorUserId(2L);
        game.setGameMode(new Pixelation());


        User user = new User();
        user.setId(3L);

        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setPublicStatus(true);
        gamePutDTO.setGamemode("Time");
        gamePutDTO.setUserId(1L);


        given(gameService.gameById(Mockito.any())).willReturn(game);
        when(gameService.checkAuth(Mockito.any())).thenReturn(user);
        GameEntity newGame = DTOMapper.INSTANCE.convertGamePutDTOToGameEntity(gamePutDTO);
        newGame.setGameMode(new Time());
        when(gameService.update(Mockito.any(), Mockito.anyBoolean())).thenReturn(newGame);


        MockHttpServletRequestBuilder putRequest = put("/games/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePutDTO))
                .header("token","1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creatorId", is(1)))
                .andExpect(jsonPath("$.gameMode.name", is("Time")));

    }


    @Test
    public void makeGuessSuccess() throws Exception {

        GameEntity game = new GameEntity();
        game.setGameId(1L);
        game.setCreatorUserId(2L);
        game.setRound(1);
        game.setRoundDuration(20);
        game.setCurrentTime();
        game.setRoundStart(12L);
        game.setGameMode(new Pixelation());


        User user = new User();
        user.setId(3L);
        user.setUsername("TestUser");

        Answer answer = new Answer();
        answer.setTimeFactor(12f);
        answer.setUserId(3L);
        answer.setCoordGuess(new Coordinate(12.12,12.12));
        answer.setCoordQuestion(new Coordinate(123.1,123.1));
        Score score = new Score();
        score.setTotalScore(12);
        score.setTempScore(2);
        score.setUserId(3L);
        score.setLastCoordinate(new Coordinate(12.12,12.12));
        given(gameService.gameById(Mockito.any())).willReturn(game);
        when(gameService.checkAuth(Mockito.any())).thenReturn(user);
        when(gameService.makeGuess(Mockito.any())).thenReturn(score);


        MockHttpServletRequestBuilder putRequest = post("/games/1/guess")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answer))
                .header("token","1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(user.getId().intValue())))
                .andExpect(jsonPath("$.tempScore", is(2)))
                .andExpect(jsonPath("$.totalScore", is(12)));


    }

    @Test
    public void makeGuessFailed_NullNullAsCoordinates() throws Exception {
            //Check if timeout answer is accepted

            GameEntity game = new GameEntity();
            game.setGameId(1L);
            game.setCreatorUserId(2L);
            game.setRound(1);
            game.setRoundDuration(20);
            game.setCurrentTime();
            game.setRoundStart(12L);
            game.setGameMode(new Pixelation());


            User user = new User();
            user.setId(3L);
            user.setUsername("TestUser");

            Answer answer = new Answer();
            answer.setTimeFactor(12f);
            answer.setUserId(3L);
            //timeout answer
            answer.setCoordGuess(new Coordinate(null,null));
            answer.setCoordQuestion(new Coordinate(123.1,123.1));
            Score score = new Score();
            score.setTotalScore(12);
            score.setTempScore(0);
            score.setUserId(3L);
            score.setLastCoordinate(new Coordinate(12.12,12.12));
            given(gameService.gameById(Mockito.any())).willReturn(game);
            when(gameService.checkAuth(Mockito.any())).thenReturn(user);
            when(gameService.makeZeroScoreGuess(Mockito.any())).thenReturn(score);


            MockHttpServletRequestBuilder putRequest = post("/games/1/guess")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(answer))
                    .header("token","1");

            mockMvc.perform(putRequest)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId", is(user.getId().intValue())))
                    .andExpect(jsonPath("$.tempScore", is(0)))
                    .andExpect(jsonPath("$.totalScore", is(12)));

    }

    @Test
    public void getGameScoresSuccess() throws Exception {


    }

    @Test
    public void getGameScoresFailed() throws Exception {

    }

    @Test
    public void getGameQuestionsSuccess() throws Exception {

        List<Long> questions = new ArrayList<>();
        GameEntity game = new GameEntity();
        game.setQuestions(questions);

        User user = new User();

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        when(gameService.checkAuth(Mockito.any())).thenReturn(user);
        doNothing().when(gameService).checkPartofGame(Mockito.any(), Mockito.any());
        when(gameService.getQuestionsOfGame(Mockito.any())).thenReturn(questions);


        MockHttpServletRequestBuilder getRequest = get("/games/25/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(game.getQuestions())));

    }

    @Test
    public void getGameQuestionsFailedByCheckPartOfGame() throws Exception {

        GameEntity game = new GameEntity();

        User user = new User();

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        when(gameService.checkAuth(Mockito.any())).thenReturn(user);
        doThrow(new UnauthorizedException("Non player is trying to acess an only-player component")).when(gameService).checkPartofGame(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder getRequest = get("/games/25/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException))
                .andExpect(result -> assertEquals("Non player is trying to acess an only-player component", result.getResolvedException().getMessage()));

    }

    @Test
    public void getGameQuestionsFailedByCheckAuth() throws Exception {

        GameEntity game = new GameEntity();


        when(gameService.gameById(Mockito.any())).thenReturn(game);
        when(gameService.checkAuth(Mockito.any())).thenThrow(new UnauthorizedException("Not Authorized"));

        MockHttpServletRequestBuilder getRequest = get("/games/25/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(getRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException))
                .andExpect(result -> assertEquals("Not Authorized", result.getResolvedException().getMessage()));

    }

    @Test
    public void getGameQuestionsFailedByGameById() throws Exception {

        GameEntity game = new GameEntity();


        when(gameService.gameById(Mockito.any())).thenThrow(new NotFoundException("Game with this gameId does not exist"));

        MockHttpServletRequestBuilder getRequest = get("/games/25/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", "1");

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Game with this gameId does not exist", result.getResolvedException().getMessage()));

    }

    @Test
    public void getGameQuestionsSpecificSuccess() throws Exception {

        // Create QuestionGetDTO to send in POST request
        QuestionGetDTO questionGetDTO = new QuestionGetDTO();
        questionGetDTO.setHeight(500);
        questionGetDTO.setWidth(500);

        // Create GameEntity
        GameEntity game = new GameEntity();

        // Create Question
        Question question = new Question();

        // Create User
        User user = new User();

        // Mock methods from services to avoid exceptions
        when(gameService.checkAuth(Mockito.any())).thenReturn(user);
        when(gameService.gameById(Mockito.any())).thenReturn(game);
        doNothing().when(questionService).checkQuestionIdInQuestions(Mockito.any(), Mockito.any());
        when(questionService.questionById(Mockito.any())).thenReturn(question);
        when(questionService.getMapImage(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("SomeEncodedString");

        // Create POST request
        MockHttpServletRequestBuilder postRequest = post("/games/25/questions/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(questionGetDTO))
                .header("token", "1");

        // Send POST request and check response & status
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("SomeEncodedString")));

    }

    @Test
    public void getGameQuestionsSpecificFailedByQuestionById() throws Exception {

        GameEntity game = new GameEntity();

        QuestionGetDTO questionGetDTO = new QuestionGetDTO();
        questionGetDTO.setHeight(500);
        questionGetDTO.setWidth(500);

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        doNothing().when(questionService).checkQuestionIdInQuestions(Mockito.any(), Mockito.any());
        when(questionService.questionById(Mockito.any())).thenThrow(new NotFoundException("Question with this questionId is not found"));

        MockHttpServletRequestBuilder putRequest = post("/games/25/questions/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(questionGetDTO))
                .header("token", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Question with this questionId is not found", result.getResolvedException().getMessage()));

    }

    @Test
    public void getGameQuestionsSpecificFailedByCheckQuestionIdInQuestions() throws Exception {

        GameEntity game = new GameEntity();

        QuestionGetDTO questionGetDTO = new QuestionGetDTO();
        questionGetDTO.setHeight(500);
        questionGetDTO.setWidth(500);

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        doThrow(new PreconditionFailedException("Question with this id is not part of the game")).when(questionService).checkQuestionIdInQuestions(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = post("/games/25/questions/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(questionGetDTO))
                .header("token", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isPreconditionFailed())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof PreconditionFailedException))
                .andExpect(result -> assertEquals("Question with this id is not part of the game", result.getResolvedException().getMessage()));

    }

    @Test
    public void getGameQuestionsSpecificFailedByGameById() throws Exception {

        QuestionGetDTO questionGetDTO = new QuestionGetDTO();
        questionGetDTO.setHeight(500);
        questionGetDTO.setWidth(500);

        when(gameService.gameById(Mockito.any())).thenThrow(new NotFoundException("Game with this gameId does not exist"));

        MockHttpServletRequestBuilder putRequest = post("/games/25/questions/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(questionGetDTO))
                .header("token", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("Game with this gameId does not exist", result.getResolvedException().getMessage()));

    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }

}
