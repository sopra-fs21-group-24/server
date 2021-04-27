package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    // evt Login dto erstellen damit ein token returned wird

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "highPixel", target = "highPixel")
    @Mapping(source = "highClouds", target = "highClouds")
    @Mapping(source = "highTime", target = "highTime")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "highPixel", target = "highPixel")
    @Mapping(source = "highClouds", target = "highClouds")
    @Mapping(source = "highTime", target = "highTime")
    UserGetDTOWithoutToken convertEntityToUserGetDTOWithoutToken(User user);

    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "public", target = "public")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "public", target = "public")
    @Mapping(target = "users", ignore = true)
    @Mapping(source = "roomKey", target = "roomKey")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);


    @Mapping(source = "id", target = "id")
    @Mapping(target = "username", ignore = true)
    @Mapping(source = "public", target = "public")
    @Mapping(target = "users", ignore = true)
    LobbyGetDTOAllLobbies convertEntityToLobbyGetDTOAllLobbies(Lobby lobby);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "score", target = "score")
    LeaderboardGetDTO convertEntityToLeaderboardGetDTO(Leaderboard leaderboard);


    @Mapping(source = "userId", target = "creatorUserId")
    GameEntity convertGamePostDTOCreateToGameEntity(GamePostDTOCreate gamePostDTOCreate);

    @Mapping(source = "userId", target = "creatorUserId")
    GameEntity convertGamePutDTOToGameEntity(GamePutDTO gamePutDTO);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "creatorUserId", target = "creatorId")
    @Mapping(source = "round", target = "round")
    @Mapping(source = "userMode", target = "userMode")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "userIds", target = "players")
    @Mapping(source = "lobbyId", target = "lobbyId")
    GameGetDTO convertGameEntityToGameGetDTO(GameEntity gameEntity);



    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "tempScore", target = "tempScore")
    @Mapping(source = "totalScore", target = "totalScore")
    @Mapping(source = "lastCoordinate", target = "lastCoordinate")
    ScoreGetDTO convertScoreEntityToScoreGetDTO(Score score);



    
}
