package winf114.waksh.de.frogger;

import winf114.waksh.de.frogger.util.SystemUiHider;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.SurfaceView;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Display;
import android.graphics.Point;



public class GameActivity extends Activity implements SurfaceHolder.Callback{

    private MainThread mainThread;
    private Frosch frosch;
    private Auto auto;
    private Auto lkw;
    private GameHintergrund hintergrund;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    TextView textView;
    private int breite;
    private int hoehe;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        mainThread = new MainThread(surfaceHolder, this);
        frosch = new Frosch(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), 800,400,10,10);
        auto = new Auto(10,10,10,10,10);
        lkw = new Auto(10,50,10,10,5);

        mainThread.spielobjekte.add(auto);
        mainThread.spielobjekte.add(lkw);


        // 4 Knöpfe und ein Test-Textfeld

        textView = (TextView) findViewById(R.id.textView1);

        Button linksButton = (Button) findViewById(R.id.links);
        linksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Links");
                frosch.move(richtung.links);
            }
        });

        Button rechtsButton = (Button) findViewById(R.id.rechts);
        rechtsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Rechts");
                frosch.move(richtung.rechts);
            }
        });

        Button untenButton = (Button) findViewById(R.id.unten);
        untenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Unten");
                frosch.move(richtung.zurueck);
            }
        });

        Button obenButton = (Button) findViewById(R.id.oben);
        obenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                textView.setText("Oben");
                frosch.move(richtung.vor);
            }
        });
    }

    // SurfaceView Methoden
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                mainThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        String breiteSTR = Integer.toString(width);
        String hoeheSTR = Integer.toString(height);

        textView.setText(hoeheSTR + ":" + breiteSTR);


        hintergrund = new GameHintergrund(width,height);
    }

    @Override
    public void onBackPressed() {
        mainThread.setRunning(false);
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        mainThread.setRunning(false);
        super.onPause();
    }

    @Override
    public void onStop(){
        mainThread.setRunning(false);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mainThread.setRunning(false);
        super.onDestroy();
    }



    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        hintergrund.draw(canvas);
        frosch.draw(canvas);
        auto.draw(canvas);
        lkw.draw(canvas);

    }
}
