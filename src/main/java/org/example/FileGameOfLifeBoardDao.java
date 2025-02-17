package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class FileGameOfLifeBoardDao implements Dao<GameOfLifeBoard> {
    String fileName;

    public FileGameOfLifeBoardDao(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public GameOfLifeBoard read() throws GameOfLifeFileException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (GameOfLifeBoard) ois.readObject();
        } catch (Exception e) {
            Object[] messageArgs = new Object[1];
            messageArgs[0] = fileName;
            throw new GameOfLifeFileException("fileError.loadFile", e, messageArgs);
        }
    }

    @Override
    public void write(GameOfLifeBoard obj) throws GameOfLifeFileException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(obj);
        } catch (Exception e) {
            Object[] messageArgs = new Object[1];
            messageArgs[0] = fileName;
            throw new GameOfLifeFileException("fileError.saveToFile", e, messageArgs);
        }
    }

    @Override
    public void close() {

    }
}
