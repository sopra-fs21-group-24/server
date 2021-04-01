package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.HashMap;

import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;

public class Leaderboard implements Observer{
    
    public HashMap<String ,Score[]> scores;

    public void addScoreEntry(Score newScore){
       //this.scores.put(newScore.gameModeId, newScore);
       ;
    }

    public Score[] getScoresForGameMode(String gameModeId){
        return this.scores.get(gameModeId);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }

}

