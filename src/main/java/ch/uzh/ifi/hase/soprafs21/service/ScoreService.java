package ch.uzh.ifi.hase.soprafs21.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.ScoreRepository;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class ScoreService {

    private final ScoreRepository scoreRepository;

    @Autowired
    public ScoreService(@Qualifier("scoreRepository") ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    public void save(Score score){
        try {
            scoreRepository.save(score);
            scoreRepository.flush();
        }
        catch (IllegalArgumentException e){
            throw new PreconditionFailedException("[ScoreService] There is already a Score in the database");
        }
    }

    public void delete(Score score){
        try {
            scoreRepository.delete(score);
            scoreRepository.flush();
        }
        catch (IllegalArgumentException e){
            throw new PreconditionFailedException("[ScoreService] The given score is null");
        }
    }

    public Score findById(Long userId){
        Optional<Score> foundScore = scoreRepository.findById(userId);
        if (foundScore.isEmpty()){
            throw new NotFoundException("Score for player could not be found");
        } else {
            return foundScore.get();
        }
    }

}
