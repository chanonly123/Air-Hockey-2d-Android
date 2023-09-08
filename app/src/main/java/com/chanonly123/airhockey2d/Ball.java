package com.chanonly123.airhockey2d;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;

public final class Ball {
    static final int SCORE_COM = 2;
    static final int SCORE_NONE = 0;
    static final int SCORE_PLAYER = 1;
    static int goal_max;
    static int goal_min;
    static int goal_size;
    private Bitmap bitmap;
    int height;
    int lbh;
    int lbw;
    float loss;
    Vector2D pointer;
    Vector2D pos;
    int radius;
    boolean touched;
    Vector2D vel;
    int width;

    static void setGoal(int lw, int lh, int wid, int hei, int goal_s) {
        goal_size = goal_s;
        goal_min = ((wid / SCORE_COM) + lw) - (goal_size / SCORE_COM);
        goal_max = ((wid / SCORE_COM) + lw) + (goal_size / SCORE_COM);
    }

    Ball(int r, Vector2D mouse) {
        this.pos = new Vector2D(0.0f, 0.0f);
        this.radius = SCORE_NONE;
        this.vel = new Vector2D(0.0f, 0.0f);
        this.loss = 0.1f;
        this.touched = false;
        this.radius = r;
        this.pointer = mouse;
    }

    void setBounds(int w, int h, int wid, int hei) {
        this.lbw = w;
        this.lbh = h;
        this.width = wid;
        this.height = hei;
    }

    void initBall(Vector2D pos, Vector2D vel) {
        this.pos.f4x = pos.f4x;
        this.pos.f5y = pos.f5y;
        this.vel.f4x = vel.f4x;
        this.vel.f5y = vel.f5y;
    }

    void initBall(float posx, float posy, float velx, float vely) {
        this.pos.f4x = posx;
        this.pos.f5y = posy;
        this.vel.f4x = velx;
        this.vel.f5y = vely;
    }

    void updatePosition() {
        this.pos.add(this.vel);
    }

    void setPlayerVelocity() {
        if (!this.touched) {
            this.vel = this.pos.getSub(this.pointer);
            this.vel.scalarMul(-0.5f);
        }
    }

    void checkBounds() {
        Vector2D vector2D;
        if (this.pos.f5y < ((float) (this.lbh + this.radius))) {
            this.pos.f5y = (float) (this.lbh + this.radius);
            this.pointer.f5y = this.pos.f5y;
            vector2D = this.vel;
            this.vel.f5y = 0.0f;
            vector2D.f4x = 0.0f;
        } else if (this.pos.f5y >= ((float) ((this.lbh + this.height) - this.radius))) {
            this.pos.f5y = (float) (((this.lbh + this.height) - this.radius) - 1);
            this.pointer.f5y = this.pos.f5y;
            vector2D = this.vel;
            this.vel.f5y = 0.0f;
            vector2D.f4x = 0.0f;
        }
    }

    void moveTo(int x, int y, int dificulty) {
        this.pointer.f4x = (float) x;
        this.pointer.f5y = (float) y;
        if (this.pointer.f4x < ((float) (this.lbw + this.radius))) {
            this.pointer.f4x = (float) (this.lbw + this.radius);
        } else if (this.pointer.f4x >= ((float) ((this.lbw + this.width) - this.radius))) {
            this.pointer.f4x = (float) (((this.lbw + this.width) - this.radius) - 1);
        }
        if (this.pointer.f5y < ((float) (this.lbh + this.radius))) {
            this.pointer.f5y = (float) (this.lbh + this.radius);
        } else if (this.pointer.f5y >= ((float) ((this.lbh + this.height) - this.radius))) {
            this.pointer.f5y = (float) (((this.lbh + this.height) - this.radius) - 1);
        }
        if (!this.touched) {
            this.vel = this.pos.getSub(this.pointer);
            this.vel.scalarMul(-0.02f * ((float) Math.pow((double) dificulty, 0.8d)));
        }
    }

