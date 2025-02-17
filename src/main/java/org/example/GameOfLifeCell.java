package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class GameOfLifeCell implements Serializable, Cloneable, Comparable<GameOfLifeCell> {

    private boolean value;
    private List<GameOfLifeCell> neighbors = new ArrayList<>();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GameOfLifeCell(boolean value) {
        this.value = value;
    }

    public boolean getCellValue() {
        return value;
    }

    public List<GameOfLifeCell> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<GameOfLifeCell> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean nextState() {

        int countAlive = 0;
        boolean result = false;

        for (GameOfLifeCell neighbor : neighbors) {
            if (neighbor.getCellValue()) {
                countAlive++;
            }
        }

        if (value) {
            if (countAlive == 2 || countAlive == 3) {
               result = true;
            }
        } else {
            if (countAlive == 3) {
                result = true;
            }
        }
        return result;
    }

    public void updateState(boolean newState) {
        boolean oldState = value;
        value = newState;
        support.firePropertyChange("value", oldState, newState);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return new EqualsBuilder().append(value, ((GameOfLifeCell) obj).value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(value).toHashCode();
    }

    @Override
    public String toString() {
        return "GameOfLifeCell [value=" + value + "]";
    }

    @Override
    public int compareTo(GameOfLifeCell o) {
        if (o == null) {
            throw new NullPointerException();
        } else {
            int valueComprasion = Boolean.compare(this.value, o.value);
            if (valueComprasion != 0) {
                return valueComprasion;
            }
            return Integer.compare(this.neighbors.size(), o.neighbors.size());
        }
    }

    @Override
    protected GameOfLifeCell clone() {
        GameOfLifeCell clone = new GameOfLifeCell(value);
        clone.neighbors = new ArrayList<>();
        for (GameOfLifeCell neighbor : neighbors) {
            clone.neighbors.add(new GameOfLifeCell(neighbor.value));
        }
        return clone;
    }
}
