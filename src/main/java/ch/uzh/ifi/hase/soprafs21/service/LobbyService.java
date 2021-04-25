package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PerformingUnauthenticatedAction;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

import javax.swing.text.html.parser.Entity;

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

        List<User> users = new ArrayList<>();
        Optional<User> creator = userRepository.findById(newlobby.getCreator());
        if(creator.isEmpty()){
            throw new NotFoundException("No user found for lobby creator");
        }
        users.add(creator.get());
        newlobby.setUsers(users);
        newlobby.setRoomKey(generateRoomKey(newlobby));
        lobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newlobby);
        return newlobby;

    }

    public Lobby getLobbyWithId(Long lobbyid) {
        if (lobbyRepository.findByid(lobbyid) == null){throw new NotFoundException("No lobby found with id:"+lobbyid);}
        return lobbyRepository.findByid(lobbyid);

    }

    public void addUserToExistingLobby(User userToAdd, Lobby lobbyAddTo){
        if (lobbyAddTo.getUsers().size() < 3){
            List<User> users = lobbyAddTo.getUsers();
            users.add(userToAdd);
            lobbyAddTo.setUsers(users);
            lobbyRepository.save(lobbyAddTo);
            lobbyRepository.flush();
        }
        else {
            throw  new PerformingUnauthenticatedAction("To many users in the lobby!"); 
        }
    }

    public Long generateRoomKey(Lobby lobby){
        return 30000 - lobby.getId();
    }



    public List getAllLobbies(){
        return lobbyRepository.findAllByIsPublicTrue();
    }
}
