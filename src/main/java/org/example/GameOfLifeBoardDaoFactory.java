package org.example;

public class GameOfLifeBoardDaoFactory {
    FileGameOfLifeBoardDao getFileGameOfLifeBoardDao(String fileName) {
        return new FileGameOfLifeBoardDao(fileName);
    }

    JdbcGameOfLifeBoardDao getJdbcGameOfLifeBoardDao(String url, String username, String password) {
        return new JdbcGameOfLifeBoardDao(url, username, password);
    }
}
