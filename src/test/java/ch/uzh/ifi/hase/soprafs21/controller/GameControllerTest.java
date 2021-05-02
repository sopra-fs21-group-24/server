package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Time;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UnauthorizedException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    }

    @Test
    public void createGameFailed() throws Exception {

    }

    @Test
    public void getGameSuccess() throws Exception {

    }

    @Test
    public void getGameFailed() throws Exception {

    }

    @Test
    public void startGameSuccess() throws Exception {

    }

    @Test
    public void startGameFailed() throws Exception {

    }

    @Test
    public void exitGameSuccess() throws Exception {

    }

    @Test
    public void exitGameFailed() throws Exception {

    }

    @Test
    public void changeGameInfoSuccess() throws Exception {

    }

    @Test
    public void changeGameInfoFailed() throws Exception {

    }

    @Test
    public void makeGuessSuccess() throws Exception {

    }

    @Test
    public void makeGuessFailed() throws Exception {

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


        QuestionGetDTO questionGetDTO = new QuestionGetDTO();
        questionGetDTO.setHeight(500);
        questionGetDTO.setWidth(500);

        GameEntity game = new GameEntity();
        game.setGameId(25L);

        Question question = new Question();
        question.setQuestionId(50L);

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        doNothing().when(questionService).checkQuestionIdInQuestions(Mockito.any(), Mockito.any());
        when(gameService.questionById(Mockito.any())).thenReturn(question);
        when(questionService.getMapImage(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).thenReturn("Some String");


        MockHttpServletRequestBuilder postRequest = post("/games/25/questions/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(questionGetDTO))
                .header("token", "1");

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Some String")));

    }

    @Test
    public void getGameQuestionsSpecificFailedByQuestionById() throws Exception {

        GameEntity game = new GameEntity();

        QuestionGetDTO questionGetDTO = new QuestionGetDTO();
        questionGetDTO.setHeight(500);
        questionGetDTO.setWidth(500);

        when(gameService.gameById(Mockito.any())).thenReturn(game);
        doNothing().when(questionService).checkQuestionIdInQuestions(Mockito.any(), Mockito.any());
        when(gameService.questionById(Mockito.any())).thenThrow(new NotFoundException("Question with this questionId is not found"));

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
