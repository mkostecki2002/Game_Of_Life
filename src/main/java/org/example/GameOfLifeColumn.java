package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameOfLifeColumn implements Cloneable {
    private List<GameOfLifeCell> column;

    public GameOfLifeColumn(List<GameOfLifeCell> column) {
        this.column = column;
    }

    public int countAliveCells() {
        int counter = 0;

        for (GameOfLifeCell cell : column) {
            if (cell.getCellValue()) {
                counter++;
            }
        }
        return counter;
    }

    public List<GameOfLifeCell> getColumn() {
        return column;
    }

    public int countDeadCells() {
        return column.size() - countAliveCells();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return new EqualsBuilder().append(column, ((GameOfLifeColumn) obj).column).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(column).toHashCode();
    }

    @Override
    public String toString() {
        return "GameOfLifeColumn [alive cells=" + countAliveCells() + ", dead cells=" + countDeadCells() + "]";
    }

    @Override
    public GameOfLifeColumn clone() throws CloneNotSupportedException {
        GameOfLifeColumn cloned = new GameOfLifeColumn(new ArrayList<>());
        for (GameOfLifeCell cell : this.column) {
            cloned.getColumn().add(cell.clone());
        }
        return cloned;
    }
}