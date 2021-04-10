package ch.uzh.ifi.hase.soprafs21.rest.mapper;

import ch.uzh.ifi.hase.soprafs21.entity.Leaderboard;
import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import javax.persistence.Column;
import javax.persistence.OrderBy;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g., UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    //@Mapping(source = "logged_in", target = "logged_in")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "username", target = "username")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "creater", target = "creater")
    Lobby convertLobbyPostDTOtoEntity(LobbyPostDTO lobbyPostDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "creater", target = "creater")
    LobbyGetDTO convertEntityToLobbyGetDTO(Lobby lobby);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "gameModeId", target = "gameModeId")
    LeaderboardGetDTO convertEntityToLeaderboardGetDTO(Leaderboard leaderboard);
}
