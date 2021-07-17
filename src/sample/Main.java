package sample;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import sample.Piece.*;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public int turnCount = 0;
    public ArrayList<Pawn> OldPawns = new ArrayList<>();

    private Group tileGroup = new Group();
    private Group pawnGroup = new Group();
    private Square[][] board = new Square[WIDTH][HEIGHT];
    private Pawn oldPawn;

    private int wk = 0;
    private int bk = 0;

    //create and initial the board
    private Parent createContent(){
        Pane root = new Pane();

        root.setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pawnGroup);
        for(int y = 0; y < HEIGHT; y++){
            for(int x = 0; x < WIDTH; x++){
                Square tile = new Square((x+y)%2 != 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);
            }
        }

        for(int i = 0; i < HEIGHT; i++){
            Pawn p = makePawnPiece(PieceType.Black, i,6,"/Image/BP.gif", board);
            Square t = board[i][6];
            t.setPiece(p);
            board[i][6] = t;

            Pawn p2 = makePawnPiece(PieceType.White, i, 1, "/Image/WP.gif", board);
            Square t2 = board[i][1];
            t2.setPiece(p2);
            board[i][1] = t2;

            pawnGroup.getChildren().addAll(p,p2);
        }

        return root;
    }

    private MoveResult tryMove(Pawn piece, int newX, int newY){
        boolean is_white_turn = true;


        if(piece.isWin()){
            return new MoveResult(MoveType.WIN);
        }
        if(turnCount % 2 == 1){
            is_white_turn = false;
        }

        if(piece.canKill() && !board[newX][newY].hasPiece()){

            return new MoveResult(MoveType.NONE);
        }

        if(OldPawns != null){
            for(Pawn p : OldPawns){
                if(piece == p && p.getHasKill()){
                    return new MoveResult(MoveType.NONE);
                }
            }

        }

        if(piece.getOldX() / TILE_SIZE == newX && piece.getOldY() / TILE_SIZE == newY){

            return new MoveResult(MoveType.NONE);
        }
        if(piece.canMove(newX, newY) && ((is_white_turn && piece.isWhite()) || (!is_white_turn && piece.isBlack()))){
            turnCount++;

            if(board[newX][newY].hasPiece() && board[newX][newY].getPiece().getType() != piece.getType()){
                --turnCount;

                if(piece.getClass().equals(Pawn.class)){
                    Pawn castedPawn = piece;
                    castedPawn.setHasKilled(true);
                }

                oldPawn = piece;

                if(oldPawn != null){
                    OldPawns.add(oldPawn);

                }
                if(piece.getType() == PieceType.White){
                    bk++;
                }else {
                    wk++;
                }
                return new MoveResult(MoveType.KILL, board[newX][newY].getPiece());
            }

            if(OldPawns != null){
                for(Pawn p : OldPawns){

                    p.setHasKilled(false);
                    Pawn castedPawn = piece;
                    castedPawn.setHasKilled(false);
                }

            }

            return new MoveResult(MoveType.NORMAL);
        }

        return new MoveResult(MoveType.NONE);
    }

    private int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    private Pawn makePawnPiece(PieceType type, int x, int y, String file_path, Square[][] board){
        Pawn pawn = new Pawn(type, x,y,file_path, board);

        pawn.setOnMouseReleased(e -> {

            int newX = toBoard(pawn.getLayoutX());
            int newY = toBoard(pawn.getLayoutY());

            MoveResult result;
            if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                result = new MoveResult(MoveType.NONE);
            } else {
                result = tryMove(pawn, newX, newY);
            }

            int x0 = toBoard(pawn.getOldX());
            int y0 = toBoard(pawn.getOldY());
            switch (result.getType()) {
                case WIN:
                    System.out.println("game over");
                case NONE:
                    pawn.abort();

                    break;
                case NORMAL:
                    pawn.move(newX, newY);
                    if(newY == 0 || newY ==7){
                        if(pawn.isWhite()){
                            System.out.println("Game over White win!");
                        }else{
                            System.out.println("Game over Black win!");
                        }
                    }
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(pawn);
                    break;
                case KILL:
                    Pawn otherPiece = result.getPiece();
                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                    pawnGroup.getChildren().remove(otherPiece);

                    pawn.move(newX, newY);
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(pawn);
                    if(wk== 7){
                        System.out.println("Game over Black win!");

                    } else if (bk ==7) {
                        System.out.println("Game over White win!");

                    }

                    break;
            }
        });

        return pawn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Chess Pawn Game");
        primaryStage.setScene(new Scene(createContent(), 800, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
