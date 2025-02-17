package org.example;

public class GameOfLifeDatabaseException extends GameOfLifeException {
    public GameOfLifeDatabaseException(String message) {
        super(message);
    }

    public GameOfLifeDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
