package ch.uzh.ifi.hase.soprafs21.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class ScoreRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ScoreRepository scoreRepository;

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
