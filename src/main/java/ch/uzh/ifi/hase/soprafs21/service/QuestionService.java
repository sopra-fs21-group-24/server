package ch.uzh.ifi.hase.soprafs21.service;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(@Qualifier("questionRepository") QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;

    }

    public String getMapImage(int height, int width, Long questionId) throws MalformedURLException {
        Question questionn = new Question();
        questionId = 1L;
        questionn.setCoordinate(new Coordinate( -73.935242, 40.730610));

        questionn.setZoomLevel(15);

        questionn.setQuestionId(1L);
        questionRepository.save(questionn);
        questionRepository.flush();
        Question question = questionRepository.findByQuestionId(questionId);


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

