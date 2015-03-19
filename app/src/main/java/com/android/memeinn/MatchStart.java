package com.android.memeinn;


import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;

public class MatchStart extends Activity implements Runnable {

    //private Thread animationThread;
    private MatchStartAnimView animatedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.matchstart);
        animatedView = new MatchStartAnimView(this);
        setContentView(animatedView);
        //animationThread = new Thread(this);
        //animationThread.start();
    }

    @Override
    public void run() {
        SurfaceHolder holder = animatedView.getHolder();
        while (!animatedView.isAnimationDone()) {
            Canvas canvas = holder.lockCanvas();
            if (canvas != null) {
                animatedView.draw(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("thread", "running...");
        }
    }
}
