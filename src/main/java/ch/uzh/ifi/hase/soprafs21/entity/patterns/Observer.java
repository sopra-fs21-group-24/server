package ch.uzh.ifi.hase.soprafs21.entity.patterns;

import ch.uzh.ifi.hase.soprafs21.entity.Score;

import java.util.ListIterator;

public interface Observer {
    void updateLeaderboard(String gameMode, ListIterator<Score> scores);
}
