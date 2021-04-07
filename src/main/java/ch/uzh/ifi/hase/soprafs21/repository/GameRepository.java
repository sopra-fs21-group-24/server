package ch.uzh.ifi.hase.soprafs21.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.uzh.ifi.hase.soprafs21.entity.User;

@Repository("gameRepository")
public interface GameRepository extends JpaRepository<User, Long> {
}
