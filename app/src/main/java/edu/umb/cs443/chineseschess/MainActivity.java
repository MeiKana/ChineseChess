package edu.umb.cs443.chineseschess;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ImageView;


public class MainActivity extends Activity {
    GridView gridView;
    boolean selected = false;
    int selectedIndex = -1;

    boolean twoPlayers = true;
    boolean redTrun;

//    private String[] numbers = new String[9*10];
    private ImageView[] numbers = new ImageView[9*10];

    Board board;

    Thread t1;
    MoveTask t2;

    private static int target;

    TextView debugger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        board = new Board();
        Game.standardInit(board);

        debugger = (TextView) findViewById(R.id.debugger);

        gridView = (GridView) findViewById(R.id.gridView);
        updateBoard();
        redTrun = true;

 //       ArrayAdapter<String> adapterS = new ArrayAdapter<String>(this,
 //               R.layout.selected_piece, numbers);

        ArrayAdapter<ImageView> adapter = new ArrayAdapter<ImageView>(this,
                R.layout.selected_piece, numbers);

        gridView.setAdapter(adapter);
//        gridView.setAdapter(adapterS);
//        gridView.setAdapter(new ImageAdapter(this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id){

                t2=new MoveTask();
                t2.execute(new Integer(position));

                if (!selected) {
                    selectPiece(position);
                    return;
                }
                else
                    movePiece(position);
                ((ArrayAdapter) gridView.getAdapter()).notifyDataSetChanged();
            }
        });

        t1=new Thread(new Runnable(){
            public void run(){
                while(true) {
                    gridView.post(new Runnable() {
                        public void run() {
                            ((ArrayAdapter) gridView.getAdapter()).notifyDataSetChanged();
                        }
                    });
                }
            }
        }

        );
     //   t1.start();
    }

    private void selectPiece(int pos){
        int X = get2dIndex(pos).X;
        int Y = get2dIndex(pos).Y;
        if(board.board[X][Y].isEmpty || board.board[X][Y].isRed != redTrun)
            return;
        else{
            selected = true;
            selectedIndex = pos;
            return;
        }

    }
    private void movePiece(int pos){
        int lastX = get2dIndex(selectedIndex).X;
        int lastY = get2dIndex(selectedIndex).Y;
        int X = get2dIndex(pos).X;
        int Y = get2dIndex(pos).Y;

        if (board.board[lastX][lastY].move(X, Y,board)){
            updateBoard();
            selected = false;
            redTrun = !redTrun;
        }
        else{
            selected = false;
        }
    }

    private class MoveTask extends AsyncTask<Integer, Void, Void>{
        @Override
        protected Void doInBackground(Integer... params) {
            target=params[0].intValue();
            Log.i("myTag", params[0].toString());

            publishProgress();

            return null;
        }

        protected void onProgressUpdate(Void... values) {
            ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();
        }
    }

    private int get1dIndex(int x, int y){
        return 89 - (y * x + x);
    }

    private Point2D get2dIndex(int i){
        i = 89 - i;
        return new Point2D(8 - (i % 9), i / 9 );
    }
/*
    private void updateBoard(){
        for (int i = 0; i < numbers.length; i++) {
            Point2D indexs = get2dIndex(i);
            numbers[i] = board.board[indexs.X][indexs.Y].toString();
        }
    } */

    private void updateBoard(){
        ImageView copy;
        String copy2;
        for (int i = 0; i < numbers.length; i++){
            Point2D indexs = get2dIndex(i);
            copy2 = board.board[indexs.X][indexs.Y].toString();
            switch(copy2){
                case "仕":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_advisor_red);
                    copy.setImageResource(R.drawable.advisor_red);
                    break;
                case "士":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_advisor_black);
                    copy.setImageResource(R.drawable.advisor_black);
                    break;
                case "炮":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_cannon_red);
                    copy.setImageResource(R.drawable.cannon_red);
                    break;
                case "砲":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_cannon_black);
                    copy.setImageResource(R.drawable.cannon_black);
                    break;
                case "相":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_elephant_red);
                    copy.setImageResource(R.drawable.elephant_red);
                    break;
                case "象":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_elephant_black);
                    copy.setImageResource(R.drawable.elephant_black);
                    break;
                case "帥":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_general_red);
                    copy.setImageResource(R.drawable.general_red);
                    break;
                case "將":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_general_black);
                    copy.setImageResource(R.drawable.general_black);
                    break;
                case "马":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_horse_red);
                    copy.setImageResource(R.drawable.horse_red);
                    break;
                case "馬":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_horse_black);
                    copy.setImageResource(R.drawable.horse_black);
                    break;
                case "车":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_rook_red);
                    copy.setImageResource(R.drawable.rook_red);
                    break;
                case "車":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_rook_black);
                    copy.setImageResource(R.drawable.rook_black);
                    break;
                case "兵":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_solider_red);
                    copy.setImageResource(R.drawable.solider_red);
                    break;
                case "卒":
                    numbers[i] = copy = (ImageView) findViewById(R.id.image_solider_black);
                    copy.setImageResource(R.drawable.solider_black);
                    break;
                default:
                    break;
            }
            //numbers[i] = board.board[indexs.X][indexs.Y].toString();
        }
    }

    public void unDo (View view) {
        Game.unDo(board);
        updateAndNotify();

    }

    private void updateAndNotify(){
        updateBoard();
        ((ArrayAdapter)gridView.getAdapter()).notifyDataSetChanged();

    }

    public void init(View view){
        this.board = new Board();
        Game.standardInit(board);
        redTrun = true;
        updateAndNotify();
    }
}
