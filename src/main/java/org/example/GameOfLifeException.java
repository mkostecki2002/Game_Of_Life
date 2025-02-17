package org.example;

public class GameOfLifeException extends Exception {
    private String messageKey;

    public GameOfLifeException(String messageKey) {
        this.messageKey = messageKey;
    }

    public GameOfLifeException(String messageKey, Throwable cause) {
        super(messageKey,cause);
    }

    public String getMessageKey() {
        return messageKey;
    }
}

