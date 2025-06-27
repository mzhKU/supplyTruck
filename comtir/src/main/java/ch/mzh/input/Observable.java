package ch.mzh.input;

import ch.mzh.game.Observer;

public interface Observable {
    void addObserver(Observer o);
    void removeObserver(Observer o);
}
