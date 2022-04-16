package classes.pieces;

import classes.rest.GameBoard;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

import java.io.File;

import static java.lang.Math.abs;

public class Knight extends Piece {
    final Media sound = new Media(new File("src/main/sounds/knight.mp3").toURI().toString());
    static final Image white = new Image("file:src/main/imgs/jezdecB.png", 100, 100, false, false);
    static final Image black = new Image("file:src/main/imgs/jezdecC.png", 100, 100, false, false);
    public static final Image whiteIc = new Image("file:src/main/imgs/jezdecB.png", 80, 80, false, false);
    public static final Image blackIc = new Image("file:src/main/imgs/jezdecC.png", 80, 80, false, false);
    final String name = "N";
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
        //i found out, that these numbers are always same, so made rule that it must be equals to 6/10/15/17
        if ((abs(((y*8)+x)-(this.posY*8)-this.posX)==6)||(abs(((y*8)+x)-(this.posY*8)-this.posX)==10)||(abs(((y*8)+x)-(this.posY*8)-this.posX)==15)||(abs(((y*8)+x)-(this.posY*8)-this.posX)==17)){
            if (GameBoard.board[(y*8)+x].contains){
                if (!this.color.equals(GameBoard.board[(y * 8) + x].piece.color)){
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
