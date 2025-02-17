package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GameOfLifeCellTest {

    GameOfLifeBoardDaoFactory daoFactory = new GameOfLifeBoardDaoFactory();
    FileGameOfLifeBoardDao dao = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeCellTest1.dat");
    GameOfLifeBoard golb = dao.read();

    GameOfLifeCellTest() throws GameOfLifeFileException {
    }

    /**
 * Values of GameOfLifeCell objects of board golb
 * [false,false,false]
 * [true,false,false]
 * [false,false,false]
 * **/
    @Test
    void getterTestGameOfLifeCell() {
        GameOfLifeCell cell = golb.getCell(0,0);
        cell.updateState(true);

        //Size of cell neighbors should be 8
        Assertions.assertEquals(8, cell.getNeighbors().size());
        Assertions.assertTrue(cell.getCellValue());
    }

    @Test
    void equalHashCodeCompareTestGameOfLifeCell() {
        GameOfLifeCell cellFalse1 = new GameOfLifeCell(false);
        GameOfLifeCell cellFalse2 = new GameOfLifeCell(false);
        GameOfLifeCell cellTrue = new GameOfLifeCell(true);

        //Same object equals
        Assertions.assertEquals(cellTrue, cellTrue);

        //Same values equals
        Assertions.assertEquals(cellFalse1,cellFalse2);

        //Null object or object from different class not equals
        Assertions.assertNotEquals(cellFalse1, null);
        Assertions.assertNotEquals(cellFalse2, new Object());

        Assertions.assertEquals(0,cellFalse1.compareTo(cellFalse2));
        Assertions.assertTrue(cellFalse2.compareTo(cellTrue) < 0);
        Assertions.assertTrue(cellTrue.compareTo(cellFalse1) > 0);
        Assertions.assertThrows(NullPointerException.class,() -> cellFalse1.compareTo(null));
    }

    @Test
    void cloneTestGameOfLifeCell() throws CloneNotSupportedException {
        GameOfLifeCell original = golb.getCell(0,0);
        GameOfLifeCell clone = original.clone();

        List<GameOfLifeCell> neighbors = original.getNeighbors();
        List<GameOfLifeCell> cloneNeighbors = clone.getNeighbors();

        Assertions.assertNotSame(original, clone);
        Assertions.assertEquals(neighbors, cloneNeighbors);

        Assertions.assertEquals(original, clone);

    }
}