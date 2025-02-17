package org.example;

import java.io.Serializable;

class PlainGameOfLifeSimulator implements GameOfLifeSimulator, Serializable {
    @Override
    public void doStep(GameOfLifeBoard golb) {

        int widthBoard = golb.getBoard().size();
        int heightBoard = golb.getBoard().getFirst().size();

        boolean[][] predictBoard = new boolean[widthBoard][heightBoard];

        for (int i = 0; i < widthBoard; i++) {
            for (int j = 0; j < heightBoard; j++) {
                predictBoard[i][j] = golb.getCell(i, j).nextState();
            }
        }

        for (int i = 0; i < widthBoard; i++) {
            for (int j = 0; j < heightBoard; j++) {
                golb.getCell(i, j).updateState(predictBoard[i][j]);
            }
        }
    }
}
