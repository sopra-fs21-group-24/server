package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTOWithoutToken;
import ch.uzh.ifi.hase.soprafs21.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation works.
 */
public class DTOMapperTest {

    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {

        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");
        userPostDTO.setPassword("123");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
        assertEquals(userPostDTO.getPassword(), user.getPassword());

    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {

        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());

    }
    @Test
    public void testGetUser_convertEntityToUserGetDTOWithoutToken_success() {

        // create User
        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("1");
        user.setId(2L);
        HashMap highscore = new HashMap();
        highscore.put("Time", 3);
        user.setHighScores(highscore);
        // MAP -> Create user
        UserGetDTOWithoutToken UserGetDTOWithoutToken = DTOMapper.INSTANCE.convertEntityToUserGetDTOWithoutToken(user);

        // check content
        assertEquals(UserGetDTOWithoutToken.getUsername(), user.getUsername());
        assertEquals(UserGetDTOWithoutToken.getId(), user.getId());
        assertEquals(UserGetDTOWithoutToken.getHighscores(), user.getHighScores());
    }


}
