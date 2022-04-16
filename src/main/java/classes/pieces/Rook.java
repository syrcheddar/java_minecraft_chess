package classes.pieces;

import classes.rest.GameBoard;
import classes.rest.IsCheck;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

import static java.lang.Math.abs;

public class Rook extends Piece {
    final Media sound = new Media(new File("src/main/sounds/rook.mp3").toURI().toString());
    static final Image white = new Image("file:src/main/imgs/vezB.png", 100, 100, false, false);
    static final Image black = new Image("file:src/main/imgs/vezC.png", 100, 100, false, false);
    public static final Image whiteIc = new Image("file:src/main/imgs/vezB.png", 80, 80, false, false);
    public static final Image blackIc = new Image("file:src/main/imgs/vezC.png", 80, 80, false, false);
    final String name = "R";
    @Override
    public Media getSound(){return sound;}

    @Override
    public Image getWhite() {
        return white;
    }

    @Override
    public Image getBlack() {
        return black;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean tryMovePiece(int x, int y) {
        boolean freeSpace = true;
        //checking if rook goes only vertical or horizontal
        if (((x == this.posX) && (y != this.posY)) || ((x != this.posX) && (y == this.posY))) {
            //going vertical
            if (x==this.posX){
                if (y>this.posY){
                    for (int i = this.posY+1; i <y ; i++) {
                        if ((GameBoard.board[(8 * i)+x].contains)&& (abs(y-this.posY)>1)){
                            freeSpace=false;
                            break;
                        }
                    }
                }
                if (y<this.posY){
                    for (int i = y+1; i <this.posY ; i++) {
                        if ((GameBoard.board[(8 * i)+x].contains)&& (abs(y-this.posY)>1)){
                            freeSpace=false;
                            break;
                        }
                    }
                }
                //going horizontal
            }else {
                if (x>this.posX){
                    for (int i = this.posX+1; i <x ; i++) {
                        if ((GameBoard.board[(8 * y)+i].contains)&& (abs(x-this.posX)>1)){
                            freeSpace=false;
                            break;
                        }
                    }
                }
                if (x<this.posX){
                    for (int i = x+1; i <this.posX ; i++) {
                        if ((GameBoard.board[(8 * y)+i].contains)&& (abs(x-this.posX)>1)){
                            freeSpace=false;
                            break;
                        }
                    }
                }
            }
            //checking if squares between current and future position are empty
            if(freeSpace) {
                if (this.color.equals("black")) {
                    if (!GameBoard.board[(8 * y) + x].contains || (GameBoard.board[(8 * y) + x].piece.color.equals("white"))) {
                        return true;

                    }
                } else {
                    if (!GameBoard.board[(8 * y) + x].contains || (GameBoard.board[(8 * y) + x].piece.color.equals("black"))) {
                        return true;
                    }
                }
            }
        }
        //castling
        if(!this.hasMoved) {
            if (this.color.equals("black") && (x == 4) && (y == 0) && GameBoard.board[x].contains && !IsCheck.isCheckBlack()) {
                if (!GameBoard.board[x].piece.hasMoved) {
                    if (x > this.posX) {
                        //queenside
                        if (!GameBoard.board[1].contains && !GameBoard.board[2].contains && !GameBoard.board[3].contains) {
                            Piece dys = new King();
                            dys.color = "black";
                            GameBoard.board[3].contains = true;
                            dys.posY = 0;
                            dys.posX = 3;
                            GameBoard.board[3].piece = dys;
                            GameBoard.board[x].contains = false;
                            GameBoard.board[x].piece = null;
                            if (!IsCheck.isCheckBlack()) {
                                GameBoard.board[2].contains = true;
                                dys.posY = 0;
                                dys.posX = 2;
                                GameBoard.board[2].piece = dys;
                                GameBoard.board[3].contains = false;
                                GameBoard.board[3].piece = null;
                                if (!IsCheck.isCheckBlack()) {
                                    this.castling="O-O-O";
                                    GameBoard.board[(8 * this.posY) + this.posX].piece.forceMovePiece(3, 0);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[4].contains = true;
                                dys.posY = 0;
                                dys.posX = 4;
                                GameBoard.board[4].piece = dys;
                                GameBoard.board[2].contains = false;
                                GameBoard.board[2].piece = null;
                                return false;
                            }
                            GameBoard.board[4].contains = true;
                            dys.posY = 0;
                            dys.posX = 4;
                            GameBoard.board[4].piece = dys;
                            GameBoard.board[3].contains = false;
                            GameBoard.board[3].piece = null;
                            return false;
                        }
                    } else {
                        //kingside
                        if (!GameBoard.board[5].contains && !GameBoard.board[6].contains) {
                            Piece dys = new King();
                            dys.color = "black";
                            dys.posY = 0;
                            dys.posX = 5;
                            GameBoard.board[5].contains = true;
                            GameBoard.board[5].piece = dys;
                            GameBoard.board[x].contains = false;
                            GameBoard.board[x].piece = null;
                            if (!IsCheck.isCheckBlack()) {
                                GameBoard.board[6].contains = true;
                                dys.posY = 0;
                                dys.posX = 6;
                                GameBoard.board[6].piece = dys;
                                GameBoard.board[5].contains = false;
                                GameBoard.board[5].piece = null;
                                if (!IsCheck.isCheckBlack()) {
                                    this.castling="O-O";
                                    GameBoard.board[(8 * this.posY) + this.posX].piece.forceMovePiece(5, 0);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[4].contains = true;
                                dys.posY = 0;
                                dys.posX = 4;
                                GameBoard.board[4].piece = dys;
                                GameBoard.board[6].contains = false;
                                GameBoard.board[6].piece = null;
                                return false;
                            }
                            GameBoard.board[4].contains = true;
                            dys.posY = 0;
                            dys.posX = 4;
                            GameBoard.board[4].piece = dys;
                            GameBoard.board[5].contains = false;
                            GameBoard.board[5].piece = null;
                            return false;
                        }
                    }
                }
            }
            //white
            if (this.color.equals("white") && (x == 4) && (y == 7) && GameBoard.board[(8 * y) + x].contains && !IsCheck.isCheckWhite()) {
                if (!GameBoard.board[(8 * y) + x].piece.hasMoved) {
                    if (x > this.posX) {
                        //queenSide
                        if (!GameBoard.board[(8 * y) + 1].contains && !GameBoard.board[(8 * y) + 2].contains && !GameBoard.board[(8 * y) + 3].contains) {
                            Piece dys = new King();
                            dys.color = "white";
                            GameBoard.board[(8 * y) + 3].contains = true;
                            dys.posY = 7;
                            dys.posX = 3;
                            GameBoard.board[(8 * y) + 3].piece = dys;
                            GameBoard.board[(8 * y) + x].contains = false;
                            GameBoard.board[(8 * y) + x].piece = null;
                            if (!IsCheck.isCheckWhite()) {
                                GameBoard.board[(8 * y) + 2].contains = true;
                                dys.posY = 7;
                                dys.posX = 2;
                                GameBoard.board[(8 * y) + 2].piece = dys;
                                GameBoard.board[(8 * y) + 3].contains = false;
                                GameBoard.board[(8 * y) + 3].piece = null;
                                if (!IsCheck.isCheckWhite()) {
                                    this.castling="O-O-O";
                                    GameBoard.board[(8 * this.posY) + this.posX].piece.forceMovePiece(3, 7);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[(8 * y) + 4].contains = true;
                                dys.posY = 7;
                                dys.posX = 4;
                                GameBoard.board[(8 * y) + 4].piece = dys;
                                GameBoard.board[(8 * y) + 2].contains = false;
                                GameBoard.board[(8 * y) + 2].piece = null;
                                return false;
                            }
                            GameBoard.board[(8 * y) + 4].contains = true;
                            dys.posY = 7;
                            dys.posX = 4;
                            GameBoard.board[(8 * y) + 4].piece = dys;
                            GameBoard.board[(8 * y) + 3].contains = false;
                            GameBoard.board[(8 * y) + 3].piece = null;
                            return false;
                        }
                        //kingside
                    } else {
                        if (!GameBoard.board[(8 * y) + 5].contains && !GameBoard.board[(8 * y) + 6].contains) {
                            Piece dys = new King();
                            dys.color = "white";
                            dys.posY = 7;
                            dys.posX = 5;
                            GameBoard.board[(8 * y) + 5].contains = true;
                            GameBoard.board[(8 * y) + 5].piece = dys;
                            GameBoard.board[(8 * y) + x].contains = false;
                            GameBoard.board[(8 * y) + x].piece = null;
                            if (!IsCheck.isCheckWhite()) {
                                GameBoard.board[(8 * y) + 6].contains = true;
                                dys.posY = 7;
                                dys.posX = 6;
                                GameBoard.board[(8 * y) + 6].piece = dys;
                                GameBoard.board[(8 * y) + 5].contains = false;
                                GameBoard.board[(8 * y) + 5].piece = null;
                                if (!IsCheck.isCheckWhite()) {
                                    this.castling="O-O";
                                    GameBoard.board[(8 * this.posY) + this.posX].piece.forceMovePiece(5, 7);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[(8 * y) + 4].contains = true;
                                dys.posY = 7;
                                dys.posX = 4;
                                GameBoard.board[(8 * y) + 4].piece = dys;
                                GameBoard.board[(8 * y) + 6].contains = false;
                                GameBoard.board[(8 * y) + 6].piece = null;
                                return false;
                            }
                            GameBoard.board[(8 * y) + 4].contains = true;
                            dys.posY = 7;
                            dys.posX = 4;
                            GameBoard.board[(8 * y) + 4].piece = dys;
                            GameBoard.board[(8 * y) + 5].contains = false;
                            GameBoard.board[(8 * y) + 5].piece = null;
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
