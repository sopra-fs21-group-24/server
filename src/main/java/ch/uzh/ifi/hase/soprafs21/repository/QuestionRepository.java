package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.Question;

    @Repository("questionRepository")
    public interface QuestionRepository extends JpaRepository<Question, Long> {
        Question findByQuestionId(Long questionId);
        List<Question> findByCountry(String country);
        List<Question> findByCountryIn(Collection<String> countries);
        List<Question> findAll();
        long count();
    }

