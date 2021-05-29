package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserAlreadyExistsException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void createUser_validInputs_success() {

       assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUser");
        testUser.setInLobby(true);

        // when
        User createdUser = userService.createUser(testUser);

        // then
        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getInLobby(), createdUser.getInLobby());
        assertNotNull(createdUser.getToken());
    }

    @Test
    public void createUser_duplicateUsername_throwsException() {

        assertNull(userRepository.findByUsername("testUser"));

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUser");
        testUser.setInLobby(true);

        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        // change the password but forget about the username
        testUser2.setPassword("passwordNew");
        testUser2.setUsername("testUser");

        // check that an error is thrown
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(testUser2));
    }

    @Test
    public void updateUserName_throwsException() {

        assertNull(userRepository.findByUsername("testUser"));

        User testUser = new User();
        testUser.setPassword("password");
        testUser.setUsername("testUser");
        testUser.setInLobby(true);

        User createdUser = userService.createUser(testUser);

        // attempt to create second user with same username
        User testUser2 = new User();

        testUser2.setPassword("passwordNew");
        testUser2.setUsername("testUser2");

        User createdUser2 = userService.createUser(testUser2);
        //Chose an already used username
        createdUser2.setUsername("testUser");
        // check that an error is thrown
        assertThrows(DataIntegrityViolationException.class, () -> userService.updateUser(testUser2.getId(), testUser2));
    }
}
