package ch.uzh.ifi.hase.soprafs21.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Question;

    @Repository("questionRepository")
    public interface QuestionRepository extends JpaRepository<Question, Long> {
        Question findByQuestionId(Long questionId);

    }

