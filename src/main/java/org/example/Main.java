package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ResourceBundle;

public class Main extends Application {

    private static Stage primaryStage;
    private static ResourceBundle bundle;
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(@NotNull Stage stage) throws IOException {
        logger.info("Starting application...");
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/form.fxml"));
        bundle = ResourceBundle.getBundle("bundle/lang");
        loader.setResources(bundle);

        Parent root = loader.load();
        FormController controller = loader.getController();

        stage.setTitle(bundle.getString("initial.title"));
        Scene scene = new Scene(root);
        controller.setBundle(bundle);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}