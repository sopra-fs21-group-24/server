package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.HashMap;

import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;
import ch.uzh.ifi.hase.soprafs21.entity.patterns.SubjetObserver;

public class Game implements SubjetObserver{

    public Game(){
        // Assign 5 random questions to Question[]
    private final String gameId;
    private final String roomKey;
    private GameStatus status;
    private Question[] questions;
    private final UserMode userMode;
    private final GameMode gameMode;

    // brauchen wir das?
    private HashMap<String, Integer> userScores;
    }

    @Override
    public void registerObserver(Observer observer) {
        throw new UnsupportedOperationException();
        
    }

    @Override
    public void removeObserver(Observer observer) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void notifyObserver() {
        throw new UnsupportedOperationException();
        
    }

    public void start(String gameId, String userId){
        throw new UnsupportedOperationException();
        
    }

    public void finishGame(String gameId, String userId){
        throw new UnsupportedOperationException();
    }

    public Question nextQuestion(String gameId){
        throw new UnsupportedOperationException();
    }

    public void exitGame(String userId, String gameId){
        throw new UnsupportedOperationException();
    }

    public Score calculateUserScore(int gameDuration, int difficulty, GameMode gameMode, String LatLang, String questionID, String UserID){
        //this.gameMode.calculateScore()
        // notifyOverver()
        return new Score();
    }
    
    
    


    

}


