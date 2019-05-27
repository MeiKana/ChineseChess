package edu.umb.cs443.chineseschess;


import edu.umb.cs443.chineseschess.piece.General;
import edu.umb.cs443.chineseschess.piece.Piece;

public class Board
{
    public Piece[][] board;

    public Point2D RGP;
    public Point2D BGP;

    public int RIVER_BLACK_SIDE = 5;
    public int RIVER_RED_SIDE = 4;
    private int INTI_LOGS_SIZE = 256;

    /* int[][] log:{Side, Piece type id, prev pos X， prev pos Y, next pos X, next pos Y}   Side: 0 = black, 1 = red
    * Piece type id: 0 = General, 1 = Rook, 2 = Horse, 3 = Elephant, 4 = Advisor, 5 = Cannon 6 = Solider
    *
    * */

    int[][] log;
    int logPointer;

    boolean red_win;
    boolean black_win;

    public Board()
    {
        board = new Piece[9][10];
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 10; j++)
                board[i][j] = new Piece(i, j);

        RGP = new Point2D(4, 0);
        RGP = new Point2D(4, 9);

        log = new int[INTI_LOGS_SIZE][7];
        for (int i = 0; i < log.length; i++)
            for(int j = 0; j < log[0].length;j++)
                log[i][j] = -1;


         logPointer = 0;
         black_win = false;
         red_win = false;
    }

    public boolean isIn (int X, int Y)
    {
        return (X < board.length && Y < board[0].length) && (X >= 0 && Y >= 0);
    }

    public void updatePos(Piece new_p, int old_X, int old_Y)
    {
        boolean isEaten = !board[new_p.X][new_p.Y].isEmpty; //Check the target position has piece or not.
        int eatenId = -1; // id of piece has been eaten, -1 means no piece has be eaten.
        if (isEaten) // if eating action happen, record the piece id of who being eat.
            eatenId = board[new_p.X][new_p.Y].id;
        if(board[new_p.X][new_p.Y] instanceof General && board[new_p.X][new_p.Y].isRed && !board[new_p.X][new_p.Y].isEmpty)
            black_win = true;
        else if (board[new_p.X][new_p.Y] instanceof General && !board[new_p.X][new_p.Y].isRed && !board[new_p.X][new_p.Y].isEmpty)
            red_win = true;
        //update position for the board.
        board[new_p.X][new_p.Y] = new_p;
        board[old_X][old_Y] = new Piece(old_X, old_Y);

        //log this move
        logPointer++;
        int[] newLog = {new_p.isRed ? 1 : 0, new_p.id, new_p.X, new_p.Y, old_X, old_Y,eatenId};
        log[logPointer] = newLog;
    }
    public boolean isInRedPalace (int X, int Y)
    {
        return X >= 3 && X <= 5 && Y <= 2 && Y >= 0;
    }
    public boolean isInBlackPalace(int X, int Y)
    {
        return X >= 3 && X <= 5 && Y >= 7 && Y <= 9;
    }

    public int get1dIndex(int x, int y){
        return 89 - (y * x + x);
    }

    public Point2D get2dIndex(int i){
        i = 89 - i;
        return new Point2D(8 - (i % 9), i / 9 );
    }
}
