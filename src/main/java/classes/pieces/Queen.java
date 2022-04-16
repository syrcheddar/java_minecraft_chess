package classes.pieces;

import classes.rest.GameBoard;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

import static java.lang.Math.abs;

public class Queen extends Piece {
    final Media sound = new Media(new File("src/main/sounds/queen.mp3").toURI().toString());
    static final Image white = new Image("file:src/main/imgs/kralovnaB.png", 100, 100, false, false);
    static final Image black = new Image("file:src/main/imgs/kralovnaC.png", 100, 100, false, false);
    public static final Image whiteIc = new Image("file:src/main/imgs/kralovnaB.png", 80, 80, false, false);
    public static final Image blackIc = new Image("file:src/main/imgs/kralovnaC.png", 80, 80, false, false);
    final String name = "Q";
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
        }else if ((((abs((y * 8)+x-(this.posY*8)-this.posX)%9==0)) || ((this.posX+this.posY)==(x+y)))&&GameBoard.board[(8* this.posY)+this.posX].color.equals(GameBoard.board[(8* y)+x].color)) {
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
