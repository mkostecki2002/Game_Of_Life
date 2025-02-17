package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GameOfLifeRowTest {
    GameOfLifeBoardDaoFactory daoFactory = new GameOfLifeBoardDaoFactory();
    FileGameOfLifeBoardDao dao = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeRowAndColumnTest1.dat");
    GameOfLifeBoard golb = dao.read();

    GameOfLifeRowTest() throws GameOfLifeFileException {
    }

    /**
     * Values of GameOfLifeCell objects of board golb
     * [false,false,true]
     * [false,false,true]
     * [true,true,false]
     * **/
    @Test
    void countAliveAndDeadCells() {
        GameOfLifeRow rowTest = golb.getRow(2);

        Assertions.assertEquals(rowTest.countAliveCells(),2);
        Assertions.assertEquals(rowTest.countDeadCells(),1);
    }

    @Test
    void equalsAndHashCodeAndToStringTest() {
        //rowTest1 and rowTest2 has same values of GameOfLifeCell objects, rowTest3 is different
        GameOfLifeRow rowTest1 = golb.getRow(0);
        GameOfLifeRow rowTest2 = golb.getRow(1);
        GameOfLifeRow rowTest3 = golb.getRow(2);

        //Equals tests
        Assertions.assertEquals(rowTest1,rowTest2);
        //Same objects
        Assertions.assertEquals(rowTest1,rowTest1);
        //Diiferent values
        Assertions.assertNotEquals(rowTest1,rowTest3);

        //Object is null or object have different class
        Assertions.assertNotEquals(rowTest1,null);
        Assertions.assertNotEquals(rowTest1,new Object());

        //toString tests
        String resultTest = "GameOfLifeRow [alive cells=1, dead cells=2]";
        Assertions.assertEquals(resultTest,rowTest1.toString());

        //HashCode tests
        Assertions.assertEquals(rowTest1.hashCode(),rowTest2.hashCode());
    }

    @Test
    void cloneTest() throws CloneNotSupportedException {
        GameOfLifeRow original = golb.getRow(1);
        GameOfLifeRow cloned = original.clone();

        Assertions.assertNotSame(original, cloned);
        Assertions.assertNotSame(original.getRow(), cloned.getRow());
        Assertions.assertEquals(original, cloned);

        for (int i = 0; i < original.getRow().size(); i++) {
            GameOfLifeCell originalCell = original.getRow().get(i);
            GameOfLifeCell clonedCell = cloned.getRow().get(i);

            Assertions.assertNotSame(originalCell, clonedCell);
            Assertions.assertEquals(originalCell, clonedCell);
        }
    }
}