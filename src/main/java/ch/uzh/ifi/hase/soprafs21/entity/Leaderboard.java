package ch.uzh.ifi.hase.soprafs21.entity;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.HashMap;

import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;
import org.yaml.snakeyaml.util.ArrayUtils;

import javax.persistence.*;

@Entity
@Table(name = "Leaderboard")
public class Leaderboard implements Observer{
    

    @Id
    @GeneratedValue
    private Long id; //same as gamemodeid?

    @Transient
    private HashMap<Integer,Score[]> scores;

    public void addScoreEntry(Score newScore){
        Score[] s =  this.scores.get(newScore.gameModeId);
        s= Arrays.copyOf(s, s.length + 1);
        s[s.length - 1] = newScore;
       //this.scores.put(newScore.gameModeId, ArrayUtils.add(scores.get(newScore.gameModeId),newScore));
    }

    public Score[] getScoresForGameMode(int gameModeId){
        return this.scores.get(gameModeId);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }

    public void setId(Long id) {this.id = id;}

    public Long getId() {return id;   }
}

