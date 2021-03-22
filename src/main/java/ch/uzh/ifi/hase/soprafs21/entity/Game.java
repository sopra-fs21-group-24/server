package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.Map;
import java.util.Queue;

public class Game {

    public Game(){
        // Assign 5 random questions to Question[]
    }
    public GameMode gameMode;
    public UserMode userMode;
    public int Id;
    public int roomKey;
    public GameStatus status;
    public Map<String, Integer> userScores;
    public Question[] questions;



    public void calculateUserScore(float userX, float userY, int questionId){
        //this.gameMode.calculateScore()
    }


}


