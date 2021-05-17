package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.repository.LeaderboardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.MissingInformationException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserAlreadyExistsException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

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
    private final LeaderboardRepository leaderboardRepository;


    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository, LeaderboardRepository leaderboardRepository) {
        this.userRepository = userRepository;
        this.leaderboardRepository = leaderboardRepository;
    }

    public User checkAuth(Map<String, String> header){
        try {
            String token = header.get("token");
            return getUserByToken(token);
        }
        catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User login(User unauthorizedUser){
        User foundUser = userRepository.findByUsername(unauthorizedUser.getUsername());
        if (foundUser == null){
            throw new NotFoundException("user with username: '" + unauthorizedUser.getUsername() + "' was not found!");
        }

        // Ensure Password Match
        if (foundUser.getPassword().equals(unauthorizedUser.getPassword())){
            foundUser.setToken(UUID.randomUUID().toString());
            return userRepository.save(foundUser);
        } else {
            throw new NotCreatorException("Invalid Password Username Combination!" + foundUser.getPassword());
        }
    }


    public User logOut(User unauthorizedUser) {
        User foundUser = userRepository.findByUsername(unauthorizedUser.getUsername());
        if (foundUser != null && foundUser.getToken() != null && unauthorizedUser.getToken().equals(foundUser.getToken())){
            foundUser.setInLobby(false);
            // TODO
            // User Aus created game werfen
            return userRepository.save(foundUser);
        } else {
            throw new NotCreatorException("No/Wrong Token was provided to authenticate logout procedure!");
        }
    }

    public User getUserByToken(String token){
        Optional<User> found = userRepository.findByToken(token);
        if (found.isEmpty()){
            throw new NotFoundException("User with this token does not exist");
        }
        return found.get();
    }

    public User getUserByUserId(Long userId){
        Optional<User> found = userRepository.findById(userId);
        if (found.isEmpty()) {
          throw new NotFoundException("User with userId: '" + userId + "' not found");
      }
        return found.get();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(User newUser) {
        // Ensure I have name, username & password
        if (newUser.getPassword() == null || newUser.getPassword().equals("")){
            throw new MissingInformationException("Please provide a password");
        }
        if (newUser.getUsername() == null || newUser.getUsername().equals("")){
            throw new MissingInformationException("Please provide a username");
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
            throw new NotCreatorException("You're trying to update an user other than yourself!");
        }
        if (user.getPassword() != null){
            userToBeUpdated.setPassword(user.getPassword());
        }
        if (user.getUsername() != null){
        for (Leaderboard leaderboard : leaderboardRepository.findAll()){
            if (leaderboardRepository.findByUsername(userToBeUpdated.getUsername()) != null){
                leaderboard.setUsername(user.getUsername());
                leaderboardRepository.save(leaderboard);
                leaderboardRepository.flush();
            }
        }
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

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new UserAlreadyExistsException(String.format(baseErrorMessage, "name", "is"));
        }
    }
}
