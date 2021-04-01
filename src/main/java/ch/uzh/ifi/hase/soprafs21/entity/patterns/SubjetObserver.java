package ch.uzh.ifi.hase.soprafs21.entity.patterns;

public interface SubjetObserver {
    public void registerObserver(Observer observer);
    public void removeObserver(Observer observer);
    public void notifyObserver();
}
