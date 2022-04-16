package classes.rest;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


import java.io.File;



public class Main extends Application {
    public static BorderPane mainPane = new BorderPane();
    public static Scene mainScene = null;
    public static final BackgroundImage menuBG = new BackgroundImage(new Image("file:src/main/imgs/menuBG.jpg",1920,1080,true,false), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    public static final BackgroundImage gameBG = new BackgroundImage(new Image("file:src/main/imgs/stoneBG.jpg",1920,1080,true,false), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    public static final BackgroundImage bedrockBG = new BackgroundImage(new Image("file:src/main/imgs/bedrock.png", 100, 100, true, false), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    public static final MediaPlayer click = new MediaPlayer(new Media(new File("src/main/sounds/click.mp3").toURI().toString()));
    public static final MediaPlayer music = new MediaPlayer(new Media(new File("src/main/sounds/theme.mp3").toURI().toString()));
    public static final Image icon = new Image("file:src/main/icons/minecraft.png",100,100,true,false);

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Chess");
        HBox hBox = new HBox();
        Button button = new Button("Save logs");
        button.setOnAction(e->{ click.stop();click.play();Game.saveLogs();});
        hBox.getChildren().add(button);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        mainPane.setCenter(MainMenu.menus());
        mainPane.setBottom(hBox);
        mainScene = new Scene(mainPane,1250,900);
        primaryStage.setScene(mainScene);
        primaryStage.setFullScreen(false);
        primaryStage.getIcons().add(icon);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
        click.setVolume(0.5);
        music.setAutoPlay(true);
        music.setVolume(0.05);
        music.play();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
