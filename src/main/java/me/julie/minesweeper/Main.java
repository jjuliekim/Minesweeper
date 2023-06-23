package me.julie.minesweeper;

import javafx.application.Application;
import javafx.stage.Stage;
import me.julie.minesweeper.controller.GameController;
import me.julie.minesweeper.view.GameView;

/**
 * Represents a minesweeper application
 */
public class Main extends Application {
    /**
     * starts the GUI for Minesweeper
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     */
    @Override
    public void start(Stage stage) {
        GameView view = new GameView();
        stage.setTitle("Minesweeper");
        stage.setScene(view.load());
        stage.show();
        GameController controller = new GameController();
        controller.run();
    }

    /**
     * entry point for application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}