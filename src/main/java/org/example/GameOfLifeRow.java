package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class GameOfLifeRow implements Cloneable {
    private List<GameOfLifeCell> row;

    public GameOfLifeRow(List<GameOfLifeCell> row) {
        this.row = row;
    }

    public int countAliveCells() {
        int counter = 0;

        for (GameOfLifeCell cell : row) {
            if (cell.getCellValue()) {
                counter++;
            }
        }
        return counter;
    }

    public int countDeadCells() {
        return row.size() - this.countAliveCells();
    }

    public List<GameOfLifeCell> getRow() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return new EqualsBuilder().append(row,((GameOfLifeRow) obj).row).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(row).toHashCode();
    }

    @Override
    public String toString() {
        return "GameOfLifeRow [alive cells=" + countAliveCells() + ", dead cells=" + countDeadCells() + "]";
    }

    @Override
    public GameOfLifeRow clone() throws CloneNotSupportedException {
        GameOfLifeRow cloned = new GameOfLifeRow(new ArrayList<>());
        for (GameOfLifeCell cell : this.row) {
            cloned.getRow().add(cell.clone());
        }
        return cloned;
    }
}
