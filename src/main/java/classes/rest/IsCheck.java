package classes.rest;

import classes.pieces.King;
import classes.pieces.Piece;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IsCheck {
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
    private static final Logger LOG = Logger.getLogger(GameBoard.class.getName());
    /**
     * Function to find out Black king coord x.
     * @return Returns x coord of black king
     */
    public static int getBlackKingX(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting black king x coord");
        for (Square currSrc : GameBoard.board){
            if(currSrc.contains) {
                if (currSrc.piece.color.equals("black")) {
                    if (currSrc.piece.getName().equals("K")) {
                        return currSrc.piece.posX;
                    }
                }
            }
        }
        return -1;
    }
    /**
     * Function to find out Black king coord y.
     * @return Returns y coord of black king
     */
    public static int getBlackKingY(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting black king y coord");
        for (Square currSrc : GameBoard.board){
            if(currSrc.contains) {
                if (currSrc.piece.color.equals("black")) {
                    if (currSrc.piece.getName().equals("K")) {
                        return currSrc.piece.posY;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Checks for a check on black side
     * @return Returns true if check is and false if not.
     */
    public static boolean isCheckBlack(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checking for black check");
        for (Square currSquare : GameBoard.board){
            if(currSquare.contains){
                if (currSquare.piece.color.equals("white")){
                    if (currSquare.piece.tryMovePiece(getBlackKingX(), getBlackKingY())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static void setLogICh(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        try {
            LOG.addHandler(new FileHandler("ICh_"+dtf.format(LocalDateTime.now())+".log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks for a check on black side after moving classes.pieces.King.
     * @return Returns true if check will be and false if wont.
     */
    public static boolean willBeCheckBlack(int x, int y){
        Piece tempPiece =null;
        if(GameBoard.board[(y*8)+x].contains){
            tempPiece = GameBoard.board[(y*8)+x].piece;
        }
        GameBoard.board[(y*8)+x].contains=true;
        GameBoard.board[(y*8)+x].piece=new King();
        GameBoard.board[(y*8)+x].piece.color="black";
        for (Square currSquare : GameBoard.board){
            if(currSquare.contains){
                if (currSquare.piece.color.equals("white") && currSquare.piece.tryMovePiece(x, y)){
                    if (tempPiece==null) {
                        GameBoard.board[(y * 8) + x].contains = false;
                    }
                    GameBoard.board[(y*8)+x].piece=tempPiece;
                    return true;
                }
            }
        }
        if (tempPiece==null) {
            GameBoard.board[(y * 8) + x].contains = false;
        }
        GameBoard.board[(y*8)+x].piece=tempPiece;
        return false;
    }
    /**
     * Function to find out white king coord x.
     * @return Returns x coord of white king
     */
    public static int getWhiteKingX(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting white king x coord");
        for (Square currSrc : GameBoard.board){
            if(currSrc.contains) {
                if (currSrc.piece.color.equals("white")) {
                    if (currSrc.piece.getName().equals("K")) {
                        return currSrc.piece.posX;
                    }
                }
            }
        }
        return -1;
    }
    /**
     * Function to find out white king coord y.
     * @return Returns y coord of white king
     */
    public static int getWhiteKingY(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": getting white king y coord");
        for (Square currSrc : GameBoard.board){
            if(currSrc.contains) {
                if (currSrc.piece.color.equals("white")) {
                    if (currSrc.piece.getName().equals("K")) {
                        return currSrc.piece.posY;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Checks for a check on white side
     * @return Returns true if check is and false if not.
     */
    //returns true if is white king is currently attacked
    public static boolean isCheckWhite(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checking for white check");
        for (Square currSquare : GameBoard.board){
            if(currSquare.contains){
                if (currSquare.piece.color.equals("black")){
                    if (currSquare.piece.tryMovePiece(getWhiteKingX(), getWhiteKingY())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Checks for a check on white side after moving classes.pieces.King.
     * @return Returns true if check will be and false if wont.
     */
    public static boolean willBeCheckWhite(int x, int y){
        Piece tempPiece =null;
        if(GameBoard.board[(y*8)+x].contains){
            tempPiece = GameBoard.board[(y*8)+x].piece;
        }
        GameBoard.board[(y*8)+x].contains=true;
        GameBoard.board[(y*8)+x].piece=new King();
        GameBoard.board[(y*8)+x].piece.color="white";
        for (Square currSquare : GameBoard.board){
            if(currSquare.contains){
                if (currSquare.piece.color.equals("black") && currSquare.piece.tryMovePiece(x, y)){
                    if (tempPiece==null) {
                        GameBoard.board[(y * 8) + x].contains = false;
                    }
                    GameBoard.board[(y*8)+x].piece=tempPiece;
                    return true;
                }
            }
        }
        if (tempPiece==null) {
            GameBoard.board[(y * 8) + x].contains = false;
        }
        GameBoard.board[(y*8)+x].piece=tempPiece;
        return false;
    }

    /**
     * Checks if both players have only Kings.
     * @return Returns ture if yes and false if no.
     */
    public static boolean kingVsKing(){
        LOG.log(Level.INFO,dtf.format(LocalDateTime.now())+": checking for game king vs king");
        Piece[] blacks = GameBoard.getAllBlacks();
        Piece[] whites = GameBoard.getAllWhites();
        if ((blacks.length == 1) && blacks[0].getName().equals("K")){
            if ((whites.length == 1) && whites[0].getName().equals("K")){
                return true;
            }
        }
        return false;
    }
}
