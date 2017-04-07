package com.hdu.myship.blackstone;

import android.app.Application;
import android.content.Context;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.litepal.LitePalApplication;
import org.litepal.util.Const;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class MyApplication extends Application {
    private  static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePalApplication.initialize(context);
        TypefaceProvider.registerDefaultIconSets();
    }
    public static Context getContext()
    {
        return context;
    }
}
