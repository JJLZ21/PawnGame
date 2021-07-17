package sample.Piece;

import sample.Square;
import static sample.Main.TILE_SIZE;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

public class Pawn extends StackPane{
    public Square[][] board;

    private PieceType type;
    private int oldX;
    private int oldY;
    private double mouseX, mouseY;

    public PieceType getType() {
        return type;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public boolean isWhite(){
        return type==PieceType.White;
    }

    public boolean isBlack(){
        return type==PieceType.Black;
    }

    private boolean HasKill;

    public Pawn(PieceType type, int x, int y, String file_path, Square[][] board){
        this.type = type;
        this.board = board;

        move(x, y);
        Image image = new Image(file_path);

        ImageView imageView = new ImageView(image);
        imageView.setX(x*TILE_SIZE);
        imageView.setY(y*TILE_SIZE);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);

        getChildren().addAll(imageView);

        setOnMousePressed(e->{
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e->{
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });
    }

    public void setHasKilled(boolean has_moved) {
        this.HasKill = has_moved;
    }

    public boolean getHasKill() {
        return HasKill;
    }


    public boolean canMove( int dx, int dy){

        Pawn piece = board[dx][dy].getPiece();

        if(piece != null){
            if(piece.isWhite() && this.isWhite()){
                return false;
            }else if(piece.isBlack() && this.isBlack()){
                return false;
            }
        }

        int sx = this.getOldX() / 100;
        int sy = this.getOldY() / 100;

        String direction = "";
        if(dy > sy){
            direction = "south";
        }
        if(dy < sy){
            direction = "north";
        }
        if(getHasKill()){
            return false;
        }

        int ly = Math.abs(dy - sy);
        int lx = Math.abs(dx - sx);
        if(lx>1 || ly > 1){
            return false;
        }
        if(lx == 1 && ly != 1) {
            return false;
        }

        if(direction.equals("south")){
            if(this.isBlack()){
                return false;
            }
            if(piece == null && lx == 1){

                return false;
            }

            if(lx != 1) {
                Pawn nPiece = null;
                if(sy+1 < 8){
                    nPiece = board[sx][sy+1].getPiece();
                }

                if(nPiece != null) {

                    return false;
                }
            }
        }
        if(direction.equals("north")) {

            if(this.isWhite()){
                return false;
            }
            if(piece == null && lx == 1) {
                return false;
            }

            if(lx != 1) {
                Pawn nPiece = null;
                if(sy-1 >= 0){
                    nPiece = board[sx][sy-1].getPiece();
                }

                if(nPiece != null) {
                    return false;
                }
            }

        }
        return true;
    }

    public boolean canKill(){
        int x = getOldX() / 100;
        int y = getOldY() / 100;
        if(this.isWhite()) {
            if (x - 1 >= 0 && y + 1 < 8 && board[x - 1][y + 1].hasPiece() && board[x - 1][y + 1].getPiece().getType() != this.getType()) {
                return true;
            }
            if (x + 1 < 8 && y + 1 < 8 && board[x + 1][y + 1].hasPiece() && board[x + 1][y + 1].getPiece().getType() != this.getType()) {
                return true;
            }
        }else{
            if(x-1>=0 && y-1>=0 && board[x-1][y-1].hasPiece() && board[x-1][y-1].getPiece().getType()!=this.getType()){
                return true;
            }
            if(x+1< 8 && y-1>=0 && board[x+1][y-1].hasPiece() && board[x+1][y-1].getPiece().getType() != this.getType()){
                return true;
            }
        }
        return false;
    }


    public boolean isWin( )
    {
        for(int i = 0; i < 8; i++){
            if(board[i][0].hasPiece()) {
                return true;
            }else if(board[i][7].hasPiece()){
                return true;
            }
        }
        return false;
    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }


    public void abort(){
        relocate(oldX, oldY);
    }

}
