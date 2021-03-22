package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.HashMap;

public class Leaderboard {
    public HashMap<String,Score[]> scores;

    public void addScoreEntry(Score newScore){
        //this.scores.put(newScore.gameModeId, newScore);
    }

    public Score[] getScoresForGameMode(String gameModeId){
        return this.scores.get(gameModeId);
    }

}

