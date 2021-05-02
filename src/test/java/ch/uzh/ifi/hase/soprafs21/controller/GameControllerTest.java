package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.service.GameService;
import ch.uzh.ifi.hase.soprafs21.service.LobbyService;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

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

    }

    @Test
    public void getAllGamesFailed() throws Exception {

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

    }

    @Test
    public void getGameQuestionsFailed() throws Exception {

    }

    @Test
    public void getGameQuestionsSpecificSuccess() throws Exception {

    }

    @Test
    public void getGameQuestionsSpecificFailed() throws Exception {

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
