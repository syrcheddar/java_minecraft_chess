package classes.pieces;

import classes.rest.GameBoard;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

import static java.lang.Math.abs;

public class Bishop extends Piece {
    final Media sound = new Media(new File("src/main/sounds/bishop.mp3").toURI().toString());
    static final Image white = new Image("file:src/main/imgs/strelecB.png", 100, 100, false, false);
    static final Image black = new Image("file:src/main/imgs/strelecC.png", 100, 100, false, false);
    public static final Image whiteIc = new Image("file:src/main/imgs/strelecB.png", 80, 80, false, false);
    public static final Image blackIc = new Image("file:src/main/imgs/strelecC.png", 80, 80, false, false);
    final String name = "B";
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
        //in my opinion hardest one of pieces, have made lot of debugging
        //again found out some rules in moving
        if ((((abs((y * 8)+x-(this.posY*8)-this.posX)%9==0)) || ((this.posX+this.posY)==(x+y)))&& GameBoard.board[(8* this.posY)+this.posX].color.equals(GameBoard.board[(8* y)+x].color)) {
            if (y > this.posY) {
                if (x > this.posX) {
                    int i = this.posY;
                    int j = this.posX;
                    while (true) {
                        i++;
                        j++;
                        if (GameBoard.board[(8 * i) + j].contains && abs(x - this.posX) + abs(y - this.posY) > 2) {
                            freeSpace = false;
                            break;
                        }
                        if (i >= y-1) {
                            break;
                        }
                    }
                }
                if (x < this.posX) {
                    int i = this.posY;
                    int j = this.posX;
                    while (true) {
                        i++;
                        j--;
                        if (GameBoard.board[(8 * i) + j].contains && abs(x - this.posX) + abs(y - this.posY) > 2) {
                            freeSpace = false;
                            break;
                        }
                        if (i >= y-1) {
                            break;
                        }
                    }
                }

            } if (y<this.posY){
                if (x > this.posX) {
                    int i = this.posY;
                    int j = this.posX;
                    while (true) {
                        i--;
                        j++;
                        if ((GameBoard.board[(8 * i) + j].contains) && (abs(x - this.posX) + abs(y - this.posY) > 2)) {
                            freeSpace = false;
                            break;
                        }
                        if (i <= y+1) {
                            break;
                        }
                    }
                } if (x<this.posX){
                    int i = y;
                    int j = x;
                    while (true) {
                        i++;
                        j++;
                        if (GameBoard.board[(8 * i) + j].contains && abs(x - this.posX) + abs(y - this.posY) > 2) {
                            freeSpace = false;
                            break;
                        }
                        if (i >= this.posY-1) {
                            break;
                        }
                    }
                }
            }
            if (freeSpace) {
                if (GameBoard.board[(8 * y) + x].contains) {
                    if (!GameBoard.board[(8 * y) + x].piece.color.equals(this.color)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
