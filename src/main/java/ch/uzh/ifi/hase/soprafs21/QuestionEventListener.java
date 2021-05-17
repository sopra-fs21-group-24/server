package ch.uzh.ifi.hase.soprafs21;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import ch.uzh.ifi.hase.soprafs21.repository.QuestionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.*;

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

        String filename = "data/data.csv";

        try (Reader reader =  new BufferedReader(new InputStreamReader(new FileInputStream(filename),"utf-8"))) {

            // read csv file
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
            .withHeader("Country","City", "Lat", "Lng", "Zoom").parse(reader);
            for (CSVRecord record : records) {
                System.out.println(record);
                double lon = Double.parseDouble(record.get("Lng"));
                double lat = Double.parseDouble(record.get("Lat"));
                String country = record.get("Country"); // Switzerland | Europe | Worldwide
                // evtl. add zoom reading from file

                Coordinate coordinate = new Coordinate(lon, lat);

                Question question = new Question();
                question.setZoomLevel(12);
                question.setCountry(country);
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