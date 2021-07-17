package sample;

import javafx.scene.shape.Rectangle;
import sample.Piece.Pawn;
import javafx.scene.paint.Color;

public class Square extends Rectangle {
    private Pawn pawn;
    public boolean hasPiece(){
        return pawn != null;
    }
    public Pawn getPiece(){
        return pawn;
    }
    public void setPiece(Pawn pawn){
        this.pawn = pawn;
    }

    public Square(boolean light, int x, int y){
        setWidth(Main.TILE_SIZE);
        setHeight(Main.TILE_SIZE);
        relocate(x*Main.TILE_SIZE, y*Main.TILE_SIZE);
        setFill(light ? Color.valueOf("#66CC66") : Color.valueOf("#FFFFCC"));
    }
}
