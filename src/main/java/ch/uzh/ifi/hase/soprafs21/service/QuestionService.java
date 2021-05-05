package ch.uzh.ifi.hase.soprafs21.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import ch.uzh.ifi.hase.soprafs21.exceptions.PreconditionFailedException;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;

@Service
@Transactional
public class QuestionService {


    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;

    }

    public void checkQuestionIdInQuestions(List<Long> questions, Long questionId) {
        if (!questions.contains(questionId)) {
            throw new PreconditionFailedException("Question with this id is not part of the game");
        }
    }


    public String getMapImage(int height, int width, Question question) throws MalformedURLException {
        try {
            String apikey = "AIzaSyCbLudPiesxon89uVFg9qloApgl_8BXviY";
            String url = "https://maps.googleapis.com/maps/api/staticmap?center="+question.getCoordinate().getLat()+","+question.getCoordinate().getLon()+"&zoom="+ question.getZoomLevel() +
                    "&size="+ height +"x"+ width +"&scale=2&maptype=satellite&key="+apikey;

            URL mapUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) mapUrl.openConnection();

            InputStream responseStream = httpURLConnection.getInputStream();

            byte[] fileContent = FileUtil.readAsByteArray(responseStream);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            responseStream.close();
            httpURLConnection.disconnect();
            return encodedString;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
}
}

