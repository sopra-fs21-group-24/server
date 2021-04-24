package ch.uzh.ifi.hase.soprafs21.controller;
import ch.uzh.ifi.hase.soprafs21.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/question/{ID}")
    @ResponseStatus(HttpStatus.OK)
    public String getUserById(@PathVariable("ID") Long questionId, @RequestBody QuestionGetDTO questionGetDTO) throws MalformedURLException {

       return questionService.getMapImage(questionGetDTO.getHeight(),questionGetDTO.getWidth(),questionId);

    }
}
