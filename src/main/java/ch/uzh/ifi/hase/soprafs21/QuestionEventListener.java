package ch.uzh.ifi.hase.soprafs21;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;

@Component
public class QuestionEventListener{
    private final QuestionRepository questionRepository;
    
    Logger logger = LoggerFactory.getLogger(QuestionEventListener.class);

    @Autowired
    public QuestionEventListener(QuestionRepository questionRepository){
        this.questionRepository = questionRepository;
    }
    // test
    @EventListener(ApplicationReadyEvent.class)
    public void handleContextStart() {
        logger.info("Application REAAADyyyyy1 event!!!");

        Coordinate coordinate = new Coordinate(12.0, 12.0);

        Question question = new Question();
        question.setQuestionId(1000L);
        question.setZoomLevel(1);
        question.setCoordinate(coordinate);

        questionRepository.saveAndFlush(question);
    }
} 