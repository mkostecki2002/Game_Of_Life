package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class FileGameOfLifeBoardDaoTest {
    GameOfLifeBoardDaoFactory factory = new GameOfLifeBoardDaoFactory();

    @BeforeAll
    static void setupIfFilesDoesntExist() throws Exception {
        GameOfLifeBoardDaoFactory factory = new GameOfLifeBoardDaoFactory();

        FileGameOfLifeBoardDao dao1 = factory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest1.dat");
        FileGameOfLifeBoardDao dao2 = factory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest2.dat");
        FileGameOfLifeBoardDao dao3 = factory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest3.dat");
        FileGameOfLifeBoardDao dao4 = factory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeCellTest1.dat");
        FileGameOfLifeBoardDao dao5 = factory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeRowAndColumnTest1.dat");

        File directory = new File("src/test/resources");

        boolean isDirectoryEmpty = false;

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files == null || files.length == 0) {
                isDirectoryEmpty = true;
            }
        }

        if (isDirectoryEmpty){
        //GameOfLifeBoard Data
        GameOfLifeBoard golb1 = new GameOfLifeBoard(5,5,30,new PlainGameOfLifeSimulator());

        List<List<GameOfLifeCell>> testBoard1 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList(
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(true)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true)
                        )
                )
        );

        golb1.setBoard(testBoard1);

        GameOfLifeBoard golb2 = new GameOfLifeBoard(3,3, 30,new PlainGameOfLifeSimulator());
        List<List<GameOfLifeCell>> testBoard2 = golb2.getBoard();
        GameOfLifeBoard golb3 = new GameOfLifeBoard(3,3, 30, new PlainGameOfLifeSimulator());
        golb3.setBoard(testBoard2);

        dao1.write(golb1);
        dao2.write(golb2);
        dao3.write(golb3);

        //GameOfLifeCell Data
        List<List<GameOfLifeCell>> testBoard3 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList(
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false)
                        )
                )
        );
        GameOfLifeBoard golb4 = new GameOfLifeBoard(3,3, 30, new PlainGameOfLifeSimulator());
        golb4.setBoard(testBoard3);
        dao4.write(golb4);


        //GameOfLifeColumn and GameOfLifeRow Data
        List<List<GameOfLifeCell>> testBoard4 = new ArrayList<>(
                Arrays.asList(
                        Arrays.asList(
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(false),
                                new GameOfLifeCell(true)
                        ),
                        Arrays.asList(
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(true),
                                new GameOfLifeCell(false)
                        )
                )
        );
        GameOfLifeBoard golb5 = new GameOfLifeBoard(3,3, 30, new PlainGameOfLifeSimulator());
        golb5.setBoard(testBoard4);

        dao5.write(golb5);
        }
    }

    @Test
    void writeAndReadTest() throws GameOfLifeFileException {
        Path pathOfTestData = Paths.get("src","test","resources","testFileGameOfLifeBoard.dat");

        FileGameOfLifeBoardDao dao = factory.getFileGameOfLifeBoardDao(pathOfTestData.toString());
        FileGameOfLifeBoardDao daoNotExisting = factory.getFileGameOfLifeBoardDao("src/test/resources/neverExist.dat");

        GameOfLifeBoard golb1 = new GameOfLifeBoard(3,3, 30, new PlainGameOfLifeSimulator());
        dao.write(golb1);

        //Expect that dao.close() won't change anything
        dao.close();
        GameOfLifeBoard golb2 = dao.read();

        //golb1 and golb2 should be equal
        Assertions.assertEquals(golb1,golb2);
        Assertions.assertThrows(GameOfLifeFileException.class,daoNotExisting::read);

        pathOfTestData.toFile().setReadOnly();

        //Expect access denied
        Assertions.assertThrows(GameOfLifeFileException.class,()-> dao.write(golb2));

        pathOfTestData.toFile().setWritable(true);
        pathOfTestData.toFile().setExecutable(true);

        Assertions.assertDoesNotThrow(() -> Files.deleteIfExists(pathOfTestData));
    }
}