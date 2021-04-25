package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;

import java.util.List;

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
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "public", target = "public")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "creator", target = "creator")
    @Mapping(source = "users", target = "users")
    @Mapping(source = "public", target = "public")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "score", target = "score")
    LeaderboardGetDTO convertEntityToLeaderboardGetDTO(Leaderboard leaderboard);


    @Mapping(source = "userId", target = "creatorUserId")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "userMode", target = "userMode")
    @Mapping(source = "publicStatus", target = "publicStatus")
    GameEntity convertGamePostDTOCreateToGameEntity(GamePostDTOCreate gamePostDTOCreate);

    @Mapping(source = "gameId", target = "gameId")
    @Mapping(source = "creatorUserId", target = "creatorId")
    @Mapping(source = "round", target = "round")
    @Mapping(source = "userMode", target = "userMode")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "userIds", target = "players")
    GameGetDTO convertGameEntityToGameGetDTO(GameEntity gameEntity);
    
    
}
