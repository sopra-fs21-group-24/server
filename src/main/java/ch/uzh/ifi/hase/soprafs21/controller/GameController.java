package ch.uzh.ifi.hase.soprafs21.controller;

import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.service.UserService;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to the user.
 * The controller will receive the request and delegate the execution to the UserService and finally return the result.
 */
@RestController
public class GameController {

    private final UserService userService;

    GameController(UserService userService) {
        this.userService = userService;
    }

}
