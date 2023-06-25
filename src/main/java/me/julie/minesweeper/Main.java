package me.julie.minesweeper;

import javafx.application.Application;
import javafx.stage.Stage;
import me.julie.minesweeper.controller.GameController;
import me.julie.minesweeper.view.GameView;

/**
 * Represents a minesweeper application
 */
public class Main extends Application {
    private static Main instance;
    private Stage stage;

    public static Main getInstance() {
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * starts the GUI for Minesweeper
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        GameController controller = new GameController();
        GameView view = new GameView(controller);
        stage.setTitle("Minesweeper");
        stage.setScene(view.load());
        stage.show();
    }

    public Main() {
        instance = this;
    }

    /**
     * entry point for application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}