package com.chanonly123.airhockey2d;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public final class MySurfaceView extends SurfaceView implements Callback {
    static boolean threadPauseRequest;
    private int count;
    boolean created;
    public boolean down;
    Hockey game;
    public String msg;
    private int screenHeight;
    private int screenWidth;
    private SurfaceHolder sh;
    private Thread f3t;
    public boolean up;
    public float x1;
    public float x2;
    public float y1;
    public float y2;

    /* renamed from: com.example.drawing3.MySurfaceView.1 */
    class C00501 implements OnTouchListener {
        C00501() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            int d = MySurfaceView.this.screenHeight / 2;
            MySurfaceView.this.count = event.getPointerCount();
            if (MySurfaceView.this.count > 0) {
                if (event.getY(0) < ((float) d)) {
                    MySurfaceView.this.x1 = event.getX(0);
                    MySurfaceView.this.y1 = event.getY(0);
                } else if (event.getY(0) > ((float) d)) {
                    MySurfaceView.this.x2 = event.getX(0);
                    MySurfaceView.this.y2 = event.getY(0);
                }
            }
            if (MySurfaceView.this.count > 1) {
                if (event.getY(1) < ((float) d)) {
                    MySurfaceView.this.x1 = event.getX(1);
                    MySurfaceView.this.y1 = event.getY(1);
                } else {
                    MySurfaceView.this.x2 = event.getX(1);
                    MySurfaceView.this.y2 = event.getY(1);
                }
            }
            if (event.getAction() == 1) {
                MySurfaceView.this.down = false;
                MySurfaceView.this.up = true;
            }
            if (event.getAction() == 0) {
                MySurfaceView.this.down = true;
                MySurfaceView.this.up = false;
            }
            return true;
        }
    }

    public MySurfaceView(MainActivity context) {
        super(context);
        this.created = false;
        this.up = false;
        this.down = false;
        this.msg = "no";
        this.count = 0;
        this.sh = getHolder();
        this.sh.addCallback(this);
        setOnTouchListener(new C00501());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.msg("surface creating");
        if (!this.created) {
            this.screenWidth = getWidth();
            this.screenHeight = getHeight();
            this.created = true;
            Log.msg("surface created 1st time");
            this.game = new Hockey(this, this.sh, this.screenWidth, this.screenHeight);
        }
        threadPauseRequest = false;
        this.f3t = new Thread(this.game);
        this.f3t.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.msg("surface changing");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.msg("surface destroying");
        threadPauseRequest = true;
        boolean retry = true;
        while (retry) {
            try {
                this.f3t.join();
                retry = false;
            } catch (Exception e) {
                Log.msg("Thread Joining!");
            }
        }
    }
}
