package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.UserAlreadyExistsException;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("MaPassword");
        testUser.setToken("token");
        testUser.setInLobby(true);
        //testUser.setHighScores(Map<>);TODO:
        testUser.setUsername("testUsername");

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {

        // when -> any object is being save in the userRepository -> return the dummy testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getId(), createdUser.getId());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertEquals(testUser.getInLobby(), createdUser.getInLobby());
        assertEquals(testUser.getToken(), createdUser.getToken());
        assertEquals(testUser.getPassword(), createdUser.getPassword());
        assertNotNull(createdUser.getToken());
    }

    @Test
    public void createUser_duplicateInputs_throwsException() {

        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(testUser));

    }

    @Test
    public void checkAuth_success() {
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));

        HashMap<String,String> map = new HashMap<>();
        map.put("token",testUser.getToken());

        User authenticatedUser = userService.checkAuth(map);

        assertEquals(authenticatedUser.getId(), createdUser.getId());
        assertEquals(authenticatedUser.getUsername(), createdUser.getUsername());
        assertEquals(authenticatedUser.getInLobby(), createdUser.getInLobby());
        assertEquals(authenticatedUser.getToken(), createdUser.getToken());
        assertEquals(authenticatedUser.getPassword(), createdUser.getPassword());
        assertNotNull(authenticatedUser.getToken());

    }

    @Test
    public void getUserByToken_success() {
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        //Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotFoundException.class, () -> userService.getUserByToken(createdUser.getToken()));

    }
    @Test
    public void getUserByToken_failure() {
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        //Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotFoundException.class, () -> userService.getUserByToken("invalid Token"));

    }

    @Test
    public void getUserById_success() {
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        //Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        //Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotFoundException.class, () -> userService.getUserByUserId(createdUser.getId()));

    }
    @Test
    public void getUserById_failure() {
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        //Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        //Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotFoundException.class, () -> userService.getUserByUserId(3000L));

    }

    @Test
    public void login_passwordMatch_failure(){
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        User changedPassWordUser = new User();
        changedPassWordUser.setUsername(testUser.getUsername());
        changedPassWordUser.setPassword("Fake Non Saved Password");

        //when
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(createdUser);
        System.out.println(createdUser.getPassword());


        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotCreatorException.class, () -> userService.login(changedPassWordUser));
    }

    @Test
    public void login_success(){
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);
        String oldToken = createdUser.getToken();
        System.out.println(oldToken);
        //when
        //Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        //Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        // then -> attempt to create second user with same user -> check that an error is thrown
        User authenticatedUser = userService.login(createdUser);

        assertEquals(authenticatedUser.getId(), createdUser.getId());
        assertEquals(authenticatedUser.getUsername(), createdUser.getUsername());
        assertEquals(authenticatedUser.getInLobby(), createdUser.getInLobby());
        assertEquals(authenticatedUser.getPassword(), createdUser.getPassword());
        assertNotNull(authenticatedUser.getToken());
        System.out.println(authenticatedUser.getToken());
        System.out.println(createdUser.getToken());
        assertFalse(authenticatedUser.getToken().equals(oldToken));


    }


    @Test
    public void logout_noUser_fail(){
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        User changedPassWordUser = new User();
        changedPassWordUser.setUsername("non existing user");

        //when
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(createdUser);


        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NullPointerException.class, () -> userService.logOut(changedPassWordUser));
    }

    @Test
    public void logout_noToken_fail(){
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);
        createdUser.setToken(null);


        //when
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(createdUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotCreatorException.class, () -> userService.logOut(createdUser));
    }

    @Test
    public void logout_nonMatchingTokens_fail(){
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        User changedTokenUser = new User();
        changedTokenUser.setToken("non matching token");
        //when
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(createdUser);

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotCreatorException.class, () -> userService.logOut(changedTokenUser));
    }


    @Test
    public void updateUser_noAuth_failure(){
        // given -> a first user has already been created
        User createdUser = userService.createUser(testUser);

        User changedTokenUser = new User();
        changedTokenUser.setUsername(createdUser.getUsername());
        changedTokenUser.setPassword(createdUser.getPassword());
        changedTokenUser.setId(createdUser.getId());
        changedTokenUser.setToken("non matching token");
        //when
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(createdUser));

        // then -> attempt to create second user with same user -> check that an error is thrown
        assertThrows(NotCreatorException.class, () -> userService.updateUser(changedTokenUser.getId(),changedTokenUser));
    }

    @Test
    public void updateUser_partialUpdate_success(){
        // given -> a first user has already been created
        String oldUsername = testUser.getUsername();
        User createdUser = userService.createUser(testUser);

        User updatedUser = new User();
        updatedUser.setUsername("new name");
        updatedUser.setPassword(createdUser.getPassword());
        updatedUser.setId(createdUser.getId());
        updatedUser.setToken(createdUser.getToken());
        //when
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(createdUser));



        // then -> attempt to create second user with same user -> check that an error is thrown
        User afterUpdateUser = userService.updateUser(updatedUser.getId(), updatedUser);
        assertEquals(afterUpdateUser.getId(), createdUser.getId());
        assertFalse(afterUpdateUser.getUsername().equals(oldUsername));
        assertEquals(afterUpdateUser.getInLobby(), createdUser.getInLobby());
        assertEquals(afterUpdateUser.getPassword(), createdUser.getPassword());
        assertNotNull(afterUpdateUser.getToken());
    }

    @Test
    public void updateUser_fullUpdate_success(){
        // given -> a first user has already been created
        String oldUsername = testUser.getUsername();
        String oldPassword = testUser.getPassword();
        User createdUser = userService.createUser(testUser);

        User updatedUser = new User();
        updatedUser.setUsername("new name");
        updatedUser.setPassword("new password");
        updatedUser.setId(createdUser.getId());
        updatedUser.setToken(createdUser.getToken());
        //when
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(createdUser));



        // then -> attempt to create second user with same user -> check that an error is thrown
        User afterUpdateUser = userService.updateUser(updatedUser.getId(), updatedUser);
        assertEquals(afterUpdateUser.getId(), createdUser.getId());
        assertFalse(afterUpdateUser.getUsername().equals(oldUsername));
        assertEquals(afterUpdateUser.getInLobby(), createdUser.getInLobby());
        assertFalse(afterUpdateUser.getPassword().equals(oldPassword));
        assertNotNull(afterUpdateUser.getToken());
    }


}