    int checkWallCollition() {
        Vector2D vector2D;
        if (this.pos.f4x - ((float) this.radius) < ((float) this.lbw)) {
            vector2D = this.vel;
            vector2D.f4x *= -(1.0f - this.loss);
            this.pos.f4x = (float) (this.lbw + this.radius);
            Sound.play(Sound.collide2);
        } else if (this.pos.f4x + ((float) this.radius) > ((float) (this.lbw + this.width))) {
            vector2D = this.vel;
            vector2D.f4x *= -(1.0f - this.loss);
            this.pos.f4x = (float) ((this.lbw + this.width) - this.radius);
            Sound.play(Sound.collide2);
        }
        if (this.pos.f5y - ((float) this.radius) < ((float) this.lbh)) {
            if (this.pos.f4x < ((float) goal_min) || this.pos.f4x >= ((float) goal_max)) {
                vector2D = this.vel;
                vector2D.f5y *= -(1.0f - this.loss);
                this.pos.f5y = (float) (this.lbh + this.radius);
                Sound.play(Sound.collide2);
                return SCORE_NONE;
            } else if (this.pos.f5y <= ((float) (this.lbh - (this.radius / SCORE_COM)))) {
                return SCORE_PLAYER;
            } else {
                return SCORE_NONE;
            }
        } else if (this.pos.f5y + ((float) this.radius) <= ((float) (this.lbh + this.height))) {
            return SCORE_NONE;
        } else {
            if (this.pos.f4x < ((float) goal_min) || this.pos.f4x >= ((float) goal_max)) {
                vector2D = this.vel;
                vector2D.f5y *= -(1.0f - this.loss);
                this.pos.f5y = (float) ((this.lbh + this.height) - this.radius);
                Sound.play(Sound.collide2);
                return SCORE_NONE;
            } else if (this.pos.f5y >= ((float) ((this.lbh + this.height) + (this.radius / SCORE_COM)))) {
                return SCORE_COM;
            } else {
                return SCORE_NONE;
            }
        }
    }

    void chekcBallCollision(Ball ball) {
        Vector2D dspl_vec = new Vector2D(ball.pos.getSub(this.pos));
        float dis = dspl_vec.value() - ((float) (ball.radius + this.radius));
        if (dis > 0.0f) {
            this.touched = false;
        } else if (this.touched) {
            this.vel = dspl_vec.getUnit().getScalarMul(dis);
            updatePosition();
            ball.vel = dspl_vec.getUnit().getScalarMul((-dis) * 0.5f).getAdd(ball.vel);
        } else {
            this.touched = true;
            Sound.play(Sound.collide1);
            Vector2D vn1 = new Vector2D(dspl_vec);
            vn1.setUnit();
            Vector2D vt1 = vn1.getNormal();
            float v1n = this.vel.dotProduct(vn1);
            float v1t = this.vel.dotProduct(vn1);
            float v2n = ball.vel.dotProduct(vn1);
            float v2t = ball.vel.dotProduct(vt1);
            float v1n_a = ((((float) (this.radius - ball.radius)) * v1n) + (((float) (ball.radius * SCORE_COM)) * v2n)) / ((float) (this.radius + ball.radius));
            Vector2D vn2 = vn1.getScalarMul(((((float) (ball.radius - this.radius)) * v2n) + (((float) (this.radius * SCORE_COM)) * v1n)) / ((float) (this.radius + ball.radius)));
            Vector2D vt2 = vt1.getScalarMul(v2t);
            vn1.scalarMul(v1n_a);
            vt1.scalarMul(v1t);
            ball.vel = vn2.getAdd(vt2);
            ball.vel.scalarMul(1.2f);
            ball.decreaseExtraSpeedTo(20);
            ball.updatePosition();
        }
    }

    void decreaseExtraSpeedTo(int velocity) {
        if (this.vel.value() > ((float) velocity)) {
            this.vel.setUnit();
            this.vel.scalarMul((float) velocity);
        }
    }

    public void setBitmap(Bitmap img) {
        int d = this.radius * SCORE_COM;
        Bitmap scaledSrc = Bitmap.createScaledBitmap(img, d, d, false);
        this.bitmap = Bitmap.createBitmap(d, d, Config.ARGB_8888);
        Canvas canvas = new Canvas(this.bitmap);
        Paint paint = new Paint(SCORE_PLAYER);
        paint.setShader(new BitmapShader(scaledSrc, TileMode.CLAMP, TileMode.CLAMP));
        d /= SCORE_COM;
        canvas.drawColor(SCORE_NONE);
        canvas.drawCircle((float) d, (float) d, (float) d, paint);
    }

    void draw(Canvas c) {
        c.drawBitmap(this.bitmap, this.pos.f4x - ((float) this.radius), this.pos.f5y - ((float) this.radius), null);
    }

    void forceMovePointer() {
        this.pointer.f4x = this.pos.f4x;
        this.pointer.f5y = this.pos.f5y;
    }
}
