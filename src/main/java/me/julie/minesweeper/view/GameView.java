package me.julie.minesweeper.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import me.julie.minesweeper.Main;

import java.io.IOException;

/**
 * View for the minesweeper game
 */
public class GameView {
    FXMLLoader loader;

    /**
     * constructor for GameView
     */
    public GameView() {
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
