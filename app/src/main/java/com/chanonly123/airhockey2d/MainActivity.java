package com.chanonly123.airhockey2d;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.Window;

public final class MainActivity extends Activity {

    static boolean backPressed;
    static MainActivity context;
    private boolean created;
    private MySurfaceView surfaceView;

    public MainActivity() {
        this.created = false;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Log.msg("activity creating");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        if (!this.created) {
            this.created = true;
            this.surfaceView = new MySurfaceView(this);
        }
        if (this.surfaceView == null) {
            Log.msg("surface is gone!");
        }
        setContentView(this.surfaceView);
        backPressed = false;
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.msg("activity Destroying");
    }

    public void onBackPressed() {
        backPressed = true;
    }

}
