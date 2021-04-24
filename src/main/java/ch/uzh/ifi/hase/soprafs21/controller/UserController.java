package ch.uzh.ifi.hase.soprafs21.controller;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs21.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO register(@RequestBody UserPostDTO userPostDTO) {
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User createdUser = userService.createUser(user);
        User loggedInUser = userService.login(createdUser);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);
    }

    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO login(@RequestBody UserPostDTO userPostDTO) {
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // Check if user exists and has credentials
        User authenticatedUser = userService.login(user);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(authenticatedUser);
    }


    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO logout(@RequestBody UserPostDTO userPostDTO) {
        // Ensure he sends his token so I know it's him
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User loggedOutUser = userService.logOut(user);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedOutUser);
    }

    // Getting a User Profile by ID
    @GetMapping("/users/{ID}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDTO getUserById(@PathVariable("ID") long userId){
        User foundUser = userService.getUserByUserId(userId);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(foundUser);
    }

    // Updating a User Profile by ID
    @PutMapping("/users/{ID}")                              //Token as identificaion
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserGetDTO updateUser(@PathVariable("ID") long userId, @RequestBody UserPostDTO userPostDTO){
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User updatedUser = userService.updateUser(userId, user);
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
    }


}
