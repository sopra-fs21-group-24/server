package ch.uzh.ifi.hase.soprafs21;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
        logger.info("Reading Questions from csv and creating Entities...");

        String filename = "data/worldcities.csv";
        try (Reader reader = Files.newBufferedReader(Paths.get(filename))) {

            // read csv file
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
            .withHeader("city", "zoom", "lat", "lon", "id").parse(reader);
            for (CSVRecord record : records) {
                double lon = Double.parseDouble(record.get("lon"));
                double lat = Double.parseDouble(record.get("lat"));
                int zoom = Integer.parseInt(record.get("zoom"));

                Coordinate coordinate = new Coordinate(lon, lat);

                Question question = new Question();
                question.setZoomLevel(12);
                question.setCoordinate(coordinate);

                questionRepository.save(question);
            }

            questionRepository.flush();
            logger.info("Finished reading quesitons from file '{}'", filename);
        
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
} 