package com.codiography.airhockey2d;

public abstract class Log {
    public static final void msg(String s) {
        android.util.Log.d("android", s);
    }
}
