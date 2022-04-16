package classes.pieces;

import classes.rest.GameBoard;
import classes.rest.IsCheck;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

import static java.lang.Math.abs;

public class King extends Piece {
    final Media sound = new Media(new File("src/main/sounds/king.mp3").toURI().toString());
    final Image white = new Image("file:src/main/imgs/kralB.png", 100, 100, false, false);
    final Image black = new Image("file:src/main/imgs/kralC.png", 100, 100, false, false);
    public final Image whiteIc = new Image("file:src/main/imgs/kralB.png", 80, 80, false, false);
    public final Image blackIc = new Image("file:src/main/imgs/kralC.png", 80, 80, false, false);
    final String name = "K";
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
        //king can move only in square around him
        if ((abs(this.posX-x)<2)&&(abs(this.posY-y)<2)){
            if (GameBoard.board[(y * 8) + x].contains&&this.color.equals(GameBoard.board[(y * 8) + x].piece.color)) {
                return false;
            }
            //if is check king cannot move
            if (this.color.equals("black")&& !IsCheck.willBeCheckBlack(x,y)) {
                if (GameBoard.board[(y * 8) + x].contains) {
                    return true;
                } else {
                    return true;
                }
            }
            //if is check king cannot move
            if (this.color.equals("white")&& !IsCheck.willBeCheckWhite(x,y)) {
                if (GameBoard.board[(y * 8) + x].contains) {
                    return true;
                } else {
                    return true;
                }
            }
            //castling
        } else if(!this.hasMoved) {
            //black castling
            if (this.color.equals("black") && ((x == 0)||(x==7)) && (y == 0) && GameBoard.board[(8 * y) + x].contains && !IsCheck.isCheckBlack()) {
                if (!GameBoard.board[(8 * y) + x].piece.hasMoved) {
                    if (x < this.posX) {
                        //queenside
                        if (!GameBoard.board[(8 * y) + 1].contains && !GameBoard.board[(8 * y) + 2].contains && !GameBoard.board[(8 * y) + 3].contains) {
                            GameBoard.board[(8 * y) + 3].contains = true;
                            GameBoard.board[(8 * y) + 3].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY = 0;
                            this.posX = 3;
                            if (!IsCheck.isCheckBlack()) {
                                GameBoard.board[(8 * y) + 2].contains = true;
                                GameBoard.board[(8 * y) + 2].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY = 0;
                                this.posX = 2;
                                if (!IsCheck.isCheckBlack()) {
                                    GameBoard.board[(8 * y) + x].piece.castling="O-O-O";
                                    GameBoard.board[(8 * y) + x].piece.forceMovePiece(3, 0);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[(8 * y) + 4].contains = true;
                                GameBoard.board[(8 * y) + 4].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY = 0;
                                this.posX = 4;
                                return false;
                            }
                            GameBoard.board[(8 * y) + 4].contains = true;
                            GameBoard.board[(8 * y) + 4].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY = 0;
                            this.posX = 4;
                            return false;
                        }
                        //kingside
                    } else {
                        if (!GameBoard.board[(8 * y) + 5].contains && !GameBoard.board[(8 * y) + 6].contains) {
                            GameBoard.board[(8 * y) + 5].contains = true;
                            GameBoard.board[(8 * y) + 5].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY=0;
                            this.posX=5;
                            if (!IsCheck.isCheckBlack()) {
                                GameBoard.board[(8 * y) + 6].contains = true;
                                GameBoard.board[(8 * y) + 6].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY=0;
                                this.posX=6;
                                if (!IsCheck.isCheckBlack()) {
                                    GameBoard.board[(8 * y) + x].piece.castling="O-O";
                                    this.hasMoved = true;
                                    GameBoard.board[(8 * y) + x].piece.forceMovePiece(5, 0);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[(8 * y) + 4].contains = true;
                                GameBoard.board[(8 * y) + 4].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY=0;
                                this.posX=4;
                                return false;
                            }
                            GameBoard.board[(8 * y) + 4].contains = true;
                            GameBoard.board[(8 * y) + 4].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY=0;
                            this.posX=4;
                            return false;
                        }
                    }
                }
            }
            //white castling
            if (this.color.equals("white") && ((x == 0)||(x==7)) && (y == 7) && GameBoard.board[(8 * y) + x].contains && !IsCheck.isCheckWhite()) {
                if (!GameBoard.board[(8 * y) + x].piece.hasMoved) {
                    if (x < this.posX) {
                        //queenside
                        if (!GameBoard.board[(8 * y) + 1].contains && !GameBoard.board[(8 * y) + 2].contains && !GameBoard.board[(8 * y) + 3].contains) {
                            GameBoard.board[(8 * y) + 3].contains = true;
                            GameBoard.board[(8 * y) + 3].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY = 7;
                            this.posX = 3;
                            if (!IsCheck.isCheckWhite()) {
                                GameBoard.board[(8 * y) + 2].contains = true;
                                GameBoard.board[(8 * y) + 2].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY = 7;
                                this.posX = 2;
                                if (!IsCheck.isCheckWhite()) {
                                    GameBoard.board[(8 * y) + x].piece.castling="O-O-O";
                                    GameBoard.board[(8 * y) + x].piece.forceMovePiece(3, 7);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[(8 * y) + 4].contains = true;
                                GameBoard.board[(8 * y) + 4].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY = 7;
                                this.posX = 4;
                                return false;
                            }
                            GameBoard.board[(8 * y) + 4].contains = true;
                            GameBoard.board[(8 * y) + 4].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY = 7;
                            this.posX = 4;
                            return false;
                        }
                        //kingside
                    } else {
                        if (!GameBoard.board[(8 * y) + 5].contains && !GameBoard.board[(8 * y) + 6].contains) {
                            GameBoard.board[(8 * y) + 5].contains = true;
                            GameBoard.board[(8 * y) + 5].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY = 7;
                            this.posX = 5;
                            if (!IsCheck.isCheckWhite()) {
                                GameBoard.board[(8 * y) + 6].contains = true;
                                GameBoard.board[(8 * y) + 6].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY = 7;
                                this.posX = 6;
                                if (!IsCheck.isCheckWhite()) {
                                    GameBoard.board[(8 * y) + x].piece.castling="O-O";
                                    GameBoard.board[(8 * y) + x].piece.forceMovePiece(5, 7);
                                    GameBoard.changeMove();
                                    GameBoard.refreshBoard();
                                    return false;
                                }
                                GameBoard.board[(8 * y) + 4].contains = true;
                                GameBoard.board[(8 * y) + 4].piece = this;
                                GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                                GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                                this.posY = 7;
                                this.posX = 4;
                                return false;
                            }
                            GameBoard.board[(8 * y) + 4].contains = true;
                            GameBoard.board[(8 * y) + 4].piece = this;
                            GameBoard.board[(8 * this.posY) + this.posX].contains = false;
                            GameBoard.board[(8 * this.posY) + this.posX].piece = null;
                            this.posY = 7;
                            this.posX = 4;
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
