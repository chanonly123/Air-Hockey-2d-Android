package com.chanonly123.airhockey2d;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.view.SurfaceHolder;

import java.util.Random;

public final class Hockey implements Runnable {
    static int STATE = 0;
    static final int STATE_EXIT = 4;
    static final int STATE_GAME = 3;
    static final int STATE_MAIN = 1;
    static final int STATE_PAUSED = 2;
    private static final int WIN_COM = 1;
    private static final int WIN_NONE = 3;
    private static final int WIN_PLAYER = 2;
    private static Button butt1 = null;
    private static Button butt2 = null;
    private static Button butt3 = null;
    private static Button butt4 = null;
    private static Button butt5 = null;
    private static final int fps = 60;
    static boolean gamePauseRequest = false;
    private static int height = 0;
    static int level = 0;
    private static Paint paintGoal1 = null;
    private static Paint paintGoal2 = null;
    private static long start = 0;
    private static final int target = 16;
    private static Paint textPaint;
    private static Paint textPaint1;
    private static Paint textPaint2;
    static boolean vsCom;
    private static long wait;
    private static int width;
    private Bitmap background;
    private Ball ball;
    private Ball com;
    private int current_scorer;
    private Bitmap mainMenuImg;
    private Vector2D mouse1;
    private Vector2D mouse2;
    Paint paintDark;
    private Ball player;
    int result;
    private int score_com;
    private int score_player;
    private int score_to_win;
    private final SurfaceHolder sh;
    private Bitmap temp;
    private int time_after_goal;
    private final MySurfaceView win;

    static {
        STATE = WIN_COM;
    }

    public Hockey(MySurfaceView surfaceView, SurfaceHolder sh, int w, int h) {
        this.mouse1 = new Vector2D(0.0f, 0.0f);
        this.mouse2 = new Vector2D(0.0f, 0.0f);
        this.score_to_win = WIN_PLAYER;
        STATE = WIN_COM;
        this.win = surfaceView;
        this.sh = sh;
        width = w;
        height = h;
        Sound.loadAll();
        this.temp = BitmapFactory.decodeResource(this.win.getResources(), R.drawable.main_and);
        this.mainMenuImg = Bitmap.createScaledBitmap(this.temp, width, height, false);
        int size = width / 5;
        int pos = (height * WIN_PLAYER) / WIN_NONE;
        butt1 = new Button(width / STATE_EXIT, pos, size);
        butt1.setBitmaps(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_com_01), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_com_02), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_com_03));
        butt2 = new Button((width * WIN_NONE) / STATE_EXIT, pos, size);
        butt2.setBitmaps(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_player_01), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_player_02), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_player_03));
        butt3 = new Button(width / WIN_PLAYER, (height * 5) / 6, size);
        butt3.setBitmaps(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_quit_01), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_quit_02), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_quit_03));
        butt4 = new Button(width / STATE_EXIT, pos, width / STATE_EXIT);
        butt4.setBitmaps(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_resume_01), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_resume_02), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_resume_03));
        butt5 = new Button((width * WIN_NONE) / STATE_EXIT, pos, width / STATE_EXIT);
        butt5.setBitmaps(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_main_01), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_main_02), BitmapFactory.decodeResource(this.win.getResources(), R.drawable.button_main_03));
        this.paintDark = new Paint();
        this.paintDark.setColor(Color.argb(150, 0, 0, 0));
        setRandomBoard();
        this.ball = new Ball(height / 24, null);
        this.ball.setBitmap(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.img_ball));
        this.ball.setBounds(0, 0, width, height);
        this.ball.initBall(100.0f, 100.0f, 5.0f, 2.0f);
        this.com = new Ball(height / target, this.mouse1);
        this.com.setBitmap(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.img_com));
        this.com.setBounds(0, 0, width, (height / WIN_PLAYER) - (this.ball.radius / WIN_PLAYER));
        this.com.initBall((float) (width / WIN_PLAYER), (float) (height / WIN_NONE), 0.0f, 0.0f);
        this.player = new Ball(height / target, this.mouse2);
        this.player.setBitmap(BitmapFactory.decodeResource(this.win.getResources(), R.drawable.img_player));
        this.player.setBounds(width / WIN_PLAYER, (height / WIN_PLAYER) + (this.ball.radius / WIN_PLAYER), width, height);
        this.player.initBall((float) (width / WIN_PLAYER), (float) ((height * WIN_PLAYER) / WIN_NONE), 0.0f, 0.0f);
        Ball.setGoal(0, 0, width, height, (int) (((double) width) / 1.9d));
        textPaint = new Paint(WIN_COM);
        textPaint.setColor(-16711681); //
        textPaint.setTextAlign(Align.CENTER);
        textPaint.setTypeface(Typeface.SERIF);
        textPaint1 = new Paint(WIN_COM);
        textPaint1.setTextSize((float) (width / 8));
        textPaint1.setColor(SupportMenu.CATEGORY_MASK);
        textPaint1.setTextAlign(Align.RIGHT);
        textPaint2 = new Paint(WIN_COM);
        textPaint2.setTextSize((float) (width / 8));
        textPaint2.setColor(SupportMenu.CATEGORY_MASK);
        textPaint2.setTextAlign(Align.LEFT);
        paintGoal1 = new Paint();
        paintGoal1.setStyle(Style.STROKE);
        paintGoal1.setStrokeWidth((float) (width / 30));
        paintGoal2 = new Paint(paintGoal1);
        paintGoal2.setColor(SupportMenu.CATEGORY_MASK);
        paintGoal1.setColor(-16776961);
        resetGame();
    }

    private void resetGame() {
        level = WIN_COM;
        this.score_com = 0;
        this.score_player = 0;
        resetBalls();
    }

    private void resetScoreLevelUp() {
        this.score_com = 0;
        this.score_player = 0;
        level += WIN_COM;
    }

    private void setRandomBoard() {
        int id;
        switch (new Random().nextInt(10)) {
            case 0 /*0*/:
                id = R.drawable.board_back0;
                break;
            case WIN_COM /*1*/:
                id = R.drawable.board_back1;
                break;
            case WIN_PLAYER /*2*/:
                id = R.drawable.board_back2;
                break;
            case WIN_NONE /*3*/:
                id = R.drawable.board_back3;
                break;
            case STATE_EXIT /*4*/:
                id = R.drawable.board_back4;
                break;
            default:
                id = R.drawable.board_back0;
                break;
        }
        this.temp = BitmapFactory.decodeResource(this.win.getResources(), id);
        this.background = Bitmap.createScaledBitmap(this.temp, width, height, false);
    }

    public void run() {
        Log.msg("Thread starting");
        while (!MySurfaceView.threadPauseRequest) {
            Log.msg("state : " + STATE);
            switch (STATE) {
                case WIN_COM /*1*/:
                    mainMenu();
                    resetGame();
                    fadeOutMain(WIN_COM);
                    break;
                case WIN_PLAYER /*2*/:
                    pauseMenu();
                    if (STATE != WIN_COM) {
                        break;
                    }
                    fadeOutPause(WIN_COM);
                    break;
                case WIN_NONE /*3*/:
                    if (vsCom) {
                        fadeOutString("Level " + level, WIN_PLAYER, width / 6, width / STATE_EXIT);
                        fadeOutString("GO", WIN_COM, width / 6, width / WIN_PLAYER);
                    } else {
                        fadeOutString("GO", WIN_COM, width / 6, width / WIN_PLAYER);
                    }
                    setRandomBoard();
                    this.result = resume();
                    if (vsCom) {
                        if (this.result != WIN_COM) {
                            if (this.result != WIN_PLAYER) {
                                STATE = WIN_PLAYER;
                                break;
                            }
                            showString("YOU WIN", WIN_NONE, width / 5);
                            resetScoreLevelUp();
                            break;
                        }
                        showString("YOU LOSE", WIN_NONE, width / 5);
                        STATE = WIN_PLAYER;
                        break;
                    }
                    STATE = WIN_PLAYER;
                    break;
                case STATE_EXIT /*4*/:
                    MainActivity.context.finish();
                    MySurfaceView.threadPauseRequest = true;
                    break;
                default:
                    break;
            }
        }
        Log.msg("Thread ending");
    }

    void resetBalls() {
        if (this.current_scorer == WIN_PLAYER) {
            this.ball.initBall(new Vector2D((float) (width / WIN_PLAYER), (float) (((double) height) * 0.6d)), new Vector2D(0.0f, 0.0f));
        } else if (this.current_scorer == WIN_COM) {
            this.ball.initBall(new Vector2D((float) (width / WIN_PLAYER), (float) (((double) height) * 0.4d)), new Vector2D(0.0f, 0.0f));
        } else {
            this.ball.initBall(new Vector2D((float) (width / WIN_PLAYER), -1.0f + ((float) (((double) height) * 0.5d))), new Vector2D(0.0f, 0.0f));
        }
        this.com.initBall(new Vector2D((float) (width / WIN_PLAYER), (float) (((double) height) * 0.2d)), new Vector2D(0.0f, 0.0f));
        this.player.initBall(new Vector2D((float) (width / WIN_PLAYER), (float) (((double) height) * 0.8d)), new Vector2D(0.0f, 0.0f));
        this.win.x1 = this.com.pos.f4x;
        this.win.y1 = this.com.pos.f5y;
        this.win.x2 = this.player.pos.f4x;
        this.win.y2 = this.player.pos.f5y;
        this.time_after_goal = 0;
    }

    private int resume() {
        Log.msg("resumed");
        this.win.x1 = this.com.pos.f4x;
        this.win.y1 = this.com.pos.f5y;
        this.win.x2 = this.player.pos.f4x;
        this.win.y2 = this.player.pos.f5y;
        start = System.currentTimeMillis();
        while (!MySurfaceView.threadPauseRequest) {
            try {
                if (MainActivity.backPressed) {
                    MainActivity.backPressed = false;
                    return WIN_NONE;
                }
                if (vsCom) {
                    calculationForAI();
                } else {
                    this.mouse1.set(this.win.x1, this.win.y1);
                    this.com.setPlayerVelocity();
                }
                this.mouse2.set(this.win.x2, this.win.y2);
                this.player.setPlayerVelocity();
                this.ball.updatePosition();
                this.player.updatePosition();
                this.com.updatePosition();
                if (!vsCom) {
                    this.com.checkBounds();
                }
                this.player.checkBounds();
                this.player.chekcBallCollision(this.ball);
                this.com.chekcBallCollision(this.ball);
                if (this.time_after_goal == WIN_COM) {
                    resetBalls();
                }
                if (this.time_after_goal == 0) {
                    this.current_scorer = this.ball.checkWallCollition();
                    switch (this.current_scorer) {
                        case WIN_COM /*1*/:
                            this.score_player += WIN_COM;
                            this.time_after_goal = 180;
                            Sound.play(Sound.goal2);
                            break;
                        case WIN_PLAYER /*2*/:
                            this.score_com += WIN_COM;
                            this.time_after_goal = 180;
                            Sound.play(Sound.goal1);
                            break;
                    }
                }
                if (this.time_after_goal > 0) {
                    this.time_after_goal--;
                }
                if (this.score_com == this.score_to_win && this.time_after_goal < fps) {
                    return WIN_COM;
                }
                if (this.score_player == this.score_to_win && this.time_after_goal < fps) {
                    return WIN_PLAYER;
                }
                Canvas c = this.sh.lockCanvas();
                gameCustomDrawing(c);
                if (c != null) {
                    this.sh.unlockCanvasAndPost(c);
                }
                waitForNextTimeSlice();
            } catch (Exception e) {
            }
        }
        return WIN_NONE;
    }

    private void gameCustomDrawing(Canvas c) throws Exception {
        c.drawBitmap(this.background, 0.0f, 0.0f, null);
        c.drawText(String.valueOf(this.score_com), (float) width, (float) (height / WIN_PLAYER), textPaint1);
        c.drawText(String.valueOf(this.score_player), 0.0f, (float) (height / WIN_PLAYER), textPaint2);
        c.drawLine((float) Ball.goal_min, 0.0f, (float) Ball.goal_max, 0.0f, paintGoal1);
        c.drawLine((float) Ball.goal_min, (float) height, (float) Ball.goal_max, (float) height, paintGoal2);
        this.com.draw(c);
        this.ball.draw(c);
        this.player.draw(c);
    }

    private void pauseMenu() {
        boolean clicked = false;
        start = System.currentTimeMillis();
        this.win.up = false;
        Sound.play(Sound.pause);
        Log.msg(this.result + " pause");
        while (!MySurfaceView.threadPauseRequest && !clicked) {
            try {
                boolean backPressed = MainActivity.backPressed;
                MainActivity.backPressed = false;
                if (!backPressed) {
                    if (this.result == WIN_NONE && butt4.clicked(this.win.x2, this.win.y2, this.win.down, this.win.up)) {
                        STATE = WIN_NONE;
                        clicked = true;
                    } else if (butt5.clicked(this.win.x2, this.win.y2, this.win.down, this.win.up)) {
                        STATE = WIN_COM;
                        resetGame();
                        clicked = true;
                    }
                    this.win.up = false;
                    Canvas c = this.sh.lockCanvas();
                    gameCustomDrawing(c);
                    c.drawPaint(this.paintDark);
                    if (this.result == WIN_NONE) {
                        butt4.draw(c);
                    }
                    butt5.draw(c);
                    if (c != null) {
                        this.sh.unlockCanvasAndPost(c);
                    }
                    waitForNextTimeSlice();
                } else if (this.result == WIN_NONE) {
                    STATE = WIN_NONE;
                    return;
                } else {
                    STATE = WIN_COM;
                    return;
                }
            } catch (Exception e) {
            }
        }
    }

    private void fadeOutPause(int time) {
        time *= fps;
        float t = 255.0f;
        float m = 255.0f / ((float) time);
        start = System.currentTimeMillis();
        Log.msg(this.result + " fade");
        while (!MySurfaceView.threadPauseRequest && time > 0) {
            t -= m;
            time--;
            if (t < 0.0f) {
                t = 0.0f;
            }
            try {
                Canvas c = this.sh.lockCanvas();
                gameCustomDrawing(c);
                c.drawPaint(this.paintDark);
                if (this.result == WIN_NONE) {
                    butt4.draw(c);
                }
                butt5.draw(c);
                c.drawColor(Color.argb((int) (255.0f - t), 0, 0, 0));
                if (c != null) {
                    this.sh.unlockCanvasAndPost(c);
                }
                wait = 16 - (System.currentTimeMillis() - start);
                if (wait > 0) {
                    Thread.sleep(wait);
                }
                start = System.currentTimeMillis();
            } catch (Exception e) {
            }
        }
    }

    private void mainMenu() {
        boolean clicked = false;
        start = System.currentTimeMillis();
        this.win.up = false;
        MainActivity.backPressed = false;
        Sound.play(Sound.main);
        while (!MySurfaceView.threadPauseRequest && !clicked) {
            try {
                if (butt1.clicked(this.win.x2, this.win.y2, this.win.down, this.win.up)) {
                    STATE = WIN_NONE;
                    vsCom = true;
                    clicked = true;
                } else if (butt2.clicked(this.win.x2, this.win.y2, this.win.down, this.win.up)) {
                    STATE = WIN_NONE;
                    vsCom = false;
                    clicked = true;
                } else if (MainActivity.backPressed || butt3.clicked(this.win.x2, this.win.y2, this.win.down, this.win.up)) {
                    STATE = STATE_EXIT;
                    clicked = true;
                }
                this.win.up = false;
                Canvas c = this.sh.lockCanvas();
                c.drawBitmap(this.mainMenuImg, 0.0f, 0.0f, null);
                butt1.draw(c);
                butt2.draw(c);
                butt3.draw(c);
                if (c != null) {
                    this.sh.unlockCanvasAndPost(c);
                }
                waitForNextTimeSlice();
            } catch (Exception e) {
            }
        }
    }

    private void fadeOutMain(int time) {
        time *= fps;
        float t = 255.0f;
        float m = 255.0f / ((float) time);
        start = System.currentTimeMillis();
        while (!MySurfaceView.threadPauseRequest && time > 0) {
            t -= m;
            time--;
            if (t < 0.0f) {
                t = 0.0f;
            }
            try {
                Canvas c = this.sh.lockCanvas();
                c.drawBitmap(this.mainMenuImg, 0.0f, 0.0f, null);
                butt1.draw(c);
                butt2.draw(c);
                butt3.draw(c);
                c.drawColor(Color.argb((int) (255.0f - t), 0, 0, 0));
                if (c != null) {
                    this.sh.unlockCanvasAndPost(c);
                }
                wait = 16 - (System.currentTimeMillis() - start);
                if (wait > 0) {
                    Thread.sleep(wait);
                }
                start = System.currentTimeMillis();
            } catch (Exception e) {
            }
        }
    }

    private void template(int time) {
        start = System.currentTimeMillis();
        while (!MySurfaceView.threadPauseRequest) {
            try {
                Canvas c = this.sh.lockCanvas();
                if (c != null) {
                    this.sh.unlockCanvasAndPost(c);
                }
                waitForNextTimeSlice();
            } catch (Exception e) {
            }
        }
    }

    private static void waitForNextTimeSlice() throws InterruptedException {
        wait = 16 - (System.currentTimeMillis() - start);
        if (wait > 0) {
            Thread.sleep(wait);
        }
        start = System.currentTimeMillis();
    }

    private void calculationForAI() {
        int diskRad = this.ball.radius;
        int ballRad = this.com.radius;
        if (this.ball.pos.f5y > ((float) (diskRad * WIN_PLAYER))) {
            if (this.ball.pos.f5y < ((float) ((height / WIN_PLAYER) + ballRad))) {
                this.com.moveTo((int) this.ball.pos.f4x, (int) this.ball.pos.f5y, level);
            } else {
                this.com.moveTo((int) this.ball.pos.f4x, ballRad * WIN_PLAYER, level);
            }
        }
        if (this.com.pos.f4x < ((float) diskRad) || this.com.pos.f4x > ((float) (width - (diskRad - ballRad))) || this.com.pos.f5y < ((float) diskRad) || this.com.pos.f5y > ((float) ((height / WIN_PLAYER) - (diskRad - ballRad)))) {
            this.com.moveTo(width / WIN_PLAYER, height / 35, level);
        }
        if (this.ball.pos.f5y < 0.0f || this.ball.pos.f5y > ((float) height)) {
            this.com.moveTo(width / WIN_PLAYER, height / 35, level);
        }
    }

    private void fadeOutString(String msg, int time, int size1, int size2) {
        time *= fps;
        float size = (float) size1;
        float fs = (2.0f * ((float) (size2 - size1))) / ((float) (time * time));
        float ft = 510.0f / ((float) (time * time));
        int t = 0;
        Sound.play(Sound.resume);
        start = System.currentTimeMillis();
        while (!MySurfaceView.threadPauseRequest && time > 0) {
            int td = t * t;
            try {
                textPaint.setTextSize(size);
                textPaint.setARGB(255 - ((int) ((((float) td) * ft) / 2.0f)), 20, 220, MotionEventCompat.ACTION_MASK);
                time--;
                t += WIN_COM;
                size = ((float) size1) + ((((float) td) * fs) / 2.0f);
                Canvas c = this.sh.lockCanvas();
                gameCustomDrawing(c);
                c.drawText(msg, (float) (width / WIN_PLAYER), (((float) height) + size) / 2.0f, textPaint);
                if (c != null) {
                    this.sh.unlockCanvasAndPost(c);
                }
                wait = 16 - (System.currentTimeMillis() - start);
                if (wait > 0) {
                    Thread.sleep(wait);
                }
                start = System.currentTimeMillis();
            } catch (Exception e) {
            }
        }
    }

    private void showString(String msg, int time, int size) {
        time *= 1000;
        try {
            Log.msg(msg);
            textPaint.setTextSize((float) size);
            textPaint.setColor(-16711681);
            Canvas c = this.sh.lockCanvas(null);
            gameCustomDrawing(c);
            c.drawText(msg, (float) (width / WIN_PLAYER), (float) ((height + size) / WIN_PLAYER), textPaint);
            if (c != null) {
                this.sh.unlockCanvasAndPost(c);
            }
            Thread.sleep((long) time);
        } catch (Exception e) {
        }
    }
}
