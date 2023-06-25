package me.julie.minesweeper.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import me.julie.minesweeper.Main;
import me.julie.minesweeper.controller.GameController;

import java.io.IOException;

/**
 * View for the minesweeper game
 */
public class GameView {
    FXMLLoader loader;

    /**
     * constructor for GameView
     */
    public GameView(GameController controller) {
        this.loader = new FXMLLoader(Main.class.getResource("minesweeper.fxml"));
    }

    /**
     * Loads a scene
     */
    public Scene load() throws IllegalStateException {
        try {
            return this.loader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
