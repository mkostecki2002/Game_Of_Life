package org.example;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

public class BoardController {

    GameOfLifeBoard golb;
    GameOfLifeCellStringConverter converter = new GameOfLifeCellStringConverter();
    FileChooser fileChooser = new FileChooser();
    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

    @FXML
    private Menu file;

    @FXML
    private MenuItem save;

    @FXML
    private MenuItem load;

    @FXML
    private Label boardLabel;

    @FXML
    private Button simulationButton;

    @FXML
    private Label aliveCellsLabel;

    @FXML
    private Label deadCellsLabel;

    @FXML
    private ResourceBundle bundle;

    @FXML
    private GridPane boardGrid;

    public void setData(int density, int width, int height, ResourceBundle bundle) {
        golb = new GameOfLifeBoard(width,height,density,new PlainGameOfLifeSimulator());
        logger.info("Created GameOfLifeBoard with dimensions: {}x{} and density: {}", width, height, density);
        this.bundle = bundle;
        if (bundle != null) {
            file.setText(bundle.getString("menu.file"));
            save.setText(bundle.getString("menu.save"));
            load.setText(bundle.getString("menu.load"));
            boardLabel.setText(bundle.getString("board.label"));
            aliveCellsLabel.setText(bundle.getString("alive.cells"));
            deadCellsLabel.setText(bundle.getString("dead.cells"));
            simulationButton.setText(bundle.getString("button.simulation"));
        }
        renderBoard();
    }

    private void renderBoard() {
        boardGrid.getChildren().clear();

        int height = golb.getBoard().size();
        int width = golb.getBoard().getFirst().size();

        boardGrid.setHgap(40);
        boardGrid.setVgap(10);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Label label;

                BooleanProperty cellProperty = GameOfLifeCellAdapter
                        .createBooleanPropertyGameOfLifeCell(golb.getCell(j, i));

                String[] separateString = converter.toString(cellProperty.get()).split(";");
                label = new Label(separateString[0]);
                label.setStyle("-fx-text-fill: " + separateString[1] + ";");

                if (height > 16) {
                    label.setStyle(label.getStyle() + "-fx-font-size: 15px;");
                } else if (height > 13) {
                    label.setStyle(label.getStyle() + "-fx-font-size: 20px;");
                } else {
                    label.setStyle(label.getStyle() + "-fx-font-size: 30px;");
                }

                boardGrid.add(label, j, i);

                label.setOnMouseClicked(mouseEvent -> updateLabel(label, cellProperty));
            }
        }
    }

    public void updateLabel(Label label, BooleanProperty cell) {
        cell.set(!cell.get());
        String [] separateString = converter.toString(cell.get()).split(";");
        label.setText(separateString[0]);
        label.setStyle(label.getStyle() + "-fx-text-fill: " + separateString[1] + ";");
    }

    @FXML
    void save() {
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath());
            try {
                dao.write(golb);
                logger.info("Successfully saved GameOfLifeBoard to file: {}", file.getAbsolutePath());
            } catch (GameOfLifeFileException e) {
                logger.error("Failed to save GameOfLifeBoard to file: {}", file.getAbsolutePath(), e);
                String message = MessageFormat.format(bundle.getString(e.getMessageKey()),e.getMessageArgs());
                showAlert(bundle.getString("fileError"), message);
            }
        }
    }

    @FXML
    void load() {
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            FileGameOfLifeBoardDao dao = new FileGameOfLifeBoardDao(file.getAbsolutePath());
            try {
                golb = dao.read();
                renderBoard();
                logger.info("Successfully loaded GameOfLifeBoard from file: {}", file.getAbsolutePath());
            } catch (GameOfLifeFileException e) {
                logger.error("Failed to load GameOfLifeBoard from file: {}", file.getAbsolutePath(), e);
                String message = MessageFormat.format(bundle.getString(e.getMessageKey()),e.getMessageArgs());
                showAlert(bundle.getString("fileError"), message);
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
