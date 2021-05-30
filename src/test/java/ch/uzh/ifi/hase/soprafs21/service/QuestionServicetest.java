package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.Clouds;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


public class QuestionServicetest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question testquestion;
    private Question testquestion2;
    private GameEntity gameEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testquestion = new Question();
        testquestion.setCoordinate(new Coordinate(3.0, 3.0));
        testquestion.setQuestionId(1L);
        testquestion.setZoomLevel(15);


        testquestion2 = new Question();
        testquestion2.setCoordinate(new Coordinate(4.0, 4.0));
        testquestion2.setQuestionId(2L);
        testquestion2.setZoomLevel(16);

        gameEntity = new GameEntity();
        gameEntity.setGameMode(new Clouds());
        gameEntity.setGameId(6L);
        gameEntity.setRound(2);
        List<Long> questions;
        questions = new ArrayList<>();
        questions.add(testquestion.getQuestionId());
        questions.add(testquestion2.getQuestionId());
        gameEntity.setQuestions(questions);

        // when -> any object is being save in the userRepository -> return the dummy testUser
        Mockito.when(questionRepository.save(Mockito.any())).thenReturn(testquestion);
        Mockito.when(questionRepository.findById(testquestion.getQuestionId())).thenReturn(Optional.ofNullable(testquestion));
        Mockito.when(questionRepository.findById(testquestion2.getQuestionId())).thenReturn(Optional.ofNullable(testquestion2));
    }

    @Test
    public void QuestionService_success() {

        Question foundQ = questionService.questionById(testquestion.getQuestionId());
        assertEquals(foundQ.getQuestionId(), testquestion.getQuestionId());
        assertEquals(foundQ.getCoordinate(), testquestion.getCoordinate());
        assertEquals(foundQ.getZoomLevel(), testquestion.getZoomLevel());

    }


    @Test
    public void QuestionService_fail() {

        assertThrows(NotFoundException.class, () -> {questionService.questionById(8L);});

    }

    @Test
    public void checkQuestionIdInQuestions_ThrowsError() {

        assertThrows(PreconditionFailedException.class, () -> {questionService.checkQuestionIdInQuestions(List.of(testquestion.getQuestionId()),8L);});


    }


    @Test
    public void getRoundQuestionSolution_success() {

        gameEntity.setRound(1);
        Coordinate c = questionService.getRoundQuestionSolution(gameEntity);
        assertEquals(c, testquestion.getCoordinate());

        gameEntity.setRound(2);
        Coordinate c2 = questionService.getRoundQuestionSolution(gameEntity);
        assertEquals(c2, testquestion2.getCoordinate());


    }
    @Test
    public void getRoundQuestionSolution_ThrowsErrorRoundDidNotStart() {

        gameEntity.setRound(0);
        assertThrows(PreconditionFailedException.class, () -> {questionService.getRoundQuestionSolution(gameEntity);});



    }


}
