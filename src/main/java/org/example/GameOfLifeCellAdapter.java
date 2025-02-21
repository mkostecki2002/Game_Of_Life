package org.example;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameOfLifeCellAdapter {
    public static BooleanProperty createBooleanPropertyGameOfLifeCell(GameOfLifeCell cell) {
        BooleanProperty property = new SimpleBooleanProperty(cell.getCellValue());

        cell.addPropertyChangeListener(evt -> {
            if ("value".equals(evt.getPropertyName())) {
                property.set((Boolean) evt.getNewValue());
            }
        });

        property.addListener((obs, oldValue, newValue) -> {
            cell.updateState(newValue);
        });


        return property;
    }
}
