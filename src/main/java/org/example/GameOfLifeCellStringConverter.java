package org.example;

import javafx.util.StringConverter;

public class GameOfLifeCellStringConverter extends StringConverter<Boolean> {
    @Override
    public String toString(Boolean value) {
        return value ? "o;green" : "x;blue";
    }

    @Override
    public Boolean fromString(String s) {
        return s.startsWith("o;green");
    }
}
