package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MY SHIP on 2017/5/30.
 * 用于判断是否登录了
 */

public class IsLoginUtil {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile="isLogin";
    private Boolean isLogined;
    private Context context;
    public IsLoginUtil(Context context) {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(isLoginedFile,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public Boolean getLogined() {
        return sharedPreferences.getBoolean("islogined",false);
    }

    public void setLogined(Boolean logined) {
        isLogined = logined;
        editor.putBoolean("islogined",logined).apply();
    }
}
