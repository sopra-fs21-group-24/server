package ch.uzh.ifi.hase.soprafs21.service;

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
import ch.uzh.ifi.hase.soprafs21.exceptions.NotCreatorException;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;

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

        Optional<User> foundCreator = userRepository.findById(newlobby.getCreator());
        if(foundCreator.isEmpty()){
            throw new NotFoundException("No user found for lobby creator");
        }

        User creator = foundCreator.get();

        if(creator.getInLobby()){
            throw new PreconditionFailedException("User is already in another Lobby");
        }

        newlobby.addUser(creator.getId());
        newlobby.setRoomKey(generateRoomKey(newlobby));
        creator.setInLobby(true);
        userRepository.save(creator);
        lobbyRepository.save(newlobby);
        lobbyRepository.flush();
        userRepository.flush();
        log.debug("Created Information for Lobby: {}", newlobby);
        return newlobby;
    }

    public Lobby getLobbyWithId(Long lobbyid) {
        if (lobbyRepository.findByid(lobbyid) == null){throw new NotFoundException("No lobby found with id:"+lobbyid);}
        return lobbyRepository.findByid(lobbyid);

    }

    public void addUserToExistingLobby(User userToAdd, Lobby lobbyAddTo){
        if(userToAdd.getInLobby()){throw new NotFoundException("User is already in Lobby");}
        if (lobbyAddTo.getUsers().size() < 3){
            List<Long> users = lobbyAddTo.getUsers();
            users.add(userToAdd.getId());
            userToAdd.setInLobby(true);
            userRepository.save(userToAdd);
            lobbyAddTo.setUsers(users);
            lobbyRepository.save(lobbyAddTo);
            lobbyRepository.flush();
            userRepository.flush();
        }
        else {
            throw  new NotCreatorException("To many users in the lobby!"); 
        }
    }

    public Long generateRoomKey(Lobby lobby){
        return 3000 - lobby.getCreator();
    }



    public List<Lobby> getAllLobbies(){
        if (lobbyRepository.findAllByIsPublicTrue().isEmpty()){throw new NotFoundException("No open lobbies");}
        return lobbyRepository.findAllByIsPublicTrue();
    }

    public Lobby getLobbyWithRoomKey(Long roomKey){
        if (lobbyRepository.findByRoomKey(roomKey) == null){throw new NotFoundException("Lobby does not exist!"); }
        return lobbyRepository.findByRoomKey(roomKey);

    }

    public void UserExitLobby(Long userId, Long lobbyId){
        if (lobbyRepository.findByid(lobbyId) == null){throw new NotFoundException("Lobby does not exist!");}
        if (!userRepository.findById(userId).get().getInLobby()){throw new NotFoundException("User is not in a lobby!"); }
        if (!lobbyRepository.findByid(lobbyId).getUsers().contains(userId)){throw new NotFoundException("User si not in this lobby!");}
        List<Long> newuserList = lobbyRepository.findByid(lobbyId).getUsers();
        newuserList.remove(userId);
        userRepository.findById(userId).get().setInLobby(false);
        userRepository.flush();
        lobbyRepository.findByid(lobbyId).setUsers(newuserList);
        lobbyRepository.flush();
    }

    public void deleteLobby(Long lobbyId){
        if (lobbyRepository.findByid(lobbyId) == null){throw new NotFoundException("Lobby does not exist!");}
        for (Long id : lobbyRepository.findByid(lobbyId).getUsers()){
            userRepository.findById(id).get().setInLobby(false);
        }
        lobbyRepository.delete(lobbyRepository.findByid(lobbyId));
        lobbyRepository.flush();
    }

}
