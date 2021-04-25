package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.MissingInformationException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import ch.uzh.ifi.hase.soprafs21.controller.UserController;

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
public void assert_successful_registration() throws Exception{

}

@Test
public void assert_successful_login() throws Exception {

}

/*@Test
public void assert_successful_logout() throws Exception {

}

@Test
public void assert_successful_registration() throws Exception{

}

@Test
public void assert_successful_login() throws Exception {

}

@Test
public void assert_successful_logout() throws Exception {

}*/




/*
    @Test
    // Test #1
    public void user_successfully_posts() throws Exception {
        User user = new User();
        user.setName("Jerome");
        user.setId(1L);

        user.setToken("1");
        user.setUsername("jeromehadorn");
        user.setPassword("password");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Jerome");
        userPostDTO.setUsername("jeromehadorn");
        userPostDTO.setPassword("password");


        given(userService.createUser(Mockito.any())).willReturn(user);
        //given(authService.login(Mockito.any())).willReturn(user);


        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        this.mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("/users/"+user.getId().toString())));
        *//*.andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.token", is("1")))
                .andExpect(jsonPath("$.logged_in", is("ONLINE")));*//*

    }

    @Test
    // Test #2
    public void user_posts_fail() throws Exception {
        User user = new User();
        user.setName("Jerome");
        user.setId(1L);
        user.setToken("1");
        user.setUsername("jeromehadorn");
        user.setPassword("password");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Jerome");
        userPostDTO.setUsername("jeromehadorn");
        // Left out on purpose
        //userPostDTO.setPassword("password");

        given(userService.createUser(Mockito.any())).willThrow(new MissingInformationException("Please provide a password"));

        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        this.mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MissingInformationException))
                .andExpect(result -> assertEquals("Please provide a password", result.getResolvedException().getMessage()));

    }

    @Test
    // Test #3
    public void get_user_by_id() throws Exception {
        String userId = "1";

        User user = new User();
        user.setName("Jerome");
        user.setId(1L);
        user.setToken("1");
        user.setUsername("jeromehadorn");
        user.setPassword("password");

        given(userService.getUserByUserId(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.logged_in", is("ONLINE")))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.token", is("1")))
                .andExpect(jsonPath("$.logged_in", is("ONLINE")));

    }

    @Test
    // Test #4
    public void get_user_by_id_fail() throws Exception {
        String userId = "42";

        User user = new User();
        user.setName("Jerome");
        user.setId(1L);
        user.setToken("1");
        user.setUsername("jeromehadorn");
        user.setPassword("password");

        given(userService.getUserByUserId(Mockito.any()))
                .willThrow(new NotFoundException("User with userId: '" + userId + "' not found"));


        MockHttpServletRequestBuilder getRequest = get("/users/1")
                .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("User with userId: '" + userId + "' not found", result.getResolvedException().getMessage()));

    }


    @Test
    // Test #5
    public void update_user() throws Exception {
        String userId = "1";

        User user = new User();
        user.setName("Jerome Updated");
        user.setId(1L);
        user.setToken("1");
        user.setUsername("jeromehadorn");
        user.setPassword("password");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Jerome Updated");


        //given(userService.updateUser(Mockito.any(),Mockito.any())).willReturn(user);
        //given(userService.updateUser(user.getId(),user)).willReturn(user);

        MockHttpServletRequestBuilder putRequest = put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        this.mockMvc.perform(putRequest)
                .andDo(print())
                .andExpect(status().isNoContent());
    }


    @Test
    // Test #6
    public void update_user_not_found() throws Exception {
        String userId = "1";

        User user = new User();
        user.setName("Jerome Updated");
        user.setId(1L);
        user.setToken("1");
        user.setUsername("jeromehadorn");
        user.setPassword("password");

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setName("Jerome Updated");


        // given(userService.updateUser(Mockito.any(),Mockito.any())).willReturn(user);
        given(userService.updateUser(Mockito.anyLong(), Mockito.any()))
                .willThrow(new NotFoundException("User with userId: '" + userId + "' not found"));

        MockHttpServletRequestBuilder putRequest = put("/users/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));


        this.mockMvc.perform(putRequest)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NotFoundException))
                .andExpect(result -> assertEquals("User with userId: '" + userId + "' not found", result.getResolvedException().getMessage()));
        *//*this.mockMvc.perform(putRequest)
                .andDo(print())
                .andExpect(status().isNoContent());*//*
    }



    *//**
     * Helper Method to convert userPostDTO into a JSON string such that the input can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     * @param object
     * @return string
     *//*
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The request body could not be created.%s", e.toString()));
        }
    }*/


}