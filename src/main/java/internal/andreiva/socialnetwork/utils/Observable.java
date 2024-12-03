package internal.andreiva.socialnetwork.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable
{
    protected List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer)
    {
        observers.add(observer);
    }
    public void removeObserver(Observer observer)
    {
        observers.remove(observer);
    }
    protected void notifyObservers(Event event)
    {
        observers.forEach(observer -> observer.update(event));
    }
}
