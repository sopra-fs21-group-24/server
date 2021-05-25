package ch.uzh.ifi.hase.soprafs21.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.GameEntity;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.exceptions.NotFoundException;
import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {


    private final QuestionRepository questionRepository;

    Logger logger = LoggerFactory.getLogger(QuestionService.class);

    @Autowired
    public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;

    }

    public Question questionById(Long questionId) {
        Optional<Question> found = questionRepository.findById(questionId);
        if (found.isEmpty()) {
            throw new NotFoundException("Question with this questionId is not found");
        }
        else {
            return found.get();
        }
    }


    public void checkQuestionIdInQuestions(List<Long> questions, Long questionId) {
        if (!questions.contains(questionId)) {
            throw new PreconditionFailedException("Question with this id is not part of the game");
        }
    }

    // debug
    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

    public Coordinate getRoundQuestionSolution(GameEntity game){
        List<Long> questions = game.getQuestions();
        if (game.getRound() == 0){
            throw new PreconditionFailedException("Solution can't be fetched round hasn't started yet");
        }

        if (game.getRound() < 4) {
            return questionById(questions.get(game.getRound() - 1)).getCoordinate();
        }

        // so that after round 3, round 3 can get fetched (endscreen)
        return questionById(questions.get(2)).getCoordinate();
    }

    public String getMapImage(int height, int width, Question question){

        Long startTime = System.currentTimeMillis();

        try {
            Coordinate coord = question.getCoordinate();

            String url = "https://maps.googleapis.com/maps/api/staticmap?center="+coord.getLat()+","+coord.getLon()+"&zoom="+ question.getZoomLevel() +
                    "&size="+ String.valueOf(width) +"x"+ String.valueOf(height) +"&scale=2&maptype=satellite&key="+System.getenv("apikey"); //see on heroku if this works

            URL mapUrl = new URL(url);
            logger.info("Url: {}", url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) mapUrl.openConnection();
            httpURLConnection.setRequestMethod("GET");

            InputStream responseStream = httpURLConnection.getInputStream();
            byte[] fileContent = FileUtil.readAsByteArray(responseStream);

            responseStream.close();
            httpURLConnection.disconnect();

            Long endTime = System.currentTimeMillis();
            logger.info("(questionId = {}) Request took: {} secs", question.getQuestionId(), (endTime - startTime)/1000.0);

            return Base64.getEncoder().encodeToString(fileContent);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public long count() {
        return questionRepository.count();
    }
}

