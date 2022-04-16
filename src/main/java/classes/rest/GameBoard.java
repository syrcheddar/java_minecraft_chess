package classes.rest;

import classes.pieces.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



public class GameBoard {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
    private static final Logger LOG = Logger.getLogger(GameBoard.class.getName());
    private static GridPane squares = new GridPane();
    public static boolean browsing = false;
    public static GridPane showedBoard = new GridPane();
    public static boolean whosOnMove = true;
    public static String winner = "";
    public static Square[] board = new Square[64];
    public static int lastMove = 0;
    public static boolean gameEnded = false;
    public static void setLogGB(){
        try {
            LOG.addHandler(new FileHandler("GB_"+dtf.format(LocalDateTime.now())+".log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeMove(){
        if (whosOnMove){
            whosOnMove=false;
        } else {
            whosOnMove=true;
        }
    }

    /**
     * Initializes game board without Pieces
     */
    public static void initBoardOnly() {
        LOG.log(Level.INFO, dtf.format(LocalDateTime.now()) + ": initializing board...");
        //reset counter for 50moves draw and counter for number of round
        Game.moveCounter = 0;
        Game.moveCounter2 = 0;
        whosOnMove = true;
        //creates array of squares, the game board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[(i * 8) + j] = new Square();
                board[(i * 8) + j].coord1 = i;
                board[(i * 8) + j].coord2 = j;
            }
        }
    }
    /**
     * Initializes game board with Pieces in their basic positions.
     */

    public static void initBoard(){
        initBoardOnly();
        //creates black pawns
        for (int i = 8; i < 16; i++) {
            board[i].piece = new Pawn();
            board[i].piece.posX=i-8;
            board[i].piece.posY=1;
            board[i].contains=true;
            board[i].piece.color = "black";
        }
        //creates white pawns
        for (int i = 48; i < 56; i++) {
            board[i].piece = new Pawn();
            board[i].piece.posX=i-48;
            board[i].piece.posY=6;
            board[i].contains=true;
            board[i].piece.color = "white";
        }
        //creates rest of pieces on their basic position
        board[0].piece = new Rook();
        board[0].contains=true;
        board[0].piece.color = "black";

        board[7].piece = new Rook();
        board[7].piece.posX=7;
        board[7].contains=true;
        board[7].piece.color = "black";

        board[63].piece = new Rook();
        board[63].piece.posX=7;
        board[63].piece.posY=7;
        board[63].contains=true;
        board[63].piece.color = "white";

        board[56].piece = new Rook();
        board[56].piece.posY=7;
        board[56].contains=true;
        board[56].piece.color = "white";

        board[1].piece = new Knight();
        board[1].piece.posX=1;
        board[1].contains=true;
        board[1].piece.color = "black";

        board[6].piece = new Knight();
        board[6].piece.posX=6;
        board[6].contains=true;
        board[6].piece.color = "black";

        board[62].piece = new Knight();
        board[62].piece.posX=6;
        board[62].piece.posY=7;
        board[62].contains=true;
        board[62].piece.color = "white";

        board[57].piece = new Knight();
        board[57].piece.posX=1;
        board[57].piece.posY=7;
        board[57].contains=true;
        board[57].piece.color = "white";

        board[2].piece = new Bishop();
        board[2].piece.posX=2;
        board[2].contains=true;
        board[2].piece.color = "black";

        board[5].piece = new Bishop();
        board[5].piece.posX=5;
        board[5].contains=true;
        board[5].piece.color = "black";

        board[61].piece = new Bishop();
        board[61].piece.posX=5;
        board[61].piece.posY=7;
        board[61].contains=true;
        board[61].piece.color = "white";

        board[58].piece = new Bishop();
        board[58].piece.posX=2;
        board[58].piece.posY=7;
        board[58].contains=true;
        board[58].piece.color = "white";

        board[3].piece = new Queen();
        board[3].piece.posX=3;
        board[3].contains=true;
        board[3].piece.color = "black";

        board[59].piece = new Queen();
        board[59].piece.posX=3;
        board[59].piece.posY=7;
        board[59].contains=true;
        board[59].piece.color = "white";

        board[4].piece = new King();
        board[4].piece.posX=4;
        board[4].contains=true;
        board[4].piece.color = "black";

        board[60].piece = new King();
        board[60].piece.posX=4;
        board[60].piece.posY=7;
        board[60].contains=true;
        board[60].piece.color = "white";
    }

    /**
     * Makes current scene actual position of pieces on board.
     * @return Returns GridPane of this board.
     */
    public static GridPane showBoard(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": updating board");
        //moving is array of indexes to move piece, if is empty, first is -1, else 1st and 2nd is starting position and 3rd and 4th is ending position
        int[] moving =new int[4];
        moving[0]=-1;
        //showing board to player
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle square = new Rectangle();
                squares.setGridLinesVisible(true);
                square.setHeight(100);
                square.setWidth(100);
                ImagePattern fill;
                if (((i*7)+j)%2 ==0) {
                    board[(i*8)+j].color="white";
                    fill = new ImagePattern(new Image("file:src/main/imgs/dirt.jpg", 100, 100, true, false));
                } else {
                    board[(i*8)+j].color="black";
                    fill = new ImagePattern(new Image("file:src/main/imgs/grass.png", 100, 100, true, false));
                }
                square.setFill(fill);
                squares.add(square,j,i);
                squares.setAlignment(Pos.CENTER);
                squares.setBackground(new Background(Main.gameBG));
            }
        }
        //showing pieces on board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //gets piece from array of board
                if (board[(i * 8) + j].contains){
                    if (board[(i * 8) + j].piece.color.equals("white")){
                        squares.add(new ImageView(board[(i * 8) + j].piece.getWhite()),j,i);
                    }
                    if (board[(i * 8) + j].piece.color.equals("black")){
                        squares.add(new ImageView(board[(i * 8) + j].piece.getBlack()),j,i);
                    }
                }
                //if player is playing and not only browsing ended game
                if (!gameEnded&&!browsing) {
                    //creates button for position of square on board
                    Button button = new Button();
                    button.setPrefSize(100, 100);
                    button.setStyle("-fx-background-color: rgba(0,0,0,0)");
                    int finalI = i;
                    int finalJ = j;
                    button.setOnAction(e -> {
                        if ((board[(finalI * 8) + finalJ].contains || moving[0] != -1)) {
                            //if white is on move, only white can be classes.rest.Main.clicked
                            if (moving[0] != -1 || ((board[(finalI * 8) + finalJ].piece.color.equals("white") && whosOnMove) || (board[(finalI * 8) + finalJ].piece.color.equals("black") && !whosOnMove))) {
                                Main.click.stop();
                                Main.click.play();
                                if (button.getStyle().equals("-fx-background-color: rgba(0,0,0,0.2)")) {
                                    button.setStyle("-fx-background-color: rgba(0,0,0,0)");
                                    moving[0] = -1;

                                } else {
                                    button.setStyle("-fx-background-color: rgba(0,0,0,0.2)");
                                    if (moving[0] == -1) {
                                        moving[0] = finalJ;
                                        moving[1] = finalI;
                                    } else {
                                        moving[2] = finalJ;
                                        moving[3] = finalI;
                                        board[(moving[1] * 8) + moving[0]].piece.movePiece(moving[2], moving[3]);
                                    }
                                }
                            }
                        }
                    });
                    squares.add(button, j, i);
                }
                for(Square currSquare:board) {
                    if (currSquare.contains) {
                        //promotion choose for promoted pawn
                        if (currSquare.piece.promotion) {
                            if (currSquare.piece.color.equals("black")) {
                                Button queen = new Button();
                                queen.setGraphic(new ImageView(Queen.blackIc));
                                Button bishop = new Button();
                                bishop.setGraphic(new ImageView(Bishop.blackIc));
                                Button rook = new Button();
                                rook.setGraphic(new ImageView(Rook.blackIc));
                                Button knight = new Button();
                                knight.setGraphic(new ImageView(Knight.blackIc));
                                queen.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Queen();
                                    currSquare.piece.color = "black";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=Q ";
                                    refreshBoard();
                                });
                                bishop.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Bishop();
                                    currSquare.piece.color = "black";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=B ";
                                    refreshBoard();
                                });
                                rook.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Rook();
                                    currSquare.piece.color = "black";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=R ";
                                    refreshBoard();
                                });
                                knight.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Knight();
                                    currSquare.piece.color = "black";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=N ";
                                    refreshBoard();
                                });
                                squares.add(queen,2,3);
                                squares.add(bishop,3,3);
                                squares.add(rook,4,3);
                                squares.add(knight,5,3);
                                break;
                            } else {
                                Button queen = new Button();
                                queen.setGraphic(new ImageView(Queen.whiteIc));
                                queen.setMaxSize(100,100);
                                Button bishop = new Button();
                                bishop.setGraphic(new ImageView(Bishop.whiteIc));
                                bishop.setMaxSize(100,100);
                                Button rook = new Button();
                                rook.setGraphic(new ImageView(Rook.whiteIc));
                                rook.setMaxSize(100,100);
                                Button knight = new Button();
                                knight.setGraphic(new ImageView(Knight.whiteIc));
                                knight.setMaxSize(100,100);
                                queen.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Queen();
                                    currSquare.piece.color = "white";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=Q ";
                                    refreshBoard();
                                });
                                bishop.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Bishop();
                                    currSquare.piece.color = "white";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=B ";
                                    refreshBoard();
                                });
                                rook.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Rook();
                                    currSquare.piece.color = "white";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=R ";
                                    refreshBoard();
                                });
                                knight.setOnAction(e -> {
                                    Main.click.stop();
                                    Main.click.play();
                                    currSquare.piece = new Knight();
                                    currSquare.piece.color = "white";
                                    currSquare.piece.posX = currSquare.coord2;
                                    currSquare.piece.posY = currSquare.coord1;
                                    GameSaveLoad.gameData=GameSaveLoad.gameData.substring(0,GameSaveLoad.gameData.length()-1)+"=N ";
                                    refreshBoard();
                                });
                                squares.add(queen,2,3);
                                squares.add(bishop,3,3);
                                squares.add(rook,4,3);
                                squares.add(knight,5,3);
                            }
                        }
                    }
                }
            }
        }
        //checking if the game ended
        winner = Game.isItOver();
        if (!browsing) {
            //game is draw
            if (IsCheck.kingVsKing() || Game.moveCounter >= 50 || !isAbleToPlay()) {
                Label label = new Label("Draw");
                gameEnded = true;
                HBox hBox=new HBox();
                GameSaveLoad.result = "[Result '1/2-1/2']\n ";
                GameSaveLoad.saveData();
                label.setTextFill(Color.WHITE);
                label.setBackground(new Background(Main.gameBG));
                label.setFont(new Font("Calibri",18));
                hBox.getChildren().add(label);
                hBox.setAlignment(Pos.CENTER);
                hBox.setBackground(new Background(Main.bedrockBG));
                hBox.setSpacing(10);
                Main.mainPane.setBottom(hBox);
                return squares;
                //somebody won
            } else if (winner.equals("white") || winner.equals("black")) {
                Label label = new Label(winner + " wins");
                gameEnded = true;
                GameSaveLoad.gameData = GameSaveLoad.gameData.substring(0, GameSaveLoad.gameData.length() - 2) + "# ";
                //saving result of game
                if (winner.equals("white")) {
                    GameSaveLoad.result = "[Result '1-0']\n ";
                    GameSaveLoad.gameData += "1-0";
                } else {
                    GameSaveLoad.result = "[Result '0-1']\n ";
                    GameSaveLoad.gameData += "0-1";
                }
                label.setTextFill(Color.WHITE);
                label.setBackground(new Background(Main.gameBG));
                label.setFont(new Font("Calibri",18));
                HBox hBox=new HBox();
                hBox.getChildren().add(label);
                hBox.setAlignment(Pos.CENTER);
                hBox.setBackground(new Background(Main.bedrockBG));
                hBox.setSpacing(10);
                Main.mainPane.setBottom(hBox);
                GameSaveLoad.saveData();
                return squares;
            }else { gameEnded=false;}
        }
        //if one of players is robot, the robot move is made
        Game.robotPlays();
        return squares;
    }

    /**
     * Creates GridPane of current board without buttons to move.
     * @return Returns GridPane board
     */
    public static GridPane createGridPane(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": loading board with move");
        GridPane squares = new GridPane();
        int[] moving =new int[4];
        moving[0]=-1;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle square = new Rectangle();
                squares.setGridLinesVisible(true);
                square.setHeight(100);
                square.setWidth(100);
                ImagePattern fill;
                if (((i*7)+j)%2 ==0) {
                    board[(i*8)+j].color="white";
                    fill = new ImagePattern(new Image("file:src/main/imgs/dirt.jpg", 100, 100, true, false));
                } else {
                    board[(i*8)+j].color="black";
                    fill = new ImagePattern(new Image("file:src/main/imgs/grass.png", 100, 100, true, false));
                }
                square.setFill(fill);
                squares.add(square,j,i);
                squares.setAlignment(Pos.CENTER);
                squares.setStyle("-fx-background-color: rgb(0,0,0,0)");
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[(i * 8) + j].contains){
                    if (board[(i * 8) + j].piece.color.equals("white")){
                        squares.add(new ImageView(board[(i * 8) + j].piece.getWhite()),j,i);
                    }
                    if (board[(i * 8) + j].piece.color.equals("black")){
                        squares.add(new ImageView(board[(i * 8) + j].piece.getBlack()),j,i);
                    }
                }
            }
        }
        return squares;
    }
    public static void refreshBoard(){
        showedBoard = showBoard();
    }

    /**
     * Finds all White pieces on board.
     * @return Returns String of them.
     */
    public static Piece[] getAllWhites(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for white pieces");
        //searches board for white pieces
        ArrayList<Piece> allWhites = new ArrayList<>();
        for (Square currSquare:board){
            if (currSquare.contains&&currSquare.piece.color.equals("white")){
                allWhites.add(currSquare.piece);
            }
        }
        Piece[] allWhitesArray = new Piece[allWhites.size()];
        int i=0;
        for (Piece currPiece :allWhites){
            allWhitesArray[i]=currPiece;
            i++;
        }
        return allWhitesArray;
    }

    /**
     * Finds all black pieces on board.
     * @return Returns String of them.
     */
    public static Piece[] getAllBlacks(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for black pieces");
        //searches board for black pieces
        ArrayList<Piece> allBlacks = new ArrayList<>();
        for (Square currSquare:board){
            if (currSquare.contains&&currSquare.piece.color.equals("black")){
                allBlacks.add(currSquare.piece);
            }
        }
        Piece[] allBlacksArray = new Piece[allBlacks.size()];
        int i=0;
        for (Piece currPiece :allBlacks){
            allBlacksArray[i]=currPiece;
            i++;
        }
        return allBlacksArray;
    }

    /**
     * Finds all White pieces on board that are able of moving.
     * @return Returns String of them.
     */
    public static Piece[] getWhiteAbleMoving(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for white pieces able of moving");
        Piece[] allWhites = getAllWhites();
        //tries to move every white piece and returns array made of them
        ArrayList<Piece> allWhiteAble = new ArrayList<>();
        for (Piece currPiece :allWhites){
            for (Square currSquare : board){
                if((currPiece.getName().equals("K")&&currSquare.contains&&currSquare.piece.getName().equals("R"))||(currPiece.getName().equals("R")&&currSquare.contains&&currSquare.piece.getName().equals("K"))) {
                } else {
                    if (currPiece.tryMovePiece(currSquare.coord2, currSquare.coord1)) {
                        allWhiteAble.add(currPiece);
                        break;
                    }
                }
            }
        }
        Piece[] finalWhites = new Piece[allWhiteAble.size()];
        int i=0;
        for (Piece currPiece : allWhiteAble){
            finalWhites[i]= currPiece;
            i++;
        }
        return finalWhites;
    }

    /**
     * Finds all White pieces on board that are able of moving and their name is equals to parameter.
     * @param name Name of the wanted piece
     * @return Returns String of them.
     */
    public static Piece[] getWhiteAbleMoving(String name){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for white pieces able of moving named "+name);
        Piece[] allWhites = getAllWhites();
        //tries to move every white piece and returns array made of them
        ArrayList<Piece> allWhiteAble = new ArrayList<>();
        for (Piece currPiece :allWhites){
            for (Square currSquare : board){
                if((currPiece.getName().equals("K")&&currSquare.contains&&currSquare.piece.getName().equals("R"))||(currPiece.getName().equals("R")&&currSquare.contains&&currSquare.piece.getName().equals("K"))) {
                } else {
                    if (currPiece.getName().equals(name)&&currPiece.tryMovePiece(currSquare.coord2, currSquare.coord1)) {
                        allWhiteAble.add(currPiece);
                        break;
                    }
                }
            }
        }
        Piece[] finalWhites = new Piece[allWhiteAble.size()];
        int i=0;
        for (Piece currPiece : allWhiteAble){
            finalWhites[i]= currPiece;
            i++;
        }
        return finalWhites;
    }

    /**
     * Finds all White pieces on board that are able of moving.
     * @return Returns String of them.
     */
    public static Piece[] getBlackAbleMoving(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for black pieces able of moving");
        Piece[] allBlacks = getAllBlacks();
        //tries to move every black piece and returns array made of them
        ArrayList<Piece> allBlackAble = new ArrayList<>();
        for (Piece currPiece :allBlacks){
            for (Square currSquare : board){
                if((currPiece.getName().equals("K")&&currSquare.contains&&currSquare.piece.getName().equals("R"))||(currPiece.getName().equals("R")&&currSquare.contains&&currSquare.piece.getName().equals("K"))) {
                } else {
                    if (currPiece.tryMovePiece(currSquare.coord2, currSquare.coord1)) {
                        allBlackAble.add(currPiece);
                        break;

                    }
                }
            }
        }
        Piece[] finalBlacks = new Piece[allBlackAble.size()];
        int i=0;
        for (Piece currPiece : allBlackAble){
            finalBlacks[i]= currPiece;
            i++;
        }
        return finalBlacks;
    }
    /**
     * Finds all black pieces on board that are able of moving and their name is equals to parameter.
     * @param name Name of the wanted piece
     * @return Returns String of them.
     */
    public static Piece[] getBlackAbleMoving(String name){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for white pieces able of moving named "+name);
        Piece[] allBlacks = getAllBlacks();
        //tries to move every white piece and returns array made of them
        ArrayList<Piece> allBlackAble = new ArrayList<>();
        for (Piece currPiece :allBlacks){
            for (Square currSquare : board){
                if((currPiece.getName().equals("K")&&currSquare.contains&&currSquare.piece.getName().equals("R"))||(currPiece.getName().equals("R")&&currSquare.contains&&currSquare.piece.getName().equals("K"))) {
                } else {
                    if (currPiece.getName().equals(name)&&currPiece.tryMovePiece(currSquare.coord2, currSquare.coord1)) {
                        allBlackAble.add(currPiece);
                        break;
                    }
                }
            }
        }
        Piece[] finalWhites = new Piece[allBlackAble.size()];
        int i=0;
        for (Piece currPiece : allBlackAble){
            finalWhites[i]= currPiece;
            i++;
        }
        return finalWhites;
    }

    /**
     *
     * Checks if both players are able of moving any their pieces
     * @return Returns True if yes, false if not.
     */
    public static boolean isAbleToPlay(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checking for ability of playing");
        //checks if is anybody unable to play
        if ((whosOnMove&&getWhiteAbleMoving().length==0)||(!whosOnMove&&getBlackAbleMoving().length==0)){
            return false;
        }
        return true;
    }

}