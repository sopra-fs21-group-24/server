package ch.uzh.ifi.hase.soprafs21.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ch.uzh.ifi.hase.soprafs21.entity.Game;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.repository.GameRepository;

@Service
@Transactional
public class GameService {

    // private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final GameRepository gameRepository;
    
    @Autowired
    public GameService(@Qualifier("gameRepository") GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    // evtl ändern für lobbies
    public List<Game> allGames(){
        throw new UnsupportedOperationException();
        // return new ArrayList<>();
    }

    public Game gameById(){
        throw new UnsupportedOperationException();
    }

    public Game createGame(){
        throw new UnsupportedOperationException();
    }
    public Game exitGame(){
        throw new UnsupportedOperationException();
    }
    public Question allQuestions(){
        throw new UnsupportedOperationException();
    }

    public Question questionById(){
        throw new UnsupportedOperationException();
    }

    public Long makeGuess(){
        throw new UnsupportedOperationException();
    }

}
