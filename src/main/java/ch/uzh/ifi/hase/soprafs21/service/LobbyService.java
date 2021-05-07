package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import ch.uzh.ifi.hase.soprafs21.entity.Lobby;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs21.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.LobbyGetDTOAllLobbies;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private Queue<DeferredResult<List<LobbyGetDTOAllLobbies>>> allLobbiesRequests = new ConcurrentLinkedQueue<>();

    private final LobbyRepository lobbyRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository, UserRepository userRepository, UserService userService) {
        this.lobbyRepository = lobbyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public User checkAuth(Map<String, String> header){
        try {
            String token = header.get("token");
            return userService.getUserByToken(token);
        }
        catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Lobby createLobby(Lobby newlobby){

        Optional<User> foundCreator = userRepository.findById(newlobby.getCreator());
        if(foundCreator.isEmpty()){
            throw new NotFoundException("No user found for lobby creator");
        }

        User creator = foundCreator.get();

        if(creator.getInLobby().booleanValue()){
            throw new PreconditionFailedException("User is already in another Lobby");
        }

        newlobby.addUser(creator.getId());
        newlobby.setRoomKey(generateRoomKey(newlobby));
        creator.setInLobby(true);

        userRepository.saveAndFlush(creator);
        lobbyRepository.saveAndFlush(newlobby);
        log.debug("Created Information for Lobby: {}", newlobby);

        handleLobbies();
        return newlobby;
    }

    public Lobby getLobbyById(Long lobbyid) {
        Optional<Lobby> found = lobbyRepository.findById(lobbyid);
        if(found.isEmpty()){
            throw new NotFoundException("Lobby with this lobbyid: " + lobbyid + " not found");
        }
        return found.get();
    }

    public Lobby getLobbyByRoomkey(Long roomkey) {
        Optional<Lobby> found = lobbyRepository.findByRoomKey(roomkey);
        if(found.isEmpty()){
            throw new NotFoundException("Lobby with this roomkey: " + roomkey + " not found");
        }
        return found.get();
    }

    public void addUserToExistingLobby(User user, Lobby lobby){
        if(user.getInLobby().booleanValue()){
            throw new NotFoundException("User is already in Lobby");
        }
        if (lobby == null){throw new NotFoundException("Lobby does not exist");}
        if (lobby.getUsers().size() < 3){
            lobby.addUser(user.getId());
            user.setInLobby(true);

            handleLobbies();

            userRepository.saveAndFlush(user);
            lobbyRepository.saveAndFlush(lobby);
        }
        else {
            throw  new PreconditionFailedException("To many users in the lobby!"); 
        }
    }

    public Long generateRoomKey(Lobby lobby){
        return 3000 - lobby.getCreator();
    }



    public List<Lobby> getAllLobbies(){
        return lobbyRepository.findAllByPublicStatusTrue();
    }

    public void userExitLobby(User user, Long lobbyId){
        Long userId = user.getId();

        if (!user.getInLobby().booleanValue()){
            throw new PreconditionFailedException("User is not in a lobby!"); 
        }
        
        Lobby lobby = getLobbyById(lobbyId);
        List<Long> lobbyUsers = lobby.getUsers();

        if (!lobbyUsers.contains(userId)){
            throw new PreconditionFailedException("User is not in this lobby!");
        }

        lobbyUsers.remove(userId);
        lobby.setUsers(lobbyUsers);

        user.setInLobby(false);

        handleLobbies();

        userRepository.flush();
        lobbyRepository.flush();
    }

    public void deleteLobby(Long lobbyId){
        Lobby lobby = getLobbyById(lobbyId);
        for (Long userId : lobby.getUsers()){
            userService.getUserByUserId(userId).setInLobby(false);
        }
        lobbyRepository.delete(lobby);
        lobbyRepository.flush();
    }

    // ------------- Lobby long polling --------------- // 

    public void handleLobbies(){
        List<LobbyGetDTOAllLobbies> finalLobbyList = getLobbyGetDTOAllLobbies();

        for (DeferredResult<List<LobbyGetDTOAllLobbies>> subscriber : allLobbiesRequests){
            subscriber.setResult(finalLobbyList);
        }
    }

    public List<LobbyGetDTOAllLobbies> getLobbyGetDTOAllLobbies() {
        List<LobbyGetDTOAllLobbies> finalLobbyList = new ArrayList<>();
        for (Lobby lobby : getAllLobbies()) {
            LobbyGetDTOAllLobbies lobbyGetDTOAllLobbies = DTOMapper.INSTANCE.convertEntityToLobbyGetDTOAllLobbies(lobby);
            lobbyGetDTOAllLobbies.setUsers(lobby.getUsers().size());
            lobbyGetDTOAllLobbies.setUsername(userService.getUserByUserId(lobby.getCreator()).getUsername());
            finalLobbyList.add(lobbyGetDTOAllLobbies);
        }
        return finalLobbyList;
    }

    public void removeRequestFromQueueLobbies(DeferredResult<List<LobbyGetDTOAllLobbies>> request){
        allLobbiesRequests.remove(request);
    }

    public void addRequestToQueueLobbies(DeferredResult<List<LobbyGetDTOAllLobbies>> request){
        log.info("Test: {}", allLobbiesRequests);
        allLobbiesRequests.add(request);
    }

    public boolean existRequestAllLobbies(DeferredResult<List<LobbyGetDTOAllLobbies>> request){
        return allLobbiesRequests.contains(request);
    }


}
