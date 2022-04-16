package classes.pieces;

import classes.rest.GameBoard;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

import static java.lang.Math.abs;

public class Pawn extends Piece {
    final Image white = new Image("file:src/main/imgs/pesakB.png", 100, 100, false, false);
    final Image black = new Image("file:src/main/imgs/pesakC.png", 100, 100, false, false);
    final Media sound = new Media(new File("src/main/sounds/pawn.mp3").toURI().toString());
    final String name = "";
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
        //white side
        if (this.color.equals("white")){
            //this.hasMoved is fase if piece havent moved yet, its used for double move of pawn
            if (this.hasMoved) {
                //if pawn moves straight
                if ((this.posY - y == 1) && (this.posX == x) && (!GameBoard.board[(y * 8) + x].contains)) {
                    //if pawn wants to move on position for promotion
                    if (y==0){
                        this.promotion=true;
                    }
                    return true;
                }
                //if pawn conquers another piece
                if ((this.posY - y == 1) && (abs(this.posX - x) == 1) && (GameBoard.board[(y * 8) + x].contains)) {
                    if (GameBoard.board[(y * 8) + x].piece.color.equals("black")) {
                        //if pawn wants to move on position for promotion
                        if (y==0){
                            this.promotion=true;
                        }
                        return true;
                    }
                }
                if (this.posY==3){
                    //en passant move for white
                    if (x-this.posX==1){
                        if (this.posY-y==1) {
                            if (GameBoard.board[(this.posY * 8) + x].contains) {
                                if (GameBoard.board[(this.posY * 8) + x].piece.usedBonus) {
                                    enpassan=true;
                                    return true;
                                }
                            }
                        }
                    }
                    if (this.posX-x==1){
                        if (this.posY-y==1) {
                            if (GameBoard.board[(this.posY * 8) + x].contains) {
                                if (GameBoard.board[(this.posY * 8) + x].piece.usedBonus) {
                                    enpassan=true;
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else {
                //double move
                if ((this.posY - y ==2) && (this.posX == x) && (!GameBoard.board[(y * 8) + x].contains)&&(!GameBoard.board[((y+1) * 8) + x].contains)) {
                    usedBonus=true;
                    return true;
                }
                if ((this.posY - y ==1) && (this.posX == x) && (!GameBoard.board[(y * 8) + x].contains)) {
                    return true;
                }
                if ((this.posY - y == 1) && (abs(this.posX - x) == 1) && (GameBoard.board[(y * 8) + x].contains)) {
                    if (GameBoard.board[(y * 8) + x].piece.color.equals("black")) {
                        return true;
                    }
                }
            }
        //black side
        } else {
            //this.hasMoved is fase if piece havent moved yet, its used for double move of pawn
            if (this.hasMoved) {
                //if pawn moves straight
                if ((y -this.posY == 1) && (this.posX == x) && (!GameBoard.board[(y * 8) + x].contains)) {
                    //if pawn wants to move on position for promotion
                    if (y==7){
                        this.promotion=true;
                        return true;
                    }
                    return true;
                }
                //if pawn conquers another piece
                if ((y - this.posY == 1) && (abs(this.posX - x) == 1) && (GameBoard.board[(y * 8) + x].contains)) {
                    //if pawn wants to move on position for promotion
                    if (GameBoard.board[(y * 8) + x].piece.color.equals("white")) {
                        if (y==7){
                            this.promotion=true;
                            return true;
                        }
                        return true;
                    }
                }
            //double move of black pawn
            } else {
                if ((y-this.posY== 2) && (this.posX == x) && (!GameBoard.board[(y * 8) + x].contains)&&(!GameBoard.board[((y-1) * 8) + x].contains)) {
                    usedBonus=true;
                    return true;
                }
                if ((y - this.posY == 1) && (this.posX == x) && (!GameBoard.board[(y * 8) + x].contains)) {
                    return true;
                }
                if ((y - this.posY == 1) && (abs(this.posX - x) == 1) && (GameBoard.board[(y * 8) + x].contains)) {
                    if (GameBoard.board[(y * 8) + x].piece.color.equals("white")) {
                        return true;
                    }
                }
            }
            if (this.posY==4){
                //en passant move for black
                if (x-this.posX==1){
                    if (this.posY-y==-1) {
                        if (GameBoard.board[(this.posY * 8) + x].contains) {
                            if (GameBoard.board[(this.posY * 8) + x].piece.usedBonus) {
                                enpassan=true;
                                return true;
                            }

                        }
                    }
                }
                if (this.posX-x==1){
                    if (this.posY-y==-1) {
                        if (GameBoard.board[(this.posY * 8) + x].contains) {
                            if (GameBoard.board[(this.posY * 8) + x].piece.usedBonus) {
                                enpassan=true;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
