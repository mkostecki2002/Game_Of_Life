package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;

public class GameOfLifeBoard implements Serializable, Cloneable {
    private final GameOfLifeSimulator gols;
    private List<List<GameOfLifeCell>> board;
    private final int width;
    private final int height;
    private final int density;

    public GameOfLifeBoard(int width, int height, int density, GameOfLifeSimulator gols) {
        this.gols = gols;
        this.width = width;
        this.height = height;
        this.density = density;
        this.board = new ArrayList<>();

        int counter = 0;
        double densityValue = density * 0.01;
        for (int i = 0; i < height; i++) {
            GameOfLifeCell [] row = new GameOfLifeCell[width];

            for (int j = 0; j < width; j++) {
                if (Math.abs(counter - (densityValue * height * width))
                        > Math.abs(++counter - (densityValue * height * width))) {
                    row[j] = new GameOfLifeCell(true);
                } else {
                    row[j] = new GameOfLifeCell(false);
                }
            }
            board.add(Arrays.asList(row));
        }

        List<List<GameOfLifeCell>> listOfColumns = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            GameOfLifeColumn column = getColumn(x);
            List<GameOfLifeCell> col = column.getColumn();
            Collections.shuffle(col);
            listOfColumns.add(col);
        }

        List<List<GameOfLifeCell>> listOfRows = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                row.add(listOfColumns.get(x).get(y));
            }
            listOfRows.add(row);
        }
        board = listOfRows;
        board = Collections.unmodifiableList(board);
        this.initializeNeighbors(width, height);
    }

    public void initializeNeighbors(int width, int height) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                List<GameOfLifeCell> neighbors = new ArrayList<>(8);
                for (int k = -1; k < 2; k++) {
                    int tempRow = (i + k + height) % height;
                    if (i != tempRow) {
                        neighbors.add(board.get(tempRow).get(j));
                    }
                    neighbors.add(board.get(tempRow).get((j + 1 + width) % width));
                    neighbors.add(board.get(tempRow).get((j - 1 + width) % width));
                }
                board.get(i).get(j).setNeighbors(neighbors);
            }
        }
    }

    public List<List<GameOfLifeCell>> getBoard() {
        int heightBoard = board.size();
        int widthBoard = board.getFirst().size();
        List<List<GameOfLifeCell>> tempBoard = new ArrayList<>();
        for (int i = 0; i < heightBoard; i++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int j = 0; j < widthBoard; j++) {
                row.add(board.get(i).get(j));
            }
            row = Collections.unmodifiableList(row);
            tempBoard.add(row);
        }
        tempBoard = Collections.unmodifiableList(tempBoard);
        return tempBoard;
    }

    public void setBoard(List<List<GameOfLifeCell>> board) {
        if (board != null) {
            this.board = board;
            this.initializeNeighbors(board.getFirst().size(), board.size());
        }
    }

    public boolean get(int x, int y) {
        return board.get(y).get(x).getCellValue();
    }

    public void set(int x, int y, boolean value) {
        board.get(y).get(x).updateState(value);
    }

    public void doSimulationStep() {
        gols.doStep(this);
    }

    public GameOfLifeCell getCell(int x, int y) {
        GameOfLifeCell cell = board.get(y).get(x);
        return cell;
    }

    public GameOfLifeRow getRow(int y) {
        List<GameOfLifeCell> rowList = new ArrayList<>();
        for (int i = 0; i < board.getFirst().size(); i++) {
            rowList.add(this.getCell(i,y));
        }
        return new GameOfLifeRow(rowList);
    }

    public GameOfLifeColumn getColumn(int x) {
        List<GameOfLifeCell> colList = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            colList.add(this.getCell(x,i));
        }
        return new GameOfLifeColumn(colList);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDensity() {
        return density;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return new EqualsBuilder().append(board, ((GameOfLifeBoard) obj).board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,37)
                .append(board.hashCode())
                .toHashCode();
    }

    @Override
    public String toString() {
        StringBuilder cells = new StringBuilder();
        int height = board.size();
        int width = board.getFirst().size();
        for (int i = 0; i < height; i++) {
            cells.append("[");
            for (int j = 0; j < width; j++) {
                cells.append(board.get(j).get(i).toString());
                if (j == width - 1) {
                    if (i == height - 1) {
                        cells.append("]");
                    } else {
                        cells.append("],");
                    }
                } else {
                    cells.append(",");
                }
            }
        }
        return "GameOfLifeBoard [board=" + cells + "]";
    }

    @Override
    protected GameOfLifeBoard clone() {
        GameOfLifeBoard clone = new GameOfLifeBoard(width, height, density, gols);
        List<List<GameOfLifeCell>> cloneBoard = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            List<GameOfLifeCell> row = new ArrayList<>();
            for (int j = 0; j < board.get(i).size(); j++) {
                row.add(board.get(i).get(j).clone());
            }
            cloneBoard.add(row);
        }
        clone.setBoard(cloneBoard);
        return clone;
    }
}
