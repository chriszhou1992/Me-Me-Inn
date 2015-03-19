package com.android.memeinn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MatchStartAnimView extends SurfaceView
        implements SurfaceHolder.Callback {

    /*Width and Height constants for the whole view*/
    private int W;
    private int H;

    /*Counters used in animation*/
    private int triWidth = 0;
    private int triHeight = 0;

    /*Points used to represent the triangles in animation*/
    Point a, b, c, d, e, f;
    /*Paths that tracks the outlines of the triangle*/
    private Path upperTriPath;
    private Path lowerTriPath;
    /*Colors for the triangles*/
    private Paint upperTriPaint;
    private Paint lowerTriPaint;

    /*Tracks the end of animation*/
    private Boolean isAnimationDone;

    public MatchStartAnimView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
        getHolder().addCallback(this);
        setFocusable(true);

        W = getWidth();
        H = getHeight();
        //Upper triangle
        a = new Point(0, 0);
        b = new Point();
        c = new Point();
        //Lower triangle
        d = new Point();
        e = new Point();
        f = new Point();

        upperTriPath = new Path();
        lowerTriPath = new Path();

        //Setup how to paint the triangles
        upperTriPaint = new Paint();
        upperTriPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        upperTriPaint.setColor(getResources().getColor(R.color.match_background_gray));

        lowerTriPaint = new Paint();
        lowerTriPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lowerTriPaint.setColor(getResources().getColor(R.color.logo_color));
        isAnimationDone = false;
    }

    /**
     *
     * @param canvas
     * @note http://stackoverflow.com/questions/22042603/android-how-to-create-triangle-and-rectangle-shape-programatically
     */
    @Override
    protected void onDraw(Canvas canvas) {
        W = getWidth();
        H = getHeight();
        //mPointedHeight is the length of the
        //triangle... in this case we have it dynamic and can be changed.
        triWidth = triWidth + 10 > W ? W : triWidth + 10;
        triHeight = triHeight + 10 > H ? H : triHeight + 10;
        Log.d("thread", "W = " + W + " H = " + H);
        Log.d("thread", "triW = " + triWidth + "triH = " + triHeight);
        isAnimationDone = triWidth == W && triHeight == H;
        Log.d("thread", "" + isAnimationDone);

        //animate upper triangle
        b.set(triWidth, 0);
        c.set(0, triHeight);

        //animate lower triangle
        d.set(W, H);
        e.set(W - triWidth, H);
        f.set(W, H - triHeight);

        //Move upper triangle
        upperTriPath.rewind();
        upperTriPath.moveTo(a.x, a.y);
        upperTriPath.lineTo(b.x, b.y);
        upperTriPath.lineTo(c.x, c.y);
        upperTriPath.lineTo(a.x, a.y);
        lowerTriPath.rewind();
        lowerTriPath.moveTo(d.x, d.y);
        lowerTriPath.lineTo(e.x, e.y);
        lowerTriPath.lineTo(f.x, f.y);
        lowerTriPath.lineTo(d.x, d.y);

        //Draw path on canvas
        canvas.drawPath(upperTriPath, upperTriPaint);
        canvas.drawPath(lowerTriPath, lowerTriPaint);

        if (!isAnimationDone)
            postInvalidate();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //animationThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        /*boolean retry = true;
        while (retry) {
            try {
                animationThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }*/
    }

    /**
     * Getter for animationDone flag.
     * @return Boolean A flag indicating whether the animation has finished.
     */
    public Boolean isAnimationDone() {
        return isAnimationDone;
    }
}
