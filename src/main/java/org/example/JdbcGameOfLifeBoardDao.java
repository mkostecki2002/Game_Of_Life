package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import javax.swing.*;

public class JdbcGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {

    String url;
    String username;
    String password;

    private static final Logger logger = LoggerFactory.getLogger(JdbcGameOfLifeBoardDao.class);

    public JdbcGameOfLifeBoardDao(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws GameOfLifeDatabaseException {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new GameOfLifeDatabaseException("some.bundle",e);
        }
    }

    @Override
    public void write(GameOfLifeBoard obj) throws GameOfLifeDatabaseException {
        String queryCheckBoard = "SELECT COUNT(*) AS COUNT FROM GAME_OF_LIFE_BOARDS WHERE NAME = ?";
        String queryBoard = "INSERT INTO GAME_OF_LIFE_BOARDS(NAME,WIDTH,HEIGHT,DENSITY)"
                + " VALUES(?,?,?,?)";
        String queryBoardValues = "INSERT INTO GAME_OF_LIFE_VALUES_OF_BOARD(ROWINDEX,COLUMNINDEX,"
                + "ISALIVE,ID_GAME_OF_LIFE_BOARD) VALUES(?,?,?,?)";
        try (Connection connection = this.getConnection();
             PreparedStatement checkStatement = connection.prepareStatement(queryCheckBoard);
             PreparedStatement statement1 = connection.prepareStatement(queryBoard, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statement2 = connection.prepareStatement(queryBoardValues)) {

            //DialogPane
            String boardName = JOptionPane.showInputDialog(null, "Podaj nazwe planszy:", "ZAPIS",
                    JOptionPane.PLAIN_MESSAGE);

            if (boardName.isEmpty()) {
                throw new GameOfLifeDatabaseException("bundle.valuecannotnull");
            }

            checkStatement.setString(1, boardName);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt("COUNT") > 0) {
                    throw new GameOfLifeDatabaseException("Plansza o podanej nazwie juÅ¼ istnieje.");
                }
            }

            statement1.setString(1, boardName);
            statement1.setInt(2, obj.getWidth());
            statement1.setInt(3, obj.getHeight());
            statement1.setInt(4, obj.getDensity());
            statement1.executeUpdate();

            ResultSet generatedKeys = statement1.getGeneratedKeys();
            if (!generatedKeys.next()) {
                logger.error("Couldn't get generated key");
                throw new SQLException("Couldn't get generated key");
            }
            for (int i = 0; i < obj.getHeight(); i++) {
                for (int j = 0; j < obj.getWidth(); j++) {
                    statement2.setInt(1, i);
                    statement2.setInt(2, j);
                    statement2.setBoolean(3, obj.get(i,j));
                    statement2.setInt(4, generatedKeys.getInt(1));
                    statement2.addBatch();
                }
                statement2.executeBatch();
            }


        } catch (SQLException e) {
            throw new GameOfLifeDatabaseException("some.bundle",e);
        }
    }

    @Override
    public GameOfLifeBoard read() throws GameOfLifeDatabaseException {
        String boardName = JOptionPane.showInputDialog(null,
                "Podaj nazwe planszy:", "Wczytywanie planszy", JOptionPane.PLAIN_MESSAGE);
        String selectQueryBoard = "SELECT * FROM GAME_OF_LIFE_BOARDS WHERE NAME = ?";
        String selectQueryValues = "SELECT gv.* FROM GAME_OF_LIFE_VALUES_OF_BOARD gv "
                + "JOIN GAME_OF_LIFE_BOARDS g ON gv.ID_GAME_OF_LIFE_BOARD = g.ID WHERE g.NAME = ?";
        try (Connection connection = this.getConnection();
             PreparedStatement statement1 = connection.prepareStatement(
                     selectQueryBoard);
             PreparedStatement statement2 = connection.prepareStatement(
                     selectQueryValues)) {

            statement1.setString(1, boardName);
            statement2.setString(1, boardName);
            boolean boardExist = false;
            int width = 0;
            int height = 0;
            int density = 0;

            try (ResultSet resultSetBoards = statement1.executeQuery()) {
                if (resultSetBoards.next()) {
                    boardExist = true;
                    width = resultSetBoards.getInt("WIDTH");
                    height = resultSetBoards.getInt("HEIGHT");
                    density = resultSetBoards.getInt("DENSITY");
                }
            }

            if (!boardExist) {
                throw new GameOfLifeDatabaseException("bundle.valuecannotnull");
            }

                GameOfLifeBoard board = new GameOfLifeBoard(width, height, density, new PlainGameOfLifeSimulator());

            try (ResultSet resultSetValues = statement2.executeQuery()) {
                while (resultSetValues.next()) {
                    int rowIndex = resultSetValues.getInt("ROWINDEX");
                    int columnIndex = resultSetValues.getInt("COLUMNINDEX");
                    boolean isAlive = resultSetValues.getBoolean("ISALIVE");
                    board.set(rowIndex, columnIndex, isAlive);
                }
            }
            return board;

        } catch (SQLException e) {
            throw new GameOfLifeDatabaseException("some.bundle",e);
        }
    }

    @Override
    public void close(){

    }
}
