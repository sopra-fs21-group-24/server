package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.PerformingUnauthenticatedAction;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserRepository userRepository) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
    }

    public Lobby createLobby(Lobby newlobby){

        newlobby = lobbyRepository.save(newlobby);
        List<User> users = newlobby.getUsers();
        User creator = userRepository.findByUsername(newlobby.getCreator());
        users.add(creator);
        newlobby.setUsers(users);
        lobbyRepository.flush();

        log.debug("Created Information for User: {}", newlobby);
        return newlobby;

    }
    public Lobby getLobbyWithId(Long lobbyid) {
        return lobbyRepository.findByid(lobbyid);

    }
    public void addUserToExistingLobby(User userToAdd, Lobby lobbyAddTo){
        if (lobbyAddTo.getUsers().size()<3){
        List<User> users = lobbyAddTo.getUsers();
        users.add(userToAdd);
        lobbyAddTo.setUsers(users);
        lobbyRepository.save(lobbyAddTo);
        lobbyRepository.flush();
}
        else {throw  new PerformingUnauthenticatedAction("To many users in the lobby!"); }
    }
}
