package ch.uzh.ifi.hase.soprafs21.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ch.uzh.ifi.hase.soprafs21.entity.gamemodes.GameMode;
import ch.uzh.ifi.hase.soprafs21.entity.patterns.Observer;
import ch.uzh.ifi.hase.soprafs21.entity.patterns.SubjetObserver;
import ch.uzh.ifi.hase.soprafs21.entity.usermodes.UserMode;

public class Game implements SubjetObserver{
    private final String gameId;
    private final String roomKey;
    private GameStatus status;
    private Question[] questions;
    private final UserMode userMode;
    private final GameMode gameMode;
    
    // Observer pattern fields
    final ArrayList<Observer> observers;

    // brauchen wir das?
    private HashMap<String, Integer> userScores;

    public Game(UserMode uMode, GameMode gMode){
        // Game Config
        this.userMode = uMode;
        this.gameMode = gMode;

        // Game Internals
        this.gameId = createGameId();
        this.roomKey = createRoomKey();

        // Observer pattern fields
        this.observers = new ArrayList<>();
    }

    // ID creation
    private String createGameId(){
        throw new UnsupportedOperationException();
    }

    private String createRoomKey(){
        throw new UnsupportedOperationException();
    }
    
    // Observer Methods
    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observers.indexOf(observer));
    }

    @Override
    public void notifyObservers() {
        Iterator<Observer> it = this.observers.iterator();
        Observer observer;

        while(it.hasNext()){
            observer = it.next();
            observer.update();
        }
    }
            

    public void start(String gameId, String userId){
        throw new UnsupportedOperationException();
        
    }

    public void finish(String gameId, String userId){
        throw new UnsupportedOperationException();
    }

    public Question nextQuestion(String gameId){
        throw new UnsupportedOperationException();
    }

    public void exitGame(String userId, String gameId){
        throw new UnsupportedOperationException();
    }

    public Score calculateUserScore(int difficulty, GameMode gameMode, String latLang, String questionID, String userID){
        //this.gameMode.calculateScore()
        // notifyOverver()
        return new Score();
    }
}