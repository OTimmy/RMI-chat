package gcom.observer;

/**
 * Created by c12ton on 9/29/16.
 */
public interface Subject {
    void registerObservers(Observer ...obs);
    void notifyObserver();
}
