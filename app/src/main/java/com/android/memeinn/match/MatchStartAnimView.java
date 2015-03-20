package com.android.memeinn.match;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.memeinn.FirebaseSingleton;
import com.android.memeinn.R;
import com.android.memeinn.Utility;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.ParseUser;

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
    /*Paints for the triangles*/
    private Paint upperTriPaint;
    private Paint lowerTriPaint;
    /*Paints for the username texts*/
    private Paint onlineUserPaint;
    private Paint offlineUserPaint;
    /*Paint for circle*/
    private Paint circlePaint;
    /*Paint for count down text*/
    private Paint countDownTextPaint;
    private Rect textBoundRect;

    /*Tracks the end of animation*/
    private Boolean isAnimationDone;

    private String opponentName = null;
    private Boolean isMatchReady;

    private Integer countDown;

    public MatchStartAnimView(Context context, String opponentName) {
        super(context);
        setBackgroundColor(Color.BLACK);
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
        setupPaintsForTriangles();
        //Setup how to paint username
        setupPaintsForUsers();
        //Setup how to paint countdown
        setupPaintsForCountDown();

        isMatchReady = false;
        textBoundRect = new Rect();
        isAnimationDone = false;
        this.opponentName = opponentName;

        //Check opponent status and set whether match is ready accordingly
        checkMatchStatus();
        countDown = 15;
    }

    /**
     * Direct check of the existence of data at the URI. If the data exists, then
     * opponent is ready, so update isMatchReady flag.
     */
    private void checkMatchStatus() {
        Firebase opponentStatusRef = FirebaseSingleton.getInstance("matches/" + Utility
                .combineStringSorted(
                        ParseUser.getCurrentUser().getUsername(), opponentName)).child(opponentName);
        opponentStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isMatchReady = dataSnapshot.exists();
                Log.d("match", "snapshot exists");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    /**
     * Method to initialize Paint objects for the upper and lower triangles in the animation.
     */
    private void setupPaintsForTriangles() {
        upperTriPaint = new Paint();
        upperTriPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        upperTriPaint.setColor(getResources().getColor(R.color.match_background_gray));

        lowerTriPaint = new Paint();
        lowerTriPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lowerTriPaint.setColor(getResources().getColor(R.color.logo_color));
    }

    /**
     * Method to initialize Paint objects for the usernames.
     */
    private void setupPaintsForUsers() {
        onlineUserPaint = new Paint();
        onlineUserPaint.setColor(Color.GREEN);
        onlineUserPaint.setTextSize(36);

        offlineUserPaint = new Paint();
        offlineUserPaint.setColor(Color.BLUE);
        offlineUserPaint.setTextSize(36);
    }

    /**
     * Method to initialize Paint objects for the circle and text in the
     * countdown circle.
     */
    private void setupPaintsForCountDown() {
        circlePaint = new Paint();
        upperTriPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setColor(Color.BLACK);

        countDownTextPaint = new Paint();
        countDownTextPaint.setStyle(Paint.Style.STROKE);
        countDownTextPaint.setColor(Color.WHITE);
        countDownTextPaint.setTextSize(48);
        Typeface tf = Typeface.create("Serif",Typeface.BOLD_ITALIC);
        countDownTextPaint.setTypeface(tf);
    }

    /**
     * Method which is triggered whenever the interface needs to be drawn/re-drawn.
     * Performs animations by updating private counters that keep track of W and H of triangles.
     * @param canvas Canvas The canvas to draw upon.
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

        //animate triangles
        drawTriangles(canvas);
        //draw usernames
        drawUsers(canvas);
        //draw countdown circle
        drawCountDownCircle(canvas);
        if (!isAnimationDone)
            postInvalidate();
    }

    /**
     * Draw representation of the two competing Users on the screen.
     * @param canvas Canvas The canvas to draw upon.
     */
    private void drawUsers(Canvas canvas) {
        canvas.drawText(ParseUser.getCurrentUser().getUsername(), triWidth/4, triHeight/4,
                onlineUserPaint);
        canvas.drawText(opponentName, W - triWidth/4, H - triHeight/4,
                isMatchReady ? onlineUserPaint : offlineUserPaint);
    }

    /**
     * Draw the circle that tracks the countdown seconds before the match request
     * is automatically terminated.
     * @param canvas Canvas The canvas to draw upon.
     */
    private void drawCountDownCircle(Canvas canvas) {
        /*Draw countdown circle*/
        canvas.drawCircle(W/2, H/2, 50, circlePaint);
        /*Draw countdown text centered at the center of the circle
         *from http://stackoverflow.com/questions/14052013/number-inside-circle */
        String countDownString = countDown.toString();
        if (isMatchReady)
            countDownString = "GO!";
        countDownTextPaint.getTextBounds(countDownString, 0, countDownString.length(), textBoundRect);
        float x = W/2 - textBoundRect.width() / 2;
        Paint.FontMetrics fm = countDownTextPaint.getFontMetrics();
        float y = H/2 - (fm.descent + fm.ascent) / 2;
        canvas.drawText(countDownString, x, y, countDownTextPaint);
    }

    /**
     * Draw the upper and lower triangles which make up the animation.
     * @param canvas Canvas The canvas to draw upon.
     */
    private void drawTriangles(Canvas canvas) {
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
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

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

    /**
     * Setter method for isMatchReady..
     * @param acceptedMatch Boolean A boolean indicating whether opponent is ready..
     */
    public void setOpponentStatus(Boolean acceptedMatch) {
        isMatchReady = acceptedMatch;
        postInvalidate();
    }

    /**
     * Method used to update countdown integer.
     */
    public int decrementCountdown() {
        countDown--;
        return countDown;
    }
}
