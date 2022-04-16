package classes.rest;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;


public class MainMenu {
    /**
     * Shows scene with a menu
     * @return Returns the scene.
     */
    public static VBox menus() {
        Label chess = new Label();
        chess.setText("Chess");
        chess.setFont(new Font("Arial",60));

        Button newGame = new Button();
        newGame.setText("New game");
        //on click starts new game
        newGame.setOnAction(E -> {
            Main.click.stop();
            Main.click.play();Game.choseOpponent();});

        Button loadGame = new Button();
        loadGame.setText("Load game");
        //on click shows games to chose from
        loadGame.setOnAction(E ->{
            Main.click.stop();
            Main.click.play(); GameSaveLoad.choseGameToContinue();});

        Button browseGame = new Button();
        browseGame.setText("Browse game");
        //on click shows games to chose from
        browseGame.setOnAction(E -> {
            Main.click.stop();
            Main.click.play();GameSaveLoad.choseGameToBrowse();});

        Button exitGame = new Button();
        exitGame.setText("Quit to desktop");
        //on click exing to desktop
        exitGame.setOnAction(E -> {
            Main.click.stop();
            Main.click.play();System.exit(0);});
        VBox layout = new VBox(15);
        layout.getChildren().add(chess);
        layout.getChildren().add(newGame);
        layout.getChildren().add(loadGame);
        layout.getChildren().add(browseGame);
        layout.getChildren().add(exitGame);
        layout.setAlignment(Pos.CENTER);
        Main.mainPane.setBackground(new Background(Main.menuBG));
        return layout;
    }
}
