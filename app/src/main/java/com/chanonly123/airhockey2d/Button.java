package com.chanonly123.airhockey2d;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public final class Button {
    private Bitmap curBitmap;
    private Bitmap imgClick;
    private Bitmap imgHover;
    private Bitmap imgNormal;
    private int radius;
    private int f1x;
    private int f2y;

    Button(int posx, int posy, int radi) {
        this.radius = radi;
        this.f1x = posx;
        this.f2y = posy;
    }

    private Bitmap setBitmap(Bitmap img) {
        int d = this.radius * 2;
        return Bitmap.createScaledBitmap(img, d, d, false);
    }

    public void setBitmaps(Bitmap normalImg, Bitmap hoverImg, Bitmap clickImg) {
        this.imgNormal = setBitmap(normalImg);
        this.imgHover = setBitmap(hoverImg);
        this.imgClick = setBitmap(clickImg);
        this.curBitmap = this.imgNormal;
    }

    public boolean clicked(float px, float py, boolean down, boolean up) {
        if (new Vector2D(px - ((float) this.f1x), py - ((float) this.f2y)).value() <= ((float) this.radius)) {
            if (down) {
                this.curBitmap = this.imgHover;
                return false;
            } else if (up) {
                this.curBitmap = this.imgClick;
                Sound.play(Sound.click);
                return true;
            }
        }
        this.curBitmap = this.imgNormal;
        return false;
    }

    public void draw(Canvas c) {
        c.drawBitmap(this.curBitmap, (float) (this.f1x - this.radius), (float) (this.f2y - this.radius), null);
    }
}
