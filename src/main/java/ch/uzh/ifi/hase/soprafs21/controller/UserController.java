package ch.uzh.ifi.hase.soprafs21.controller;

import java.util.Map;

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

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    private User checkAuth(Map<String, String> header){
        try {
            String token = header.get("token");
            return userService.getUserByToken(token);
        }
        catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO register(
        @RequestBody UserPostDTO userPostDTO
        ) {

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User createdUser = userService.createUser(user);
        User loggedInUser = userService.login(createdUser);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO login(
        @RequestBody UserPostDTO userPostDTO
        ) {
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        User authenticatedUser = userService.login(user);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(authenticatedUser);
    }


    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<UserGetDTO> logout(
        @RequestBody UserPostDTO userPostDTO,
        @RequestHeader Map<String, String> header
        ) {
       
        User userFromToken = checkAuth(header);
        if(userFromToken == null){
            return ResponseEntity.status(403).body(null);
        }

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User loggedOutUser = userService.logOut(user);

        UserGetDTO response = DTOMapper.INSTANCE
                                       .convertEntityToUserGetDTO(loggedOutUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{ID}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO getUserById(
        @PathVariable("ID") long userId
        ){
        User foundUser = userService.getUserByUserId(userId);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }

    @PutMapping("/users/{ID}")                             
    public ResponseEntity<UserGetDTO> updateUser(
        @PathVariable("ID") long userId, 
        @RequestBody UserPostDTO userPostDTO,
        @RequestHeader Map<String, String> header
        ){
        
        User userFromToken = checkAuth(header);
        if(userFromToken == null){
            return ResponseEntity.status(403).body(null);
        }

        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User updatedUser = userService.updateUser(userId, user);
        UserGetDTO response = DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
        
        return ResponseEntity.ok(response);
    }


}
