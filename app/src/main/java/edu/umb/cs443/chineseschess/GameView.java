package edu.umb.cs443.chineseschess;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.View;
import android.content.Context;
import android.view.WindowManager;
import android.widget.ImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class GameView extends SurfaceView {
    Canvas canvas;
    Context con;
    private SurfaceHolder holder;

    WindowManager wm;

    Rect boardEdge;

    int screenWidth;
    int screenheight;
    int gap;
    int edgeSize;
    int endPos;

    Paint paint;
    int boardVertiPosOffset;

    Board board;
    boolean redTrun = true;
    Point2D selectedIndex;
    boolean selected;

    int buttonHeight;

    public GameView (Context context){
        super(context);
        con = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenheight = dm.heightPixels;
        holder = getHolder();
        paint = new Paint();
        buttonHeight = screenheight/15;

        board = new Board();
        selected = false;
        Game.standardInit(board);
        selectedIndex = new Point2D(-1, -1 );

        holder.addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder){
                gap = screenWidth/10;
                edgeSize = screenWidth/10;
                endPos = screenWidth - edgeSize;
                canvas = holder.lockCanvas();
                onDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2){

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder){

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            int X = toBoardPos((int)event.getX(),(int)event.getY()).X;
            int Y = toBoardPos((int)event.getX(),(int)event.getY()).Y;

           if (X != -1){
                try {
                    if (!selected) {
                        selectPiece(X, Y);
                        return super.onTouchEvent(event);
                    } else {
                        movePiece(canvas, X, Y);
                    }
                }
                catch (Exception e){e.printStackTrace();}
            }
            else if (buttomPressed(event.getX(),event.getY()) == 1 && X == -1)
                unDo(board);
            else if (buttomPressed(event.getX(),event.getY()) == 0 && X == -1)
                newGame(board);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(this.canvas);
        canvas.drawColor(Color.BLACK);

        drawBoard(this.canvas,0, 0);
        drawPiece(this.canvas,0, 0);
        drawUI();
    }

    private void drawUI(){
        Paint buttonPaint = new Paint();
        buttonPaint.setColor(Color.RED);

        this.canvas.drawRect(0,screenheight - 2 * buttonHeight,screenWidth,screenheight - 3 * buttonHeight,buttonPaint);
        buttonPaint.setColor(Color.WHITE);
        buttonPaint.setTextSize(70);
        this.canvas.drawText("New",screenWidth/2 - gap,(float) (screenheight - 2.2 * buttonHeight),buttonPaint);
        buttonPaint.setColor(Color.GRAY);
        this.canvas.drawRect(0,screenheight - 3 * buttonHeight,screenWidth,screenheight - 4 * buttonHeight,buttonPaint);
        buttonPaint.setColor(Color.WHITE);
        this.canvas.drawText("Undo",screenWidth/2 - gap,(float) (screenheight - 3.2 * buttonHeight),buttonPaint);
    }

    private void drawPiece(Canvas canvas, int offsetX, int offsetY){
        Paint wordPaint = new Paint();
        wordPaint.setColor(Color.WHITE);
        wordPaint.setTextSize(70);
        int textPosOffsetX = (int) (-gap / 2.9);
        int textPosOffsetY = (int ) (gap/3.6);
        for (int i = 0; i < board.board.length; i++){
            for(int j = 0; j < board.board[0].length;j++){
                if(!board.board[i][j].isEmpty && board.board[i][j].isRed) {
                    paint.setColor(Color.RED);
                    Point2D canvasPos = toCanvasPos(i, j);
                    canvas.drawCircle(canvasPos.X, canvasPos.Y, gap/2, paint);
                    canvas.drawText(board.board[i][j].toString(),canvasPos.X + textPosOffsetX, canvasPos.Y + textPosOffsetY,wordPaint);
                }
                else if(!board.board[i][j].isEmpty && !board.board[i][j].isRed) {
                    paint.setColor(Color.rgb(0,0,0));
                    Point2D canvasPos = toCanvasPos(i, j);
                    canvas.drawCircle(canvasPos.X, canvasPos.Y, gap/2, paint);
                    canvas.drawText(board.board[i][j].toString(),canvasPos.X + textPosOffsetX, canvasPos.Y + textPosOffsetY,wordPaint);
                }
            }
        }
    }

    private void drawBoard(Canvas canvas, int offsetX,int offsetY){
        double boardRatio = 9.0/10;
        boardEdge = new Rect(offsetX,offsetY,screenWidth,(int) (screenWidth / boardRatio) );
        paint.setStrokeWidth(3);
        paint.setColor(Color.WHITE);
        canvas.drawRect(boardEdge,paint);

        for (int i = 1 ; i < 11; i++){
            paint.setColor((Color.BLACK));
            //draw horizontal lines
            float hStartX = edgeSize + offsetX;
            float hStartY = 0 + i * gap + offsetY;
            float hEndX = endPos + offsetX;
            float hEndY = 0 + i * gap + offsetY;
            canvas.drawLine(hStartX, hStartY,hEndX, hEndY, paint);

            if (i > 1 && i <9){
                //Vertical lines for black side.
                float vStartX = gap * i + offsetX;
                float vStartY = edgeSize + offsetY;
                float vEndX = gap * i + offsetX;
                float vEndY = 5 * gap + offsetY;
                canvas.drawLine(vStartX,vStartY,vEndX,vEndY,paint);
                //Vertical line for red side.
                vStartY = 6 * gap + offsetY;
                vEndY = 10 * gap + offsetY;
                canvas.drawLine(vStartX,vStartY,vEndX,vEndY,paint);
            }
            else if (i == 1|| i == 9){
                //Edge of board.
                float vStartX =  gap * i + offsetX;
                float vStartY = edgeSize + offsetY;
                float vEndX =  gap * i + offsetX;
                float vEndY = 10 * gap + offsetY;
                canvas.drawLine(vStartX,vStartY,vEndX,vEndY,paint);
            }
        }
        drawPalaceLines(canvas,offsetX,offsetY);
    }
    private void drawPalaceLines(Canvas canvas,int offsetX,int offsetY) {
        //draw palace lines
        canvas.drawLine(edgeSize + gap * 3 + offsetX, edgeSize + offsetY, edgeSize + gap * 5 + offsetX, edgeSize + 2 * gap + offsetY, paint);
        canvas.drawLine(edgeSize + gap * 5 + offsetX, edgeSize + offsetY, edgeSize + gap * 3 + offsetX, edgeSize + 2 * gap + offsetY, paint);
        canvas.drawLine(edgeSize + gap * 3 + offsetX, edgeSize + gap * 9 + offsetY, edgeSize + gap * 5 + offsetX, edgeSize + 7 * gap + offsetY, paint);
        canvas.drawLine(edgeSize + gap * 3 + offsetX, edgeSize + gap * 7 + offsetY, edgeSize + gap * 5 + offsetX, edgeSize + 9 * gap + offsetY, paint);
    }
    private Point2D toBoardPos (int X, int Y){
        if (X > screenWidth || X < gap || Y > screenWidth/(9.0/10) || Y < 0)
            return new Point2D(-1,-1);

        double boardX, boardY;
        boardX = Math.round((double)(X) / gap) - 1;
        boardY = Math.round(((double)Y ) / gap);
        return new Point2D((int)boardX, (int)(10 - boardY));
    }

    private Point2D toCanvasPos(int X, int Y){
        if (X > board.board.length || X < 0 || Y > board.board[0].length || Y < 0)
            return new Point2D(-1,-1);

        int canvasX, canvasY;
        canvasX = edgeSize +  X * gap ;
        canvasY = (10 - Y) * gap ;
        return new Point2D(canvasX, canvasY);
    }

    private void movePiece(Canvas canvas,int X,int Y){
        int lastX = selectedIndex.X;
        int lastY = selectedIndex.Y;

        if (board.board[lastX][lastY].move(X, Y,board)){
            selected = false;
            updateScreen();
            redTrun = !redTrun;
            if (redTrun)
                Toast.makeText(con,"Red's Trun Now!",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(con,"Black's Trun Now!",Toast.LENGTH_SHORT).show();
            if(board.red_win)
                Toast.makeText(con,"Red Win! Press New To Restart",Toast.LENGTH_SHORT).show();
            else if(board.red_win)
                Toast.makeText(con,"Black Win! Press New To Restart",Toast.LENGTH_SHORT).show();
        }
        else{
            selected = false;
        }
    }

    private void updateScreen(){
        canvas = holder.lockCanvas();
        onDraw(canvas);
        holder.unlockCanvasAndPost(canvas);
    }

    private void selectPiece(int X, int Y){
        if(board.board[X][Y].isEmpty || board.board[X][Y].isRed != redTrun)
            return;
        else{
            selected = true;
            selectedIndex = new Point2D(X,Y);
            return;
        }

    }

    private int buttomPressed(float X,float Y){
        if (Y < screenheight - 2 * buttonHeight && Y > screenheight - 3 * buttonHeight) //New
            return 0;
        else if (Y < screenheight - 3 * buttonHeight && Y > screenheight - 4 * buttonHeight) //Undo
            return 1;
        else
            return -1;
    }

    private void unDo(Board board){
        Game.unDo(board);
        updateScreen();
        Toast ts = Toast.makeText(con,"Back to last turn...",Toast.LENGTH_SHORT);
        ts.show();
    }

    private void newGame(Board board){
        redTrun = true;
        selected = false;
        Game.standardInit(board);
        Toast ts = Toast.makeText(con,"New Game",Toast.LENGTH_SHORT);
        ts.show();
        updateScreen();
    }
}
