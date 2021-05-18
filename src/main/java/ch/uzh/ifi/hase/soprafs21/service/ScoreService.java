package ch.uzh.ifi.hase.soprafs21.service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Score;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.ScoreRepository;
import ch.uzh.ifi.hase.soprafs21.rest.dto.ScoreGetDTO;
import ch.uzh.ifi.hase.soprafs21.rest.mapper.DTOMapper;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back to the caller.
 */
@Service
@Transactional
public class ScoreService {

    private final ScoreRepository scoreRepository;
    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    public ScoreService(@Qualifier("scoreRepository") ScoreRepository scoreRepository, QuestionService questionService, UserService userService){
        this.scoreRepository = scoreRepository;
        this.questionService = questionService;
        this.userService = userService;
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

    public ListIterator<Score> scoresByGame(GameEntity game) {
        ArrayList<Score> scores = new ArrayList<>();
        for (Long userId : game.getUserIds()) {
            scores.add(findById(userId));
        }
        return scores.listIterator();
    }


    public List<ScoreGetDTO> getScoreGetDTOs(GameEntity game){

        List<ScoreGetDTO> scoresDTO = new ArrayList<>();

        Coordinate solution = questionService.getRoundQuestionSolution(game);

        // one to one: user - score?
        for (ListIterator<Score> scores = scoresByGame(game);scores.hasNext();) {
            ScoreGetDTO scoreGetDTO = DTOMapper.INSTANCE.convertScoreEntityToScoreGetDTO(scores.next());
            User scoreUser = userService.getUserByUserId(scoreGetDTO.getUserId());
            scoreGetDTO.setSolutionCoordinate(solution);
            scoreGetDTO.setUsername(scoreUser.getUsername());
            scoresDTO.add(scoreGetDTO);
        }

        return scoresDTO;
    }
}
