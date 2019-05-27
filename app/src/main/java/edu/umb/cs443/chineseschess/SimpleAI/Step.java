package edu.umb.cs443.chineseschess.SimpleAI;

import edu.umb.cs443.chineseschess.Board;
import edu.umb.cs443.chineseschess.Point2D;
import edu.umb.cs443.chineseschess.piece.Piece;

public class Step {
    Point2D to;
    Point2D from;
    int h_func;
    Piece piece;

    public Step(Piece pi, Point2D to, int h_func){
        if(pi.isEmpty) throw new IllegalArgumentException();
        this.h_func = h_func;
        this.to = to;
        this.piece = pi;
        from = new Point2D(pi.X, pi.Y);
    }
}
