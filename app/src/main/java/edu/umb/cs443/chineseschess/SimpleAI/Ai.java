package edu.umb.cs443.chineseschess.SimpleAI;

import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Random;
import edu.umb.cs443.chineseschess.Point2D;
import edu.umb.cs443.chineseschess.Board;
import edu.umb.cs443.chineseschess.piece.Piece;


public class Ai {
    // W.I.P.
    public static Point2D[] eval(Board board, boolean isRedTurn, int level, int time){
        Board boardCopy = copyBoard(board);
        Piece[] redList = copyOneSideList(board,true);
        Piece[] blackList = copyOneSideList(board, false);
        Piece currentBladkPiece;
        Piece currentRedPiece;
// W.I.P.
        return new Point2D[2];
    }

    private static Board copyBoard (Board board){
        Board boardCopy = new Board();
        boardCopy.board = board.board.clone();
        boardCopy.RGP = board.RGP;
        boardCopy.BGP = board.BGP;
        return boardCopy;
    }
    private static Piece pickPiece(Piece[] list){
        Piece picked;
        do{
            picked = list[new Random().nextInt(16)];
        }while(!picked.isEmpty);
        return picked;
    }

    private static Piece[] copyOneSideList(Board board, boolean isRed){
        Piece[] list = new Piece[16];
        int k = 0;
        for (int i = 0; i < board.board.length; i++){
            for (int j = 0; j < board.board[0].length; j++){
                if (!board.board[i][j].isEmpty && board.board[i][j].isRed == isRed){
                    list[k] = board.board[i][j];
                    k++;
                }

            }

        }
        return list;
    }

//    public static Hashtable<Integer,Integer> getPiecePoints(){}
}
