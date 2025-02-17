package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class JdbcGameOfLifeBoardDaoTest {
    GameOfLifeBoardDaoFactory daoFactory = new GameOfLifeBoardDaoFactory();
    FileGameOfLifeBoardDao dao1 = daoFactory.getFileGameOfLifeBoardDao("src/test/resources/GameOfLifeBoardTest1.dat");
    JdbcGameOfLifeBoardDao dao2 = daoFactory.getJdbcGameOfLifeBoardDao("jdbc:h2:~/testdb","","");
    GameOfLifeBoard golb = dao1.read();

    JdbcGameOfLifeBoardDaoTest() throws GameOfLifeFileException {
    }

    @Test
    void getConnection() throws GameOfLifeDatabaseException {
        dao2.write(golb);

        GameOfLifeBoard readBoard = dao2.read();
        assertEquals(golb, readBoard);
        assertNotSame(golb, readBoard);
    }

    @Test
    void writeWithoutCommit() throws GameOfLifeDatabaseException {
        Connection connection = dao2.getConnection();
        try {
            connection.setAutoCommit(false);
            dao2.write(null);
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void writeWithInvalidConnection() {
        JdbcGameOfLifeBoardDao dao = daoFactory.getJdbcGameOfLifeBoardDao("jdbc:invalid", "", "");
        assertThrows(GameOfLifeDatabaseException.class, () -> dao.write(golb));
    }
}



