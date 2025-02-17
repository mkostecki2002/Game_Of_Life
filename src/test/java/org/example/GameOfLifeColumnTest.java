package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameOfLifeColumnTest {
    GameOfLifeBoardDaoFactory daoFactory = new GameOfLifeBoardDaoFactory();
    FileGameOfLifeBoardDao dao = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeRowAndColumnTest1.dat");
    GameOfLifeBoard golb = dao.read();

    GameOfLifeColumnTest() throws GameOfLifeFileException {
    }

    /**
     * Values of GameOfLifeCell objects of board golb
     * [false,false,true]
     * [false,false,true]
     * [true,true,false]
     * **/
    @Test
    void countAliveCellsandDeadCells() {

        GameOfLifeColumn colTest = golb.getColumn(0);

        Assertions.assertEquals(colTest.countAliveCells(),1);
        Assertions.assertEquals(colTest.countDeadCells(),2);
    }

    @Test
    void equalsAndHashCodeAndToStringTest() {
        //colTest1 and colTest2 has same values of GameOfLifeCell objects, colTest3 is different
        GameOfLifeColumn colTest1 = golb.getColumn(0);
        GameOfLifeColumn colTest2 = golb.getColumn(1);
        GameOfLifeColumn colTest3 = golb.getColumn(2);

        //Equals tests
        Assertions.assertEquals(colTest1,colTest2);
        //Same objects
        Assertions.assertEquals(colTest1,colTest1);
        //Different values
        Assertions.assertNotEquals(colTest1,colTest3);
        //Object is null or object have different class
        Assertions.assertNotEquals(colTest1,new Object());
        Assertions.assertNotEquals(colTest1,null);

        //toString tests
        String resultTest = "GameOfLifeColumn [alive cells=1, dead cells=2]";
        Assertions.assertEquals(resultTest,colTest1.toString());

        //HashCode tests
        Assertions.assertEquals(colTest1.hashCode(),colTest2.hashCode());
    }

    @Test
    void cloneTest() throws CloneNotSupportedException {
        GameOfLifeColumn original = golb.getColumn(0);
        GameOfLifeColumn cloned = original.clone();

        Assertions.assertNotSame(original, cloned);
        Assertions.assertNotSame(original.getColumn(), cloned.getColumn());
        Assertions.assertEquals(original, cloned);

        for (int i = 0; i < original.getColumn().size(); i++) {
            GameOfLifeCell originalCell = original.getColumn().get(i);
            GameOfLifeCell clonedCell = cloned.getColumn().get(i);

            Assertions.assertNotSame(originalCell, clonedCell);
            Assertions.assertEquals(originalCell, clonedCell);
        }
    }
}