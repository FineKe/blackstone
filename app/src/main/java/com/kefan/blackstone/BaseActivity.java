package com.kefan.blackstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.testin.agent.Bugout;

/**
 * Created by MY SHIP on 2017/7/5.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bugout.init(this, "e20636cd3cd1295bb49b76aa0bac2727", "");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //注：回调 1
        Bugout.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugout.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugout.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }
}
