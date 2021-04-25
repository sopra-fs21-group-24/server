package ch.uzh.ifi.hase.soprafs21.repository;

import java.util.Optional;

import ch.uzh.ifi.hase.soprafs21.rest.dto.UserGetDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
    User findByUsername(String username);

    @Query("select u.id from User u")
    Long findByIdGetOnlyId(Long id);

    Optional<User> findByToken(String token);
}
