package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.entity.Coordinate;
import ch.uzh.ifi.hase.soprafs21.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class QuestionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void findQuestionById_success(){
        //given
        Question question = new Question();
        question.setCoordinate(new Coordinate(1.0,2.0));
        //question.setQuestionId(1L);
        question.setZoomLevel(12);
        entityManager.persist(question);
        entityManager.flush();

        // when
       Question found = questionRepository.findByQuestionId(question.getQuestionId());

        //then
        assertNotNull(found);
        Question foundQuestion = found;
        assertEquals(foundQuestion.getQuestionId(), question.getQuestionId());
        assertEquals(foundQuestion.getCoordinate(), question.getCoordinate());
        assertEquals(foundQuestion.getZoomLevel(), question.getZoomLevel());

    }


   /* @Test
    public void findByUserName_success() {
        // given
        User user = new User();

        user.setUsername("firstname@lastname");
        user.setPassword("mapassword");
        user.setToken("1");
        entityManager.persist(user);
        entityManager.flush();
        // when
        User found = userRepository.findByUsername(user.getUsername());
        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
    }

    @Test
    public void findByUserName_failure() {
        // given
        User user = new User();

        user.setUsername("firstname@lastname");
        user.setPassword("mapassword");
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();
        // when
        User found = userRepository.findByUsername("ThisUserNameDoesn'tExist");
        // then
        assertNull(found);
    }

    @Test
    public void findByToken_success() {
        // given
        User user = new User();

        user.setUsername("firstname@lastname");
        user.setPassword("mapassword");
        user.setToken("1");
        entityManager.persist(user);
        entityManager.flush();
        // when
        Optional<User> found = userRepository.findByToken(user.getToken());
        // then
        assertNotNull(found);
        User foundUser = found.get();
        assertEquals(foundUser.getUsername(), user.getUsername());
        assertEquals(foundUser.getToken(), user.getToken());
    }

    @Test
    public void findByToken_failure() {
        // given
        User user = new User();

        user.setUsername("firstname@lastname");
        user.setPassword("mapassword");
        user.setToken("1");
        entityManager.persist(user);
        entityManager.flush();
        // when
        Optional<User> found = userRepository.findByToken("Non existing Token");
        // then
        assertEquals(found.isEmpty(), true);
    }*/
}
