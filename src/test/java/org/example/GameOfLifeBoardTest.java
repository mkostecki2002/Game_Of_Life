package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Values of GameOfLifeCell objects of board golb1
 * [true,false,false,false,true]
 * [false,false,false,true,true]
 * [true,false,true,true,true]
 * [false,false,false,false,true]
 * [true,true,false,false,true]

 * golb2 and golb3 boards have the same random values of GameOfLifeCell, they are equal
 */
class GameOfLifeBoardTest {
    GameOfLifeBoardDaoFactory daoFactory = new GameOfLifeBoardDaoFactory();
    FileGameOfLifeBoardDao dao1 = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest1.dat");
    FileGameOfLifeBoardDao dao2 = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest2.dat");
    FileGameOfLifeBoardDao dao3 = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest3.dat");

    GameOfLifeBoard golb1 = dao1.read();
    GameOfLifeBoard golb2 = dao2.read();
    GameOfLifeBoard golb3 = dao3.read();

    GameOfLifeBoardTest() throws GameOfLifeFileException {}

    @Test
    void GameOfLifeBoardConstructorTest(){
        List<List<GameOfLifeCell>> testBoard = Arrays.asList(
                Arrays.asList(new GameOfLifeCell(false),new GameOfLifeCell(false)),
                Arrays.asList(new GameOfLifeCell(false),new GameOfLifeCell(false)),
                Arrays.asList(new GameOfLifeCell(false),new GameOfLifeCell(false))
        );
        GameOfLifeBoard golb1 = new GameOfLifeBoard(2,3, 30, new PlainGameOfLifeSimulator());
        GameOfLifeBoard golb2 = new GameOfLifeBoard(2,3, 30, new PlainGameOfLifeSimulator());
        golb2.setBoard(testBoard);
        System.out.println(golb2.getBoard());

        List<List<GameOfLifeCell>> golb1TestBoard = golb1.getBoard();
        List<List<GameOfLifeCell>> golb2TestBoard = golb2.getBoard();

        int counter = 0;

        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 2; j++){
                if (golb1TestBoard.get(i).get(j).getCellValue() != golb2TestBoard.get(i).get(j).getCellValue()){
                    counter++;
                }
            }
        }
        Assertions.assertTrue(counter > 0);

    }

    @Test
    void doSimulationStepTest() {
        //It shouldn't change anything
        golb1.setBoard(null);

        golb1.doSimulationStep();

        List<List<GameOfLifeCell>> testBoard2 = golb1.getBoard();

        //Expected values of GameOfLifeCell based on rules of GameOfLife
        Assertions.assertTrue(testBoard2.get(2).get(0).getCellValue());
        Assertions.assertTrue(testBoard2.get(1).get(1).getCellValue());

        Assertions.assertFalse(testBoard2.get(4).get(4).getCellValue());
        Assertions.assertFalse(testBoard2.get(2).get(2).getCellValue());

        Assertions.assertFalse(testBoard2.get(2).get(1).getCellValue());
    }

    @Test
    void getterAndSetterTest(){
        golb1.set(0,0,true);
        Assertions.assertTrue(golb1.get(0,0));
        golb1.set(0,0,false);
        Assertions.assertFalse(golb1.get(0,0));
    }

    @Test
    void equalsAndHashCodeAndToStringTest(){
        //Equals same values
        Assertions.assertEquals(golb2, golb3);

        //Equals itself
        Assertions.assertEquals(golb1, golb1);

        //Different values
        Assertions.assertNotEquals(golb1, golb2);

        //Object is null
        Assertions.assertNotEquals(golb2, null);

        //Object has different class
        Assertions.assertNotEquals(golb2, new Object());

        String resultToString = "GameOfLifeBoard [board=[GameOfLifeCell [value=true],GameOfLifeCell [value=false],GameOfLifeCell [value=true],GameOfLifeCell [value=false],GameOfLifeCell [value=true]]," +
                "[GameOfLifeCell [value=false],GameOfLifeCell [value=false],GameOfLifeCell [value=false],GameOfLifeCell [value=false],GameOfLifeCell [value=true]]," +
                "[GameOfLifeCell [value=false],GameOfLifeCell [value=false],GameOfLifeCell [value=true],GameOfLifeCell [value=false],GameOfLifeCell [value=false]]," +
                "[GameOfLifeCell [value=false],GameOfLifeCell [value=true],GameOfLifeCell [value=false],GameOfLifeCell [value=false],GameOfLifeCell [value=false]]," +
                "[GameOfLifeCell [value=true],GameOfLifeCell [value=true],GameOfLifeCell [value=false],GameOfLifeCell [value=true],GameOfLifeCell [value=true]]]";
        Assertions.assertEquals(golb1.toString(), resultToString);

        //HashCode test
        Assertions.assertNotEquals(golb1.hashCode(), golb2.hashCode());
       }

    @Test
    void cloneTest() throws CloneNotSupportedException {
        // Klonowanie planszy
        GameOfLifeBoard cloned = golb1.clone();

        // 1. Sprawdzenie, czy instancje są różne
        Assertions.assertNotSame(golb1, cloned);

        // 2. Sprawdzenie struktury planszy
        List<List<GameOfLifeCell>> originalBoard = golb1.getBoard();
        List<List<GameOfLifeCell>> clonedBoard = cloned.getBoard();

        Assertions.assertEquals(originalBoard.size(), clonedBoard.size());
        Assertions.assertEquals(originalBoard.get(0).size(), clonedBoard.get(0).size());

        // 3. Głębokie klonowanie - każda komórka powinna być nową instancją
        for (int i = 0; i < originalBoard.size(); i++) {
            List<GameOfLifeCell> originalRow = originalBoard.get(i);
            List<GameOfLifeCell> clonedRow = clonedBoard.get(i);

            Assertions.assertNotSame(originalRow, clonedRow);
            for (int j = 0; j < originalRow.size(); j++) {
                GameOfLifeCell originalCell = originalRow.get(j);
                GameOfLifeCell clonedCell = clonedRow.get(j);

                Assertions.assertNotSame(originalCell, clonedCell);
                Assertions.assertEquals(originalCell, clonedCell);
            }
        }

        // 4. Sprawdzenie, czy plansze są równe logicznie
        Assertions.assertEquals(golb1, cloned);

        // Sprawdzenie sąsiadów
        for (int i = 0; i < cloned.getBoard().size(); i++) {
            for (int j = 0; j < cloned.getBoard().get(0).size(); j++) {
                GameOfLifeCell originalCell = golb1.getCell(j, i);
                GameOfLifeCell clonedCell = cloned.getCell(j, i);

                Assertions.assertNotSame(originalCell.getNeighbors(), clonedCell.getNeighbors());
                Assertions.assertEquals(originalCell.getNeighbors(), clonedCell.getNeighbors());
            }
        }

        golb1.set(1, 0, true);

        // Sprawdzenie, czy zmiana w oryginale nie wpłynęła na klona
        Assertions.assertNotEquals(golb1, cloned);
        Assertions.assertFalse(cloned.get(1, 0));

    }
}