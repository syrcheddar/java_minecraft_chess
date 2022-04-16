package classes.rest;

import classes.pieces.*;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameSaveLoad {
    private static DateTimeFormatter dtflog = DateTimeFormatter.ofPattern("dd_MM_YYYY_HH_mm_ss");
    private static final Logger LOG = Logger.getLogger(GameBoard.class.getName());
    static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddMMyyyyhhmmss");
    static LocalDateTime now = LocalDateTime.now();
    public static String[] loadedGame = null;
    static int index =0;
    static String loadedGameName = "";
    static String eventTag = "[Event 'Casual game'] \n";
    static String site = "[Site 'Your Computer']\n";
    static String date = "";
    static String round = "[Round '?']\n";
    static String white = "[White '?']\n";
    static String black = "[White '?']\n";
    static String result = "[Result '*']\n";
    static String gameInfo = "";
    public static String gameData = "\n";
    static String dataToSave = "";

    /**
     * Function for saving data to file.
     */
    public static void saveData(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": saving data");
        date = "[Date '"+ dtf.format(now) +"']\n";
        String name = "";
        if (!loadedGameName.equals("")){
            name=loadedGameName.substring(0, 14);
        }
        else {
            name = dtf.format(now);
        }
        gameInfo=eventTag+site+round+white+black;
        dataToSave=gameInfo+result+gameData;
        try (PrintWriter out = new PrintWriter("src/main/saves/"+name+".pgn")){
            out.println(dataToSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        gameData = "\n";
    }

    /**
     * Loads a game and lets player play.
     * @param gameName String name of a game
     */
    public static void loadGame(String gameName){
        GameBoard.winner="";
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": loading game");
        loadAllMoves(gameName);
        Game.player1.isHuman = true;
        Game.player1.color = "white";
        Game.player2.isHuman = true;
        Game.player2.color = "black";
        GameBoard.initBoard();
        for (int i = 0; i < loadedGame.length; i++) {
            initMove(i);
        }
        GameBoard.refreshBoard();
        Button exit = new Button("Exit to main menu");
        Button save = new Button("Save game");
        HBox hbox = new HBox();
        GameBoard.whosOnMove=true;
        save.setOnAction(e->{
            if (!GameBoard.whosOnMove){
                Main.click.stop();
                Main.click.play();
                hbox.getChildren().add(new Label("You have to finish round first!"));
            } else{
                Main.click.stop();
                Main.click.play();
                saveData();
                GameBoard.browsing = false;
                loadedGame = null;
                Main.mainPane.setTop(null);
                Main.mainPane.setLeft(null);
                Main.mainPane.setRight(null);
                Main.mainPane.setBottom(null);
                Main.mainPane.setCenter(MainMenu.menus());
            }
        });
        exit.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            GameBoard.browsing=false;
            loadedGame=null;
            Main.mainPane.setTop(null);
            Main.mainPane.setLeft(null);
            Main.mainPane.setRight(null);
            HBox hBox = new HBox();
            Button button = new Button("Save logs");
            button.setOnAction(ev->{Game.saveLogs();});
            hBox.getChildren().add(button);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            Main.mainPane.setCenter(MainMenu.menus());
            Main.mainPane.setBottom(hBox);
            Main.mainPane.setCenter(MainMenu.menus());
            dataToSave="";
        });
        hbox.getChildren().add(exit);
        hbox.getChildren().add(save);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMinHeight(50);
        hbox.setSpacing(10);
        Main.mainPane.setTop(hbox);
        VBox vBox=new VBox();
        vBox.getChildren().add(new ImageView(new Image("file:src/main/imgs/guide.png", 376, 603, true, false)));
        vBox.setStyle("-fx-background-color: rgb(0,0,0,0)");
        vBox.setAlignment(Pos.CENTER);
        Main.mainPane.setBackground(new Background(Main.gameBG));
        Main.mainPane.setLeft(vBox);
        Main.mainPane.setCenter(GameBoard.showedBoard);
        Main.mainPane.setRight(null);
    }
    public static void setLogGSL(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        try {
            LOG.addHandler(new FileHandler("GSL_"+dtf.format(LocalDateTime.now())+".log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows a option to choose game to load
     */
    public static void choseGameToContinue(){
        String[] games = getAllSaves();
        ListView listView = new ListView();
        for (String game : games){
            listView.getItems().add(game.substring(0,2)+"."+game.substring(2,4)+"."+game.substring(4,8)+" "+game.substring(8,10)+":"+game.substring(10,12)+":"+game.substring(12,14));
        }

        Button button = new Button("Continue this game");
        Button returning = new Button("Return");
        Label chess = new Label();
        chess.setText("Chess");
        chess.setFont(new Font("Arial",60));

        returning.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            Main.mainPane.setCenter(MainMenu.menus());
        });

        button.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": game selected to load");
            ObservableList selectedIndices = listView.getSelectionModel().getSelectedIndices();
            String gameName ="";
            for(Object o : selectedIndices){
                gameName = listView.getItems().get(o.hashCode()).toString();
            }

            gameName=gameName.replace(".","");
            gameName=gameName.replace(":","");
            gameName=gameName.replace(" ","");
            gameName+=".pgn";
            loadGame(gameName);
            loadedGameName=gameName;
        });
        VBox vBox = new VBox();
        listView.setMaxWidth(150);
        listView.setMaxHeight(200);
        vBox.setMinWidth(200);
        vBox.getChildren().add(chess);
        vBox.getChildren().add(listView);
        vBox.getChildren().add(button);
        vBox.getChildren().add(returning);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        Main.mainPane.setCenter(vBox);
    }

    /**
     * shows a option to choose game to browse.
     */
    public static void choseGameToBrowse(){
        //shows
        String[] games = getAllEnded();
        ListView listView = new ListView();
        for (String game : games){
            listView.getItems().add(game.substring(0,2)+"."+game.substring(2,4)+"."+game.substring(4,8)+" "+game.substring(8,10)+":"+game.substring(10,12)+":"+game.substring(12,14));
        }

        Button button = new Button("Browse game");
        Button returning = new Button("Return");
        Label chess = new Label();
        chess.setText("Chess");
        chess.setFont(new Font("Arial",60));

        returning.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            Main.mainPane.setCenter(MainMenu.menus());
        });
        button.setOnAction(event -> {
            Main.click.stop();
            Main.click.play();
            LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": game selected to load");
            ObservableList selectedIndices = listView.getSelectionModel().getSelectedIndices();
            String gameName ="";
            for(Object o : selectedIndices){
                gameName = listView.getItems().get(o.hashCode()).toString();
            }

            gameName=gameName.replace(".","");
            gameName=gameName.replace(":","");
            gameName=gameName.replace(" ","");
            gameName+=".pgn";
            browseGame(gameName);
        });
        VBox vBox = new VBox();
        listView.setMaxWidth(150);
        listView.setMaxHeight(200);
        vBox.setMinWidth(200);
        vBox.getChildren().add(chess);
        vBox.getChildren().add(listView);
        vBox.getChildren().add(button);
        vBox.getChildren().add(returning);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        Main.mainPane.setCenter(vBox);
    }

    /**
     * Shows a browsed game
     */
    public static void browseGame(String gameName){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": browsing game");
        //loads all moves
        loadAllMoves(gameName);
        for (String move :loadedGame){
        }
        index =0;
        Game.player1.isHuman = true;
        Game.player1.color = "white";
        Game.player2.isHuman = true;
        Game.player2.color = "black";
        GameBoard.browsing=true;
        GridPane[] browsedMoves = browseMoves();
        HBox hBox = new HBox();
        Button exit = new Button("Exit to main menu");
        exit.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            GameBoard.browsing=false;
            loadedGame=null;
            Main.mainPane.setTop(null);
            Main.mainPane.setLeft(null);
            Main.mainPane.setRight(null);
            HBox hbox = new HBox();
            Button button = new Button("Save logs");
            button.setOnAction(ev->{Game.saveLogs();});
            hbox.getChildren().add(button);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Main.mainPane.setCenter(MainMenu.menus());
            Main.mainPane.setBottom(hbox);
            Main.mainPane.setCenter(MainMenu.menus());
        });
        HBox hbox = new HBox();
        hbox.getChildren().add(exit);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMinHeight(50);
        Main.mainPane.setTop(hbox);
        //creating buttons for browsing game moves
        Button previous = new Button("Previous");
        previous.setDisable(true);
        Button next = new Button("Next");
        previous.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            if (index> 0) {
                if (index==1){
                    previous.setDisable(true);
                }
                if (index==browsedMoves.length-1){
                    next.setDisable(false);
                }
                index -= 1;
                Main.mainPane.setCenter(browsedMoves[index]);
            }
        });
        next.setOnAction(e->{
            Main.click.stop();
            Main.click.play();
            if (index< (browsedMoves).length-1) {
                if (index==0){
                    previous.setDisable(false);
                }
                if (index==browsedMoves.length-2){
                    next.setDisable(true);
                }
                index += 1;
                Main.mainPane.setCenter(browsedMoves[index]);
            }
        });
        hBox.getChildren().add(previous);
        hBox.getChildren().add(next);
        hBox.setAlignment(Pos.CENTER);
        hBox.setBackground(new Background(Main.bedrockBG));
        //setting primary stage to browsing game
        Main.mainPane.setCenter(browsedMoves[index]);
        Main.mainPane.setBottom(hBox);
        Main.mainPane.setRight(null);
        VBox vBox=new VBox();
        vBox.getChildren().add(new ImageView(new Image("file:src/main/imgs/guide.png", 376, 603, true, false)));
        vBox.setStyle("-fx-background-color: rgb(0,0,0,0)");
        vBox.setAlignment(Pos.CENTER);
        Main.mainPane.setBackground(new Background(Main.gameBG));
        Main.mainPane.setLeft(vBox);
    }

    /**
     * Loads all moves from a file named the parameter.
     * @param gameName The name of game wanted to be loaded
     */
    public static void loadAllMoves(String gameName){
        loadedGame=null;
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": loading moves");
        Path path = Path.of("src/main/saves/"+gameName);
        ArrayList<String> moveData = new ArrayList<>();
        boolean eof=false;
        String data ="";
        //gets data from file end turns it to string
        try {
            data = Files.readString(path, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        data= data.replace("\n"," ");
        int i=0;
        while (true) {
            i++;
            //turns data to round segments
            int firstIndex = data.indexOf(i + ". ");
            int secondIndex = data.indexOf((i + 1) + ". ");
            if (i>9){
                firstIndex+=1;
            }
            if (secondIndex==-1){
                Game.moveCounter2=i;
                if (data.contains("0-1")||data.contains("1-0")) {
                    secondIndex = data.length() - 6;
                } else if (data.contains("1/2-1/2")){
                    secondIndex = data.indexOf(" 1/2-1/2");
                } else if (data.contains("*")){
                    secondIndex = data.length() - 2;
                }
                eof=true;
            } else {
                secondIndex-=1;
            }
            String roundSegment = data.substring(firstIndex+3, secondIndex);
            //turns every round segment to game turns white and black
            int whiteIndex = roundSegment.indexOf(" ");
            if (whiteIndex==-1){
                whiteIndex=roundSegment.length();
            }
            String whiteMove = roundSegment.substring(0, whiteIndex);
            String blackMove="";
            if ((roundSegment.length()-whiteIndex)>2) {
                blackMove = roundSegment.substring(whiteIndex + 1, roundSegment.length());
            }
            moveData.add(whiteMove);
            moveData.add(blackMove);
            if (eof){
                break;
            }
        }
        //saves data to class variable loadedGame
        loadedGame= moveData.toArray(new String[0]);
    }

    /**
     * Gets all games in the directory "Saves".
     * @return Returns array of String game names.
     */
    public static String[] getAllGames(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": searching for game saves");
        File dir = new File("src/main/saves");
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.length()==18) {
                    return name.contains(".pgn");
                }
                return false;
            }
        };
        return dir.list(filter);
    }

    /**
     * Checks if the game given in parameter has ended or not.
     * @param save String name of the game
     * @return Returns true if game hase ended, false if not.
     */
    public static boolean didItEnd(String save){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checking if game is saved or ended");
        Path path = Path.of("src/main/saves/"+save);
        ArrayList<String> moveData = new ArrayList<>();
        String data ="";
        try {
            data = Files.readString(path, StandardCharsets.US_ASCII);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data.contains("*")) {
            return false;

        }
        return true;
    }

    /**
     * Finds all Games that did not ended.
     * @return Returns array of String game names.
     */
    public static String[] getAllSaves(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting all saves");
        ArrayList<String> allSaves = new ArrayList<>();
        for (String name : getAllGames()){
            if(!didItEnd(name)){
                allSaves.add(name);
            }
        }
        return allSaves.toArray(String[]::new);
    }
    /**
     * Finds all Games that did ended.
     * @return Returns array of String game names.
     */
    public static String[] getAllEnded(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting all ended games");
        ArrayList<String> allSaves = new ArrayList<>();
        for (String name : getAllGames()){
            if(didItEnd(name)){
                allSaves.add(name);
            }
        }
        return allSaves.toArray(String[]::new);
    }

    /**
     * Function, which initialises move from loaded game.
     * @param index Index of move wanted to be initialised.
     */
    public static void initMove(int index){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": moving piece");
        //inits move in loaded game
        String[] allMoves = loadedGame;
        String move = allMoves[index];
        if (!move.equals("")) {
            //special occurance, when the move is castling
            if (move.equals("O-O")) {
                if (index % 2 == 0) {
                    GameBoard.board[(7 * 8) + 4].piece.movePiece(7, 7);
                } else {
                    GameBoard.board[(0 * 8) + 4].piece.movePiece(7, 0);
                }
            } else if (move.equals("O-O-O")) {
                if (index % 2 == 0) {
                    GameBoard.board[(7 * 8) + 4].piece.movePiece(0, 7);
                } else {
                    GameBoard.board[(0 * 8) + 4].piece.movePiece(0, 0);
                }
            } else {
                int firstX = 0;
                String chosenOneX = "";
                //gets piecename
                String pieceName = getPieceName(allMoves[index]);
                //removes all for me unimportant things from pgn formated move
                if (move.contains("#")) {
                    move = move.replace("#", "");
                }
                if (move.contains("+")) {
                    move = move.replace("+", "");
                }
                move = move.replace(pieceName, "");
                if (move.contains("x")) {
                    if (move.indexOf("x") != 0) {
                        chosenOneX = move.charAt(0) + "";
                        move = move.replace(chosenOneX, "");
                    }
                    move = move.replace("x", "");
                }
                if (Character.isAlphabetic(move.charAt(1))){
                    chosenOneX= move.charAt(0)+"";
                    move = move.replace(chosenOneX, "");
                }
                if (!chosenOneX.equals("")) {
                    firstX = switchCoords(chosenOneX, "");
                }
                int finalX = switchCoords(move.charAt(0) + "", "");
                int finalY = switchCoords("", move.charAt(1) + "");
                //it means that the move is white
                if (index % 2 == 0) {
                    for (Piece piece : GameBoard.getWhiteAbleMoving(pieceName)) {
                        if (!chosenOneX.equals("") && piece.posX == firstX) {
                            //move for promotion
                            if (move.contains("=")) {
                                GameBoard.board[(piece.posY * 8) + piece.posX].contains = false;
                                GameBoard.board[(finalY * 8) + finalX].contains = true;
                                if (move.contains("Q")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Queen();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("B")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Bishop();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("N")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Knight();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("R")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Rook();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                GameBoard.board[(piece.posY * 8) + piece.posX].piece = null;
                                break;
                            } else {
                                piece.loadMove(finalX, finalY);
                                break;
                            }
                        } else if (chosenOneX.equals("")) {
                            if (move.contains("=")) {
                                GameBoard.board[(piece.posY * 8) + piece.posX].contains = false;
                                GameBoard.board[(finalY * 8) + finalX].contains = true;
                                if (move.contains("Q")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Queen();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("B")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Bishop();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("N")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Knight();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("R")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Rook();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                GameBoard.board[(piece.posY * 8) + piece.posX].piece = null;
                                break;
                            } else {
                                if (piece.movePiece(finalX, finalY)) {
                                    break;
                                }
                            }
                        }

                    }
                    //black side
                } else {
                    for (Piece piece : GameBoard.getBlackAbleMoving(pieceName)) {
                        if (!chosenOneX.equals("") && piece.posX == firstX) {
                            if (move.contains("=")) {
                                GameBoard.board[(piece.posY * 8) + piece.posX].contains = false;
                                GameBoard.board[(finalY * 8) + finalX].contains = true;
                                if (move.contains("Q")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Queen();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("B")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Bishop();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("N")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Knight();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("R")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Rook();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                GameBoard.board[(piece.posY * 8) + piece.posX].piece = null;
                                break;
                            } else {
                                piece.loadMove(finalX, finalY);
                                break;
                            }
                        } else if (chosenOneX.equals("")) {
                            if (move.contains("=")) {
                                GameBoard.board[(piece.posY * 8) + piece.posX].contains = false;
                                GameBoard.board[(finalY * 8) + finalX].contains = true;
                                if (move.contains("Q")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Queen();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("B")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Bishop();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("N")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Knight();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                if (move.contains("R")) {
                                    GameBoard.board[(finalY * 8) + finalX].piece = new Rook();
                                    GameBoard.board[(finalY * 8) + finalX].piece.posY = finalY;
                                    GameBoard.board[(finalY * 8) + finalX].piece.posX = finalX;
                                    GameBoard.board[(finalY * 8) + finalX].piece.color = piece.color;
                                }
                                GameBoard.board[(piece.posY * 8) + piece.posX].piece = null;
                                break;
                            } else {
                                if (piece.movePiece(finalX, finalY)) {
                                    break;
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Loads all board GridPanes after each move to Grid Pane array.
     * @return Returns the GridPane array.
     */
    public static GridPane[] browseMoves(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": creating gridpane array with all moves");
        GameBoard.initBoard();
        GridPane[] gridPanes = new GridPane[loadedGame.length+1];
        for (int i = 0; i < loadedGame.length; i++) {
            gridPanes[i]=GameBoard.createGridPane();
            initMove(i);
        }
        gridPanes[loadedGame.length]=GameBoard.createGridPane();
        return gridPanes;
    }

    /**
     * Function to find out classes.pieces.Piece name from string move
     * @param move String move from classes.rest.Game.
     * @return Returns String name of the piece
     */
    public static String getPieceName(String move){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting pieceName");
        for (int i = 0; i < move.length(); i++) {
            char ch = move.charAt(i);
            if (Character.isUpperCase(ch)&&!move.contains("=")){
                return Character.toString(ch);
            }
        }
        return "";
    }

    /**
     * Function to change PNG coordinates to number coordinates
     * @param x X String coordinate of the classes.rest.Square, in case of y!="" is X ignored.
     * @param y Y String coordinate of the classes.rest.Square, in case of y="" is Y ignored.
     * @return Returns only one coordinate in integer.
     */
    public static int switchCoords(String x, String y){
        int coord = 0;
        if(y.equals("")) {
            switch (x) {
                case "a": {
                    coord = 0;
                    break;
                }
                case "b": {
                    coord = 1;
                    break;
                }
                case "c": {
                    coord = 2;
                    break;
                }
                case "d": {
                    coord = 3;
                    break;
                }
                case "e": {
                    coord = 4;
                    break;
                }
                case "f": {
                    coord = 5;
                    break;
                }
                case "g": {
                    coord = 6;
                    break;
                }
                case "h": {
                    coord = 7;
                    break;
                }
            }
        } else {
            switch (y) {
                case "8":{
                    coord = 0;
                    break;
                }
                case "7": {
                    coord = 1;
                    break;
                }
                case "6": {
                    coord = 2;
                    break;
                }
                case "5": {
                    coord = 3;
                    break;
                }
                case "4": {
                    coord = 4;
                    break;
                }
                case "3": {
                    coord = 5;
                    break;
                }
                case "2": {
                    coord = 6;
                    break;
                }
                case "1": {
                    coord = 7;
                    break;
                }
            }
        }
        return coord;
    }
}
