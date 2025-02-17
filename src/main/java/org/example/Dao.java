package org.example;

import java.io.Serializable;

public interface Dao<T> extends AutoCloseable, Serializable {
    T read() throws GameOfLifeFileException, GameOfLifeDatabaseException;

    void write(T obj) throws GameOfLifeFileException, GameOfLifeDatabaseException;
}

