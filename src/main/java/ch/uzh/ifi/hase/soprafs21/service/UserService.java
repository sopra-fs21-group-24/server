package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.MissingInformationException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PerformingUnauthenticatedAction;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserAlreadyExistsException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(User unauthorizedUser){
        //TODO: set online status
        User foundUser = userRepository.findByUsername(unauthorizedUser.getUsername());
        if (foundUser == null){
            throw new NotFoundException("user with userId: '" + unauthorizedUser.getUsername() + "' was not found!");
        }
        System.out.println(foundUser);
        // Ensure Password Match
        if (foundUser.getPassword().equals(unauthorizedUser.getPassword())){
            User newUser = userRepository.save(foundUser);
            return newUser;
        } else {
            // Not great from a security stand point
            //throw new UserAlreadyExistsException();
            throw new PerformingUnauthenticatedAction("Invalid Password Username Combination!" + foundUser.getPassword());
        }
    }


    public User logOut(User unauthorizedUser) {

        User foundUser = userRepository.findByUsername(unauthorizedUser.getUsername());
        if (unauthorizedUser.getToken().equals(foundUser.getToken())){
            User newUser = userRepository.save(foundUser);
            return newUser;
        } else {
            throw new PerformingUnauthenticatedAction("No/Wrong Token was provided to authenticate logout procedure!");
        }
    }

    public User getUserByUserId(Long userId){
        Optional<User> found = null;
        found = this.userRepository.findById(userId);
        if (!found.isPresent()) {
          throw new NotFoundException("User with userId: '" + userId + "' not found");
      }
        return found.orElseThrow();
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public String greet() {
        return "Hello, World";
    }

    public User createUser(User newUser) {
        // Ensure I have name, username & password
        if (newUser.getPassword() == null || newUser.getPassword().equals("")){
            throw new MissingInformationException("Please provide a password");
        }
        if (newUser.getUsername() == null || newUser.getUsername().equals("")){
            throw new MissingInformationException("Please provide a username");
        }
        if (newUser.getName() == null || newUser.getName().equals("")){
            throw new MissingInformationException("Please provide a name");
        }

        newUser.setToken(UUID.randomUUID().toString());


        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User updateUser(long userId, User user) {
        User userToBeUpdated = getUserByUserId(userId);
        // ensure userId matches token, e.g. you can only update yourself
        if (!userToBeUpdated.getToken().equals(user.getToken())) {
            throw new PerformingUnauthenticatedAction("You're trying to update an user other than yourself!");
        }

        if (user.getName() != null){
            userToBeUpdated.setName(user.getName());
        }
        if (user.getPassword() != null){
            userToBeUpdated.setPassword(user.getPassword());
        }
        if (user.getUsername() != null){
            userToBeUpdated.setUsername(user.getUsername());
        }


        User newUser = userRepository.save(userToBeUpdated);
        userRepository.flush();
        log.debug("Information updated for User: {}", newUser);
        return newUser;
    }

    /**
     * This is a helper method that will check the uniqueness criteria of the username and the name
     * defined in the User entity. The method will do nothing if the input is unique and throw an error otherwise.
     *
     * @param userToBeCreated
     * @throws org.springframework.web.server.ResponseStatusException
     * @see User
     */
    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
        User userByName = userRepository.findByName(userToBeCreated.getName());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null && userByName != null) {
            throw new UserAlreadyExistsException(String.format(baseErrorMessage, "username and the name", "are"));
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username and the name", "are"));
        }
        else if (userByUsername != null) {
            throw new UserAlreadyExistsException(String.format(baseErrorMessage, "username", "is"));
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
        }
        else if (userByName != null) {
            throw new UserAlreadyExistsException(String.format(baseErrorMessage, "name", "is"));
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "name", "is"));
        }
    }
}