package classes.pieces;

import classes.rest.*;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static java.lang.Math.abs;

public abstract class Piece {
    public String color = "";
    public int posX=0;
    public int posY=0;
    String castling = "";
    boolean enpassan = false;
    boolean hasMoved = false;
    public boolean promotion = false;
    boolean usedBonus = false;
    public abstract Image getWhite();
    public abstract Image getBlack();
    public abstract Media getSound();
    public abstract String getName();

    /**
     *
     * @param x is x coordinate of place where the piece is tried to move
     * @param y is x coordinate of place where the piece is tried to move
     * @return Returns true if piece can be moved, false if can't
     */
    public abstract boolean tryMovePiece(int x, int y);

    /**
     *
     * @param x is x coordinate of place where the piece is wanted to be moved
     * @param y is x coordinate of place where the piece is wanted to be moved
     * @return Returns true if piece was moved, false if wasn't
     */
    public boolean movePiece(int x, int y){
        int checkCount=0;
        boolean isThisOnlyOne=true;
        boolean contained = GameBoard.board[(8*y)+x].contains;
        //checks if the position to move to is empty or not
        Piece containedPiece = null;
        if (contained){
            containedPiece = GameBoard.board[(8*y)+x].piece;
        }
        //if the move is possible
        if (tryMovePiece(x, y)) {
            if (enpassan){
                GameBoard.board[(this.posY * 8) + x].contains = false;
                GameBoard.board[(this.posY * 8) + x].piece = null;
                Game.moveCounter = 0;
                enpassan=false;
            }
            System.out.println("tady");
            //usedbonus is variable for exception of en passant move
            for (Square currSquare : GameBoard.board){
                if (currSquare.contains){
                    currSquare.piece.usedBonus=false;
                }
            }
            if (abs(this.posY-y)==2 && this.posX==x && this.getName().equals("")){
                this.usedBonus=true;
            }
            //checking for possibility of two pieces of same color and name having ability to move on same square, its checked for saving data
            if (this.color.equals("black")) {
                for (Piece currPiece : GameBoard.getAllBlacks()){
                    if (currPiece.getName().equals(this.getName())&&(currPiece.posX!=this.posX||currPiece.posY!=this.posY)){
                        if (currPiece.tryMovePiece(x,y)){
                            isThisOnlyOne=false;
                        }
                    }
                }
            } else {
                for (Piece currPiece : GameBoard.getAllWhites()){
                    if (currPiece.getName().equals(this.getName())&&(currPiece.posX!=this.posX||currPiece.posY!=this.posY)){
                        if (currPiece.tryMovePiece(x,y)){
                            isThisOnlyOne=false;
                        }
                    }
                }
            }
            int prevX = this.posX;
            int prevY = this.posY;
            GameBoard.board[(8 * y) + x].contains = true;
            GameBoard.board[(8 * y) + x].piece = this;
            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
            this.posY = y;
            this.posX = x;
            //basically this prevents king to move on attacked square, or showing king to attacked square by moving other piece
            if ((this.color.equals("black") && IsCheck.isCheckBlack()) || (this.color.equals("white") && IsCheck.isCheckWhite())) {
                GameBoard.board[(8 * prevY) + prevX].contains = true;
                GameBoard.board[(8 * prevY) + prevX].piece = this;
                if (!contained) {
                    GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                    GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                } else {
                    GameBoard.board[(8 * this.posY) + this.posX].piece = containedPiece;
                }
                this.posY = prevY;
                this.posX = prevX;
                return false;
            //the move is made
            } else {
                //saving the move to text format
                if (GameBoard.whosOnMove) {
                    Game.moveCounter2++;
                    if (isThisOnlyOne) {
                        if (contained) {
                            GameSaveLoad.gameData += Game.moveCounter2 + ". " + this.getName() + "x" + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                        if (!contained) {
                            GameSaveLoad.gameData += Game.moveCounter2 + ". " + this.getName() + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                    } else {
                        if (contained) {
                            GameSaveLoad.gameData += Game.moveCounter2 + ". " + this.getName() + switchCoord(prevX, -1) + "x" + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                        if (!contained) {
                            GameSaveLoad.gameData += Game.moveCounter2 + ". " + this.getName() + switchCoord(prevX, -1) + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                    }
                    if (IsCheck.isCheckBlack()){
                        GameSaveLoad.gameData += "+ ";
                    } else {
                        GameSaveLoad.gameData += " ";
                    }
                } else {
                    if (isThisOnlyOne) {
                        if (contained) {
                            GameSaveLoad.gameData += this.getName() + "x" + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                        if (!contained) {
                            GameSaveLoad.gameData += this.getName() + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                    } else {
                        if (contained) {
                            GameSaveLoad.gameData += this.getName() + switchCoord(prevX, -1) + "x" + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                        if (!contained) {
                            GameSaveLoad.gameData += this.getName() + switchCoord(prevX, -1) + switchCoord(x, -1) + switchCoord(-1, y);
                        }
                    }
                    if (IsCheck.isCheckWhite()){
                        GameSaveLoad.gameData += "+ ";
                    } else {
                        GameSaveLoad.gameData += " ";
                    }
                }
                if (GameBoard.board[(8 * y) + x].contains){
                    Game.moveCounter=0;
                } else {
                    Game.moveCounter++;
                }
                if(!this.hasMoved){
                    this.hasMoved=true;
                }
                GameBoard.changeMove();
                if (GameSaveLoad.loadedGame==null) {
                    MediaPlayer mediaPlayer = new MediaPlayer(getSound());
                    mediaPlayer.setVolume(0.5);
                    mediaPlayer.play();
                }
            }
            GameBoard.refreshBoard();
            return true;
        }
        return false;
    }

    /**
     * Used for castling only!!
     * @param x x coord of future position
     * @param y y coord of future position
     */
    public void forceMovePiece(int x,int y){
        //this is used for castling classes, it just moves the piece on the position no matter what
        GameBoard.board[(8 * y) + x].contains = true;
        GameBoard.board[(8 * y) + x].piece = this;
        GameBoard.board[(8 * this.posY) + this.posX].contains = false;
        GameBoard.board[(8 * this.posY) + this.posX].piece = null;
        this.posY=y;
        this.posX=x;
        this.hasMoved = true;
        if (GameBoard.whosOnMove) {
            Game.moveCounter++;
            Game.moveCounter2++;
            GameSaveLoad.gameData += Game.moveCounter2 + ". " + this.castling + " ";
        } else {
            GameSaveLoad.gameData += this.castling + " ";
        }
        MediaPlayer sound = new MediaPlayer(getSound());
        sound.setVolume(0.5);
        sound.play();
        GameBoard.refreshBoard();
    }

    /**
     * Forces the piece to move no matter what, used for loading games
     * @param x x coord of future position
     * @param y y coord of future position
     */
    public void loadMove(int x, int y){
        GameBoard.board[(8*y)+x].contains=true;
        GameBoard.board[(8*y)+x].piece=this;
        GameBoard.board[(this.posY*8)+this.posX].contains=false;
        GameBoard.board[(this.posY*8)+this.posX].piece=null;
        this.posX=x;
        this.posY=y;
    }
    // this is for switching coords used by me to chess coords

    /**
     * Changes coordinates to PGN format
     * @param x coordinate to change, if y is nit -1 this parametter is ignored
     * @param y coordinate to change, in case of changing x coordinate, y=-1, in case of changing y coordinate its different.
     * @return Returns changed coordinate in String
     */
    public String switchCoord(int x, int y){
        String coord = "";
        if(y==-1) {
            switch (x) {
                case 0: {
                    coord = "a";
                    break;
                }
                case 1: {
                    coord = "b";
                    break;
                }
                case 2: {
                    coord = "c";
                    break;
                }
                case 3: {
                    coord = "d";
                    break;
                }
                case 4: {
                    coord = "e";
                    break;
                }
                case 5: {
                    coord = "f";
                    break;
                }
                case 6: {
                    coord = "g";
                    break;
                }
                case 7: {
                    coord = "h";
                    break;
                }
            }
        } else {
            switch (y) {
                case 0:{
                    coord = "8";
                    break;
                }
                case 1: {
                    coord = "7";
                    break;
                }
                case 2: {
                    coord = "6";
                    break;
                }
                case 3: {
                    coord = "5";
                    break;
                }
                case 4: {
                    coord = "4";
                    break;
                }
                case 5: {
                    coord = "3";
                    break;
                }
                case 6: {
                    coord = "2";
                    break;
                }
                case 7: {
                    coord = "1";
                    break;
                }
            }
        }
        return coord;
    }

}
