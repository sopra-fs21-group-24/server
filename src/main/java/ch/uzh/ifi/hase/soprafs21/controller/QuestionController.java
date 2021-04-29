package ch.uzh.ifi.hase.soprafs21.controller;
import java.net.MalformedURLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.uzh.ifi.hase.soprafs21.rest.dto.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs21.service.QuestionService;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/question/{ID}")
    @ResponseStatus(HttpStatus.OK)
    public String getUserById(@PathVariable("ID") Long questionId, @RequestBody QuestionGetDTO questionGetDTO) throws MalformedURLException {

       return "Please use other endpoint";

    }
}
