package classes.rest;

import classes.pieces.Piece;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Game {
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
    private static final Logger LOG = Logger.getLogger(GameBoard.class.getName());
    public static Player player1 = new Player();
    public static Player player2 = new Player();
    public static int moveCounter =0;
    public static int moveCounter2 =0;
    static Label timeWhite = null;
    static Label timeBlack = null;

    /**
     * Starts new game with new board
     */
    public static void startGame(){
        GameBoard.winner="";
        GameBoard.browsing=false;
        //game settings
        player1.isHuman = true;
        player1.color = "white";
        player2.color = "black";
        //creates new game
        GameBoard.initBoard();
        //shows gameboard
        GameBoard.refreshBoard();
        Thread timer = new Thread() {
            public void run() {
                Clock.moveTime();
            }
        };
        //layout buttons and their functions
        Button exit = new Button("Exit to main menu");
        Button save = new Button("Save game");
        HBox hbox = new HBox();
        save.setOnAction(e->{
            if (!GameBoard.whosOnMove){
                hbox.getChildren().add(new Label("You have to finish round first!"));
            } else {
                Main.click.stop();
                Main.click.play();
                LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": saving game...");
                GameSaveLoad.saveData();
                GameBoard.browsing = false;
                GameSaveLoad.loadedGame = null;
                Main.mainPane.setTop(null);
                Main.mainPane.setLeft(null);
                Main.mainPane.setRight(null);
                HBox hBox = new HBox();
                Button button = new Button("Save logs");
                button.setOnAction(eb->{Game.saveLogs();});
                hBox.getChildren().add(button);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                Main.mainPane.setCenter(MainMenu.menus());
                Main.mainPane.setBottom(hBox);
                Main.mainPane.setCenter(MainMenu.menus());
                timer.stop();
            }
        });
        exit.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": leaving game...");
            GameBoard.browsing=false;
            Main.mainPane.setTop(null);
            Main.mainPane.setLeft(null);
            Main.mainPane.setRight(null);
            HBox hBox = new HBox();
            Button button = new Button("Save logs");
            button.setOnAction(eb->{Game.saveLogs();});
            hBox.getChildren().add(button);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            Main.mainPane.setCenter(MainMenu.menus());
            Main.mainPane.setBottom(hBox);
            Main.mainPane.setCenter(MainMenu.menus());
            timer.stop();
        });
        hbox.getChildren().add(exit);
        hbox.getChildren().add(save);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMinHeight(50);
        hbox.setSpacing(10);
        //setting main scene to game scene
        Main.mainPane.setTop(hbox);
        Main.mainPane.setCenter(GameBoard.showedBoard);
        VBox vBox=new VBox();
        vBox.getChildren().add(new ImageView(new Image("file:src/main/imgs/guide.png", 376, 603, true, false)));
        vBox.setStyle("-fx-background-color: rgb(0,0,0,0)");
        vBox.setAlignment(Pos.CENTER_RIGHT);
        Main.mainPane.setBackground(new Background(Main.gameBG));
        Main.mainPane.setLeft(vBox);
        if (Clock.timerWhite!=null){
            showTimer();
        }
        timer.start();
    }

    /**
     * Method for showing Chess clock
     */
    public static void showTimer() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
        HBox timers = new HBox();
        Label timeWhite = new Label(dtf.format(Clock.timerWhite));
        Label timeBlack = new Label(dtf.format(Clock.timerBlack));
        timeWhite.setFont(new Font("Calibri",18));
        timeWhite.setTextFill(Color.WHITE);
        timeWhite.setBackground(new Background(Main.gameBG));
        timeBlack.setFont(new Font("Calibri",18));
        timeBlack.setBackground(new Background(Main.gameBG));
        timeBlack.setTextFill(Color.WHITE);
        timers.getChildren().add(timeWhite);
        timers.getChildren().add(timeBlack);
        timers.setAlignment(Pos.CENTER);
        timers.setBackground(new Background(Main.bedrockBG));
        timers.setSpacing(10);
        Main.mainPane.setBottom(timers);
    }

    /**
     * Saves Logs
     */
    public static void saveLogs(){
        GameBoard.setLogGB();
        setLogG();
        IsCheck.setLogICh();
        GameSaveLoad.setLogGSL();
    }
    public static void setLogG(){
        try {
            LOG.addHandler(new FileHandler("G_"+dtf.format(LocalDateTime.now())+".log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the game ended or not, if its Checkmate on any side
     * @return Returns String "white" in case of white side winning and String "black" in case of black side winning
     */
    public static String isItOver(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checking for end of game");
        if (timeWhite!=null) {
            if (dtf.format(Clock.timerBlack).equals(dtf.format(Clock.endTime))) {
                return "white";
            }
            if (dtf.format(Clock.timerBlack).equals(dtf.format(Clock.endTime))) {
                return "black";
            }
        }
        if (IsCheck.isCheckWhite()){
            boolean checkmate = true;
            //trying to prevent checkmate by moving any white piece
            for (Square currSquare : GameBoard.board) {
                if (currSquare.contains && currSquare.piece.color.equals("white")) {
                    for (Square trySquare : GameBoard.board) {
                        if (currSquare.piece.tryMovePiece(trySquare.coord2, trySquare.coord1)) {
                            int x = trySquare.coord2;
                            int y = trySquare.coord1;
                            Piece tempPiece = null;
                            if (GameBoard.board[(y * 8) + x].contains) {
                                tempPiece = GameBoard.board[(y * 8) + x].piece;
                            }
                            GameBoard.board[(y * 8) + x].contains = true;
                            GameBoard.board[(y * 8) + x].piece = currSquare.piece;
                            currSquare.contains = false;
                            currSquare.piece = null;
                            //is still check after moving this piece?
                            if (!IsCheck.isCheckWhite()) {
                                if (tempPiece == null) {
                                    GameBoard.board[(y * 8) + x].contains = false;
                                }
                                //all returns back before the move and game continues
                                currSquare.piece = GameBoard.board[(y * 8) + x].piece;
                                GameBoard.board[(y * 8) + x].piece = tempPiece;
                                currSquare.contains = true;
                                checkmate = false;
                            } else {
                                //all return back before the move, its checkmate
                                if (tempPiece == null) {
                                    GameBoard.board[(y * 8) + x].contains = false;
                                }
                                currSquare.piece = GameBoard.board[(y * 8) + x].piece;
                                GameBoard.board[(y * 8) + x].piece = tempPiece;
                                currSquare.contains = true;
                            }
                        }
                    }
                }
            }
            if (checkmate) {
                LOG.log(Level.INFO, dtf.format(LocalDateTime.now()) + ": checkmate white");
                GameSaveLoad.result = "[Result '0-1']\n \n";
                return "black";
            }
        } else if (IsCheck.isCheckBlack()) {
            boolean checkmate = true;
            //trying to prevent checkmate by moving any white piece
            for (Square currSquare : GameBoard.board) {
                if (currSquare.contains && currSquare.piece.color.equals("black")) {
                    for (Square trySquare : GameBoard.board) {
                        if (currSquare.piece.tryMovePiece(trySquare.coord2, trySquare.coord1)) {
                            int x = trySquare.coord2;
                            int y = trySquare.coord1;
                            Piece tempPiece = null;
                            if (GameBoard.board[(y * 8) + x].contains) {
                                tempPiece = GameBoard.board[(y * 8) + x].piece;
                            }
                            GameBoard.board[(y * 8) + x].contains = true;
                            GameBoard.board[(y * 8) + x].piece = currSquare.piece;
                            currSquare.contains = false;
                            currSquare.piece = null;
                            //is still check after moving this piece?
                            if (!IsCheck.isCheckBlack()) {
                                if (tempPiece == null) {
                                    GameBoard.board[(y * 8) + x].contains = false;
                                }
                                //all returns back before the move and game continues
                                currSquare.piece = GameBoard.board[(y * 8) + x].piece;
                                GameBoard.board[(y * 8) + x].piece = tempPiece;
                                currSquare.contains = true;
                                checkmate = false;
                            } else {
                                if (tempPiece == null) {
                                    GameBoard.board[(y * 8) + x].contains = false;
                                }
                                //all returns back before the move, its checkmate
                                currSquare.piece = GameBoard.board[(y * 8) + x].piece;
                                GameBoard.board[(y * 8) + x].piece = tempPiece;
                                currSquare.contains = true;
                            }
                        }
                    }
                }
            }
            if (checkmate){
                LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checkmate black");
                GameSaveLoad.result = "[Result '1-0']\n \n";
                return "white";
            }
        }
        return "";
    }

    /**
     * Shows option to choose Opponent
     */
    public static void choseOpponent(){
        Button robot = new Button("I want to play against robot");
        Button player = new Button("I want to play against player");
        Button returning = new Button("Return");
        Label chess = new Label();
        chess.setText("Chess");
        chess.setFont(new Font("Arial",60));

        returning.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            Main.mainPane.setCenter(MainMenu.menus());
        });
        player.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,Clock.dtf.format(LocalDateTime.now())+": player chosen");
            player2.isHuman=true;
            chooseTime();
        });

        robot.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,Clock.dtf.format(LocalDateTime.now())+": robot chosen");
            player2.isHuman=false;
            chooseTime();
        });
        VBox vBox = new VBox();
        vBox.setMinWidth(200);
        vBox.getChildren().add(chess);
        vBox.getChildren().add(player);
        vBox.getChildren().add(robot);
        vBox.getChildren().add(returning);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Main.mainPane.setCenter(vBox);
    }

    /**
     * Shows option to choose timer
     */
    public static void chooseTime(){
        Button noTime = new Button("I dont want to use Chess clock");
        Button time30 = new Button("I want to set time on 30 minutes");
        Button time20 = new Button("I want to set time on 20 minutes");
        Button time10 = new Button("I want to set time on 10 minutes");
        Button time5 = new Button("I want to set time on 5 minutes");
        Button returning = new Button("Return");
        Label chess = new Label();
        chess.setText("Chess");
        chess.setFont(new Font("Arial",60));

        returning.setOnAction(e->{
            Main.mainPane.setCenter(MainMenu.menus());
        });
        noTime.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": no clock chosen");
            Clock.setTime(-1);
            startGame();
        });

        time30.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": 30 minutes chosen");
            Clock.setTime(30);
            startGame();
        });
        time20.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": 20 minutes chosen");
            Clock.setTime(20);
            startGame();
        });
        time10.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": 10 minutes chosen");
            Clock.setTime(10);
            startGame();
        });
        time5.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": 5 minutes chosen");
            Clock.setTime(5);
            startGame();
        });
        VBox vBox = new VBox();
        vBox.setMinWidth(200);
        vBox.getChildren().add(chess);
        vBox.getChildren().add(noTime);
        vBox.getChildren().add(time5);
        vBox.getChildren().add(time10);
        vBox.getChildren().add(time20);
        vBox.getChildren().add(time30);
        vBox.getChildren().add(returning);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Main.mainPane.setCenter(vBox);
    }

    /**
     * Automatically makes move for a robot player
     */
    public static void robotPlays(){
        if (!player1.isHuman&&(GameBoard.whosOnMove)){
            Piece[] pieces =GameBoard.getWhiteAbleMoving();
            Random rand = new Random();
            //randomly picks piece to move
            int chosenOne = rand.nextInt(pieces.length);
            ArrayList<Square> possibleSquares = new ArrayList<>();
            //gets all possible moves for chosen piece
            for (Square currSquare: GameBoard.board) {
                if (pieces[chosenOne].tryMovePiece(currSquare.coord2,currSquare.coord1)){
                    possibleSquares.add(currSquare);
                }
            }
            Square[] possibleSqrs = new Square[possibleSquares.size()];
            int i=0;
            for (Square currSqr :possibleSquares){
                possibleSqrs[i]=currSqr;
                i++;
            }
            //randomly picks move for the piece
            int chosenMove = rand.nextInt(possibleSqrs.length);
            pieces[chosenOne].movePiece(possibleSqrs[chosenMove].coord2,possibleSqrs[chosenMove].coord1);
        }
        if (!player2.isHuman&&(!GameBoard.whosOnMove)){
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": robot plays");
            Piece[] pieces =GameBoard.getBlackAbleMoving();
            Random rand = new Random();
            //randomly picks piece to move
            int chosenOne = rand.nextInt(pieces.length);
            ArrayList<Square> possibleSquares = new ArrayList<>();
            //gets all possible moves for chosen piece
            for (Square currSquare: GameBoard.board) {
                if (pieces[chosenOne].tryMovePiece(currSquare.coord2,currSquare.coord1)){
                    possibleSquares.add(currSquare);
                }
            }
            Square[] possibleSqrs = new Square[possibleSquares.size()];
            int i=0;
            for (Square currSqr :possibleSquares){
                possibleSqrs[i]=currSqr;
                i++;
            }
            //randomly picks move for the piece
            int chosenMove = rand.nextInt(possibleSqrs.length);
            pieces[chosenOne].movePiece(possibleSqrs[chosenMove].coord2,possibleSqrs[chosenMove].coord1);
        }
    }
}
