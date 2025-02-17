package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameOfLifeFileException extends GameOfLifeException {
    private final Object[] messageArgs;
    private static final Logger logger = LoggerFactory.getLogger(GameOfLifeFileException.class);

    public GameOfLifeFileException(String messageKey, Throwable cause, Object[] messageArgs) {
        super(messageKey,cause);
        this.messageArgs = messageArgs;
    }

    @Override
    public String getMessageKey() {
        logger.info("Message key");
        return super.getMessageKey();
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }
}
