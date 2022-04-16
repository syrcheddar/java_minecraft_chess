
import classes.pieces.Bishop;
import classes.pieces.Pawn;
import classes.pieces.Piece;
import classes.pieces.Rook;
import classes.rest.GameBoard;
import classes.rest.Square;
import javafx.embed.swing.JFXPanel;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;


public class JunitTests {
    @Test
    public void isAbleToMoveFalse() {
        JFXPanel panel = new JFXPanel();
        GameBoard.initBoardOnly();
        assertFalse(GameBoard.isAbleToPlay());
    }
    @Test
    public void isAbleToMoveTrue(){
        JFXPanel panel = new JFXPanel();
        GameBoard.initBoard();
        assertTrue(GameBoard.isAbleToPlay());
    }
    @Test
    public void tryGetAllPieces(){
        JFXPanel panel = new JFXPanel();
        GameBoard.initBoard();
        Piece[] blacks = GameBoard.getAllBlacks();
        Piece[] whites = GameBoard.getAllWhites();
        assertEquals(blacks.length,16,"number of black pieces is invalid");
        assertEquals(whites.length,16,"number of black pieces is invalid");
    }
    @Test
    public void tryBasicMovePawn(){
        JFXPanel panel = new JFXPanel();
        GameBoard.initBoard();
        Piece[] pieces = GameBoard.getWhiteAbleMoving("");
        for (Piece piece : pieces){
            int counter=0;
            for(Square square : GameBoard.board){
                if (piece.tryMovePiece(square.coord2,square.coord1)){counter++;}
            }
            assertEquals(2,counter,"classes.pieces.Piece on x: "+piece.posX+", y: "+piece.posY+"can move somewhere else");
        }
        Piece[] pieces2 = GameBoard.getBlackAbleMoving("");
        for (Piece piece : pieces2){
            int counter=0;
            for(Square square : GameBoard.board){
                if (piece.tryMovePiece(square.coord2,square.coord1)){counter++;}
            }
            assertEquals(2,counter,"classes.pieces.Piece on x: "+piece.posX+", y: "+piece.posY+"can move somewhere else");
        }
    }
    @Test
    public void tryMovePawn() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[4].contains=true;
        GameBoard.board[4].piece=new Pawn();
        GameBoard.board[4].piece.color="black";
        GameBoard.board[4].piece.posX=4;
        GameBoard.board[4].piece.posY=0;
        for (Square square :GameBoard.board){
            if(GameBoard.board[4].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(2,counter,"pawn should be able to move twice");
    }
    @Test
    public void tryMovePawnFalse() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[4].contains=true;
        GameBoard.board[4].piece=new Pawn();
        GameBoard.board[4].piece.color="white";
        GameBoard.board[4].piece.posX=4;
        GameBoard.board[4].piece.posY=0;
        for (Square square :GameBoard.board){
            if(GameBoard.board[4].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(0,counter,"pawn shouldn't be able to move");
    }
    @Test
    public void tryMoveRook() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[4].contains=true;
        GameBoard.board[4].piece=new Rook();
        GameBoard.board[4].piece.color="white";
        GameBoard.board[4].piece.posX=4;
        GameBoard.board[4].piece.posY=0;
        for (Square square :GameBoard.board){
            if(GameBoard.board[4].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(14,counter,"rook should be able to make 14 moves");
    }
    @Test
    public void tryMoveRookFalse() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[35].contains=true;
        GameBoard.board[35].piece=new Rook();
        GameBoard.board[35].piece.color="white";
        GameBoard.board[35].piece.posX=3;
        GameBoard.board[35].piece.posY=4;
        GameBoard.board[27].contains=true;
        GameBoard.board[27].piece=new Pawn();
        GameBoard.board[27].piece.color="white";
        GameBoard.board[43].contains=true;
        GameBoard.board[43].piece=new Pawn();
        GameBoard.board[43].piece.color="white";
        GameBoard.board[32].contains=true;
        GameBoard.board[32].piece=new Pawn();
        GameBoard.board[32].piece.color="white";
        GameBoard.board[39].contains=true;
        GameBoard.board[39].piece=new Pawn();
        GameBoard.board[39].piece.color="white";

        for (Square square :GameBoard.board){
            if(GameBoard.board[35].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(5,counter,"rook should be able to make 5 moves in this scenario");
    }
    @Test
    public void tryMoveBishop1() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[35].contains=true;
        GameBoard.board[35].piece=new Bishop();
        GameBoard.board[35].piece.color="white";
        GameBoard.board[35].piece.posX=3;
        GameBoard.board[35].piece.posY=4;
        GameBoard.board[8].contains=true;
        GameBoard.board[8].piece=new Pawn();
        GameBoard.board[8].piece.color="white";
        GameBoard.board[56].contains=true;
        GameBoard.board[56].piece=new Pawn();
        GameBoard.board[56].piece.color="white";
        GameBoard.board[62].contains=true;
        GameBoard.board[62].piece=new Pawn();
        GameBoard.board[62].piece.color="white";
        GameBoard.board[7].contains=true;
        GameBoard.board[7].piece=new Pawn();
        GameBoard.board[7].piece.color="white";

        for (Square square :GameBoard.board){
            if(GameBoard.board[35].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(9,counter,"rook should be able to make 9 moves in this scenario");
    }
    @Test
    public void tryMoveBishop2() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[35].contains=true;
        GameBoard.board[35].piece=new Bishop();
        GameBoard.board[35].piece.color="white";
        GameBoard.board[35].piece.posX=3;
        GameBoard.board[35].piece.posY=4;
        GameBoard.board[26].contains=true;
        GameBoard.board[26].piece=new Pawn();
        GameBoard.board[26].piece.color="white";
        GameBoard.board[28].contains=true;
        GameBoard.board[28].piece=new Pawn();
        GameBoard.board[28].piece.color="white";
        GameBoard.board[42].contains=true;
        GameBoard.board[42].piece=new Pawn();
        GameBoard.board[42].piece.color="white";
        GameBoard.board[44].contains=true;
        GameBoard.board[44].piece=new Pawn();
        GameBoard.board[44].piece.color="white";

        for (Square square :GameBoard.board){
            if(GameBoard.board[35].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(0,counter,"rook should be able to make 0 moves in this scenario");
    }
    @Test
    public void tryMoveBishop3() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[35].contains=true;
        GameBoard.board[35].piece=new Bishop();
        GameBoard.board[35].piece.color="black";
        GameBoard.board[35].piece.posX=3;
        GameBoard.board[35].piece.posY=4;
        GameBoard.board[26].contains=true;
        GameBoard.board[26].piece=new Pawn();
        GameBoard.board[26].piece.color="white";
        GameBoard.board[28].contains=true;
        GameBoard.board[28].piece=new Pawn();
        GameBoard.board[28].piece.color="white";
        GameBoard.board[42].contains=true;
        GameBoard.board[42].piece=new Pawn();
        GameBoard.board[42].piece.color="white";
        GameBoard.board[44].contains=true;
        GameBoard.board[44].piece=new Pawn();
        GameBoard.board[44].piece.color="white";

        for (Square square :GameBoard.board){
            if(GameBoard.board[35].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(4,counter,"rook should be able to make 4 moves in this scenario");
    }
    @Test
    public void tryMoveBishop4() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[35].contains=true;
        GameBoard.board[35].piece=new Bishop();
        GameBoard.board[35].piece.color="white";
        GameBoard.board[35].piece.posX=3;
        GameBoard.board[35].piece.posY=4;
        GameBoard.board[17].contains=true;
        GameBoard.board[17].piece=new Pawn();
        GameBoard.board[17].piece.color="white";
        GameBoard.board[21].contains=true;
        GameBoard.board[21].piece=new Pawn();
        GameBoard.board[21].piece.color="white";
        GameBoard.board[49].contains=true;
        GameBoard.board[49].piece=new Pawn();
        GameBoard.board[49].piece.color="white";
        GameBoard.board[53].contains=true;
        GameBoard.board[53].piece=new Pawn();
        GameBoard.board[53].piece.color="white";

        for (Square square :GameBoard.board){
            if(GameBoard.board[35].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(4,counter,"rook should be able to make 4 moves in this scenario");
    }
    @Test
    public void tryMoveBishop5() {
        JFXPanel panel = new JFXPanel();
        int counter =0;
        GameBoard.initBoardOnly();
        GameBoard.board[35].contains=true;
        GameBoard.board[35].piece=new Bishop();
        GameBoard.board[35].piece.color="black";
        GameBoard.board[35].piece.posX=3;
        GameBoard.board[35].piece.posY=4;
        GameBoard.board[17].contains=true;
        GameBoard.board[17].piece=new Pawn();
        GameBoard.board[17].piece.color="white";
        GameBoard.board[21].contains=true;
        GameBoard.board[21].piece=new Pawn();
        GameBoard.board[21].piece.color="white";
        GameBoard.board[49].contains=true;
        GameBoard.board[49].piece=new Pawn();
        GameBoard.board[49].piece.color="white";
        GameBoard.board[53].contains=true;
        GameBoard.board[53].piece=new Pawn();
        GameBoard.board[53].piece.color="white";

        for (Square square :GameBoard.board){
            if(GameBoard.board[35].piece.tryMovePiece(square.coord2,square.coord1)){
                counter++;
            }
        }
        assertEquals(8,counter,"rook should be able to make 8 moves in this scenario");
    }
}
