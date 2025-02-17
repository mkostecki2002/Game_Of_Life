package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class FormController {

    private static final Logger logger = LoggerFactory.getLogger(FormController.class);

    @FXML
    private ResourceBundle bundle;

    @FXML
    private Label valueOfBoardWidth;

    @FXML
    private Label valueOfBoardHeight;

    @FXML
    private ToggleGroup densityOfBoard;

    @FXML
    private Slider boardWidthSlider;

    @FXML
    private Slider boardHeightSlider;

    //Bundle LANG
    @FXML
    private Label titleString;

    @FXML
    private Label boardWidthString;

    @FXML
    private Label boardHeightString;

    @FXML
    private Label boardDensityString;

    @FXML
    private RadioButton low;

    @FXML
    private RadioButton medium;

    @FXML
    private RadioButton high;

    public enum OptionsOfDensity {
        LOW(10),
        MEDIUM(30),
        HIGH(50);

        private final int percent;

        OptionsOfDensity(int percent) {
            this.percent = percent;
        }
    }


    @FXML
    public void initialize() {
        densityOfBoard = new ToggleGroup();

        low.setToggleGroup(densityOfBoard);
        medium.setToggleGroup(densityOfBoard);
        high.setToggleGroup(densityOfBoard);

        valueOfBoardWidth.setText("" + (int) boardWidthSlider.getValue());

        boardWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueOfBoardWidth.setText("" + newValue.intValue());
        });
        boardHeightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            valueOfBoardHeight.setText("" + newValue.intValue());
        });
    }

    @FXML
    public void start() throws IOException {
        if (boardWidthSlider != null && boardHeightSlider != null && densityOfBoard.getSelectedToggle() != null) {
            RadioButton option = (RadioButton) densityOfBoard.getSelectedToggle();
            String name = option.getId();
            int valueOfWidth = (int) boardWidthSlider.getValue();
            int valueOfHeight = (int) boardHeightSlider.getValue();
            int valueOfDensity = switch (name) {
                case "LOW" -> OptionsOfDensity.LOW.percent;
                case "HIGH" -> OptionsOfDensity.HIGH.percent;
                default -> OptionsOfDensity.MEDIUM.percent;
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/board.fxml"));
            loader.setResources(bundle);
            Parent root = loader.load();
            BoardController boardController = loader.getController();

            boardController.setData(valueOfDensity,valueOfWidth,valueOfHeight,bundle);

            Stage stage = Main.getPrimaryStage();
            stage.getScene().setRoot(root);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setTitle(bundle.getString("game.title"));
        }
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
        updateLanguage();
    }

    @FXML
    public void setEnglish() {
        loadLanguage(new Locale("en"));
    }

    @FXML
    public void setPolish() {
        loadLanguage(new Locale("pl"));
    }

    public void loadLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("bundle/lang", locale);
        updateLanguage();
        logger.info("Language successfully changed to: {}", locale.getDisplayLanguage());
    }

    public void updateLanguage() {
        if (bundle != null) {
            boardWidthString.setText(bundle.getString("slider.boardWidth"));
            boardHeightString.setText(bundle.getString("slider.boardHeight"));
            boardDensityString.setText(bundle.getString("slider.boardDensity"));
            titleString.setText(bundle.getString("game.title"));
            low.setText(bundle.getString("density.low"));
            low.setToggleGroup(densityOfBoard);
            medium.setText(bundle.getString("density.medium"));
            medium.setToggleGroup(densityOfBoard);
            high.setText(bundle.getString("density.high"));
            high.setToggleGroup(densityOfBoard);
            Main.getPrimaryStage().setTitle(bundle.getString("initial.title"));
        }
    }
}