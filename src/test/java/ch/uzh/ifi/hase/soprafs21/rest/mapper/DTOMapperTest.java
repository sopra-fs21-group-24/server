package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.*;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Pixelation;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.MultiPlayer;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
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
    @Test
    public void testGetUser_convertLobbyPostDTOtoEntity_success() {


        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setCreator("1");
        lobbyPostDTO.setIsPublic(false);



        Lobby lobby = DTOMapper.INSTANCE.convertLobbyPostDTOtoEntity(lobbyPostDTO);

        // check content
        assertEquals(lobbyPostDTO.getIsPublic(), lobby.getPublicStatus());
    }


    @Test
    public void testGetUser_convertEntityToLobbyGetDTO_success() {


        Lobby lobby = new Lobby();
        lobby.setCreator(1L);
        lobby.setGameId(2L);


        LobbyGetDTO lobbyGetDTO = DTOMapper.INSTANCE.convertEntityToLobbyGetDTO(lobby);

        // check content
        assertEquals(lobby.getCreator(), lobbyGetDTO.getCreator());
    }

    @Test
    public void testGetUser_convertEntityToLobbyGetDTOAllLobbies_success() {


        Lobby lobby = new Lobby();
        lobby.setCreator(1L);
        lobby.setPublicStatus(true);
        lobby.setGameId(2L);

        LobbyGetDTOAllLobbies lobbyGetDTOAllLobbies = DTOMapper.INSTANCE.convertEntityToLobbyGetDTOAllLobbies(lobby);

        // check content
        assertEquals(lobby.getId(), lobbyGetDTOAllLobbies.getId());
        assertEquals(lobby.getPublicStatus(), lobbyGetDTOAllLobbies.getPublicStatus());
    }

    @Test
    public void testGetUser_convertEntityToLeaderboardGetDTO_success() {


        Leaderboard leaderboard = new Leaderboard();

        leaderboard.setScore(12);
        leaderboard.setGameMode(gameModeEnum.CLOUDS);
        leaderboard.setUsername("abc");
        leaderboard.setId(2L);

        LeaderboardGetDTO leaderboardGetDTO = DTOMapper.INSTANCE.convertEntityToLeaderboardGetDTO(leaderboard);

        // check content
        assertEquals(leaderboard.getScore(), leaderboardGetDTO.getScore());
        assertEquals(leaderboard.getGameMode(), leaderboardGetDTO.getGameMode());

    }
    @Test
    public void testGetUser_convertGamePostDTOCreateToGameEntity_success() {


        GamePostDTOCreate gamePostDTOCreate = new GamePostDTOCreate();
        gamePostDTOCreate.setUserId(2L);

        GameEntity gameEntity = DTOMapper.INSTANCE.convertGamePostDTOCreateToGameEntity(gamePostDTOCreate);

        // check content
        assertEquals(gameEntity.getCreatorUserId(), gamePostDTOCreate.getUserId());


    }

    @Test
    public void convertGamePutDTOToGameEntity_success() {

        GamePutDTO gamePutDTO = new GamePutDTO();
        gamePutDTO.setUserId(2L);


        GameEntity gameEntity = DTOMapper.INSTANCE.convertGamePutDTOToGameEntity(gamePutDTO);

        // check content
        assertEquals(gameEntity.getCreatorUserId(), gamePutDTO.getUserId());


    }


    @Test
    public void convertGameEntityToGameGetDTO_success() {

        GameEntity gameEntity = new GameEntity();
        gameEntity.setCreatorUserId(2L);
        gameEntity.setGameId(1L);
        gameEntity.setGameMode(new Pixelation());
        gameEntity.setLobbyId(3L);
        gameEntity.setRound(1);
        gameEntity.setBreakDuration(4);
        gameEntity.setGameModeFromName("Hallo");
        gameEntity.setRoundDuration(12);
        gameEntity.setRoundStart(5L);
        gameEntity.setUserMode(new MultiPlayer());


        GameGetDTO gameGetDTO = DTOMapper.INSTANCE.convertGameEntityToGameGetDTO(gameEntity);

        // check content
        assertEquals(gameEntity.getCreatorUserId(), gameGetDTO.getCreatorId());
        assertEquals(gameEntity.getGameId(), gameGetDTO.getGameId());
        assertEquals(gameEntity.getGameMode(), gameGetDTO.getGameMode());
        assertEquals(gameEntity.getRound(), gameGetDTO.getRound());
        assertEquals(gameEntity.getLobbyId(), gameGetDTO.getLobbyId());
        assertEquals(gameEntity.getUserMode(), gameGetDTO.getUserMode());


    }

    @Test
    public void convertAnwserPostDTOtoAnswer_success() {

        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setCoordGuess(new Coordinate(12.12,12.12));
        answerPostDTO.setDifficultyFactor(12);
        answerPostDTO.setQuestionId(1L);


        Answer answer = DTOMapper.INSTANCE.convertAnwserPostDTOtoAnswer(answerPostDTO);

        // check content
        assertEquals(answer.getQuestionId(), answerPostDTO.getQuestionId());
        assertEquals(answer.getCoordGuess(), answerPostDTO.getCoordGuess());
        assertEquals(answer.getDifficultyFactor(), answerPostDTO.getDifficultyFactor());



    }


}
