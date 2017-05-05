package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by MY SHIP on 2017/5/4.
 */

public class LoginDialog extends Dialog {
    private Context context;
    private int resId;
    public LoginDialog(Context context, int resLayout) {
        this(context,0,0);
    }
    public LoginDialog(Context context, int themeResId, int resLayout) {
        super(context, themeResId);
        this.context = context;
        this.resId = resLayout;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resId);
    }
}
