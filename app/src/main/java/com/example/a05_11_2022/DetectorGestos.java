package com.example.a05_11_2022;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

public class DetectorGestos extends GridView{

    private GestureDetector gdetector;
    private boolean confirmarMovimiento = false;
    private float mTouchX;
    private float mTouchY;
    private static final int SWIPE_MIN_DISTANCE = 100;
    private static final int SWIPE_MAX_OFF_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private static MediaPlayer pop;

    public DetectorGestos(Context context) {
        super(context);
        init(context);
    }

    public DetectorGestos(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DetectorGestos(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DetectorGestos(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        gdetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                pop = MediaPlayer.create(getContext(), R.raw.pop);
                final int posicion = DetectorGestos.this.pointToPosition(Math.round(e1.getX()), Math.round(e1.getY()));

                PuzzleFirstFragment puzzleFirstFragment = new PuzzleFirstFragment();

                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                        //implementar movimientos hacia arriba
                        puzzleFirstFragment.moverCeldas(context, puzzleFirstFragment.UP, posicion);
                        pop.start();
                    } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                        //implementar movimientos hacia abajo
                        puzzleFirstFragment.moverCeldas(context, puzzleFirstFragment.DOWN, posicion);
                        pop.start();

                    }
                } else {
                    if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                        //implementar movimientos hacia izquierda
                        puzzleFirstFragment.moverCeldas(context, puzzleFirstFragment.LEFT, posicion);
                        pop.start();

                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                        //implementar movimientos hacia derecha
                        puzzleFirstFragment.moverCeldas(context, puzzleFirstFragment.RIGHT, posicion);
                        pop.start();

                    }
                }

                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gdetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            confirmarMovimiento = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (confirmarMovimiento) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                confirmarMovimiento = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gdetector.onTouchEvent(ev);
    }
}
