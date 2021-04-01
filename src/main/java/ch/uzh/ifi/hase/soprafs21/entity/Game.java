package ch.uzh.ifi.hase.soprafs21.entity;

import org.hibernate.mapping.Map;

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
    private Map<String, Integer> userScores;
    }

    @Override
    public void registerObserver(Observer observer) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeObserver(Observer observer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyObserver() {
        // TODO Auto-generated method stub
        
    }

    public void startGame(String gameId, String userId){
        throw new UnsupportedOperationException();
        
    }

    public void startGame(String gameId, String userId){
    }

    public Question nextQuestion(String gameId){
    }

    public void exitGame(String userId, String gameId){

    }

    public Score calculateUserScore(int gameDuration, int difficulty, GameMode gameMode, String LatLang, String questionID, String UserID){
        //this.gameMode.calculateScore()
        // notifyOverver()
        return new Score();
    }
    
    
    


    

}


