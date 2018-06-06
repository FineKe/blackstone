package com.hdu.myship.blackstone;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.litepal.LitePalApplication;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class BlackStoneApplication extends Application {
    private  static Context context;

    private static RequestQueue requestQueue;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePalApplication.initialize(context);
        TypefaceProvider.registerDefaultIconSets();
        requestQueue= Volley.newRequestQueue(this);
    }

    public static Context getContext()
    {
        return context;
    }

    private void initLogServer() {

        FormatStrategy formatStrategy= PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodCount(7)
                .tag("Sun Yat")
                .build();
//        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return true;
            }
        });
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
