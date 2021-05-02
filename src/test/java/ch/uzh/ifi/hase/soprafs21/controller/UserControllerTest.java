package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.*;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST request without actually sending them over the network.
 * This tests if the UserController works.
 */
//@SpringBootTest
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;


    @Test
    public void registerSuccessful() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setUsername("Ben");
        user.setPassword("1234");
        user.setToken("1");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("Ben");
        userPostDTO.setPassword("1234");

        given(userService.createUser(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

    }

    @Test
    public void registerFailed() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("Ben");
        userPostDTO.setPassword("1234");

        given(userService.createUser(Mockito.any())).willThrow(new UserAlreadyExistsException("The name provided is not unique. Therefore, the user could not be created!"));
        given(userService.login(Mockito.any())).willThrow(new UserAlreadyExistsException("The name provided is not unique. Therefore, the user could not be created!"));

        MockHttpServletRequestBuilder postRequest = post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException))
                .andExpect(result -> assertEquals("The name provided is not unique. Therefore, the user could not be created!", result.getResolvedException().getMessage()));

    }

    @Test
    public void loginSuccessful() throws Exception {

        User user = new User();
        Map<String, Integer> highscores = new HashMap<String, Integer>();
        highscores.put("Time", 4000);
        highscores.put("Pixelation", 2000);
        highscores.put("Clouds", 1000);
        user.setId(1L);
        user.setUsername("Ben");
        user.setPassword("1234");
        user.setToken("1");
        user.setHighScores(highscores);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("Ben");
        userPostDTO.setPassword("1234");

        given(userService.login(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("Ben")))
                .andExpect(jsonPath("$.token", is("1")))
                .andExpect(jsonPath("$.highscores", is(user.getHighScores())));

    }

    @Test
    public void loginFailed() throws Exception {


        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("Ben");
        userPostDTO.setPassword("1234");

        given(userService.login(Mockito.any())).willThrow(new NotFoundException("user with username: Ben was not found!"));

        MockHttpServletRequestBuilder postRequest = post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("user with username: Ben was not found!", result.getResolvedException().getMessage()));

    }


    @Test
    public void getUserByIdSuccessful() throws Exception {

        User user = new User();
        Map<String, Integer> highscores = new HashMap<String, Integer>();
        highscores.put("Time", 4000);
        highscores.put("Pixelation", 2000);
        highscores.put("Clouds", 1000);
        user.setId(1L);
        user.setUsername("Ben");
        user.setPassword("1234");
        user.setToken("1");
        user.setHighScores(highscores);

        given(userService.getUserByUserId(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("Ben")))
                .andExpect(jsonPath("$.highscores", is(user.getHighScores())));

    }

    @Test
    public void getUserByIdFailed() throws Exception {

        given(userService.getUserByUserId(Mockito.any())).willThrow(new NotFoundException("User with userId: 1 not found"));

        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("User with userId: 1 not found", result.getResolvedException().getMessage()));

    }

    @Test
    public void updateUserSuccessful() throws Exception {


        Map<String, Integer> highscores = new HashMap<String, Integer>();
        highscores.put("Time", 4000);
        highscores.put("Pixelation", 2000);
        highscores.put("Clouds", 1000);

        User user = new User();
        user.setId(1L);
        user.setUsername("Ben");
        user.setPassword("1234");
        user.setToken("1");
        user.setHighScores(highscores);

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("Max");
        userPostDTO.setPassword("password");

        when(userService.updateUser(user.getId(), user)).thenReturn(user);
        when(userService.checkAuth(Mockito.any())).thenReturn(user);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .header("token", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isOk());

    }


    @Test
    public void updateUserFailedByCheckAuth() throws Exception {

        User user = new User();
        user.setId(1L);
        user.setToken("1");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("Max");
        userPostDTO.setPassword("password");


        when(userService.checkAuth(Mockito.any())).thenThrow(new UnauthorizedException("Unauthorized"));

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO))
                .header("token", "1");

        mockMvc.perform(putRequest)
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UnauthorizedException));

    }

    @Test
    public void updateUserFailedByUpdateUser() throws Exception {

//        User user = new User();
//        user.setId(1L);
//        user.setToken("1");
//
//        UserPostDTO userPostDTO = new UserPostDTO();
//        userPostDTO.setUsername("Max");
//        userPostDTO.setPassword("password");
//
//
//        when(userService.checkAuth(Mockito.any())).thenReturn(user);
//        when(userService.updateUser(user.getId(), user)).thenThrow(new NotCreatorException("You're trying to update an user other than yourself!"));
//
//        MockHttpServletRequestBuilder putRequest = put("/users/1")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userPostDTO))
//                .header("token", "1");
//
//        mockMvc.perform(putRequest)
//                .andExpect(status().isUnauthorized())
//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotCreatorException));

    }

        private String asJsonString (final Object object){
            try {
                return new ObjectMapper().writeValueAsString(object);
            }
            catch (JsonProcessingException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
            }
        }
    }