module me.julie.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens me.julie.minesweeper to javafx.fxml;
    exports me.julie.minesweeper;
    exports me.julie.minesweeper.controller;
    opens me.julie.minesweeper.controller to javafx.fxml;
    exports me.julie.minesweeper.view;
    opens me.julie.minesweeper.view to javafx.fxml;
    exports me.julie.minesweeper.model;
    opens me.julie.minesweeper.model to javafx.fxml;
}