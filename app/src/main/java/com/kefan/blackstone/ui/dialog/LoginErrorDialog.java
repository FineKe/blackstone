package com.kefan.blackstone.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.activity.ForgetPasswordActivity;
import com.kefan.blackstone.ui.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @note:
 * @author: 柯帆
 * @date: 2018/6/6 下午5:13
 */
public class LoginErrorDialog extends Dialog {


    private int resId;

    @BindView(R.id.error_login_dialog_forget)
    TextView forget;


    @BindView(R.id.error_login_dialog_inputAgain)
    TextView inputAgain;


    private LoginErrorDialog(@NonNull Context context, int themeResId) {
        this(context, 0, 0);
    }

    private LoginErrorDialog(Context context, int themeResId, int layout) {
        super(context, themeResId);
        this.resId = layout;
    }


    public static LoginErrorDialog buildLoginErrorDialog(Context context) {

        LoginErrorDialog loginErrorDialog = new LoginErrorDialog(context, R.style.LoginDialog, R.layout.error_login_dialog);
        loginErrorDialog.setCancelable(false);
        return loginErrorDialog;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resId);
        ButterKnife.bind(this);

        initEvent();
    }

    private void initEvent() {

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginErrorDialog.this.dismiss();
                getContext().startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
            }
        });

        inputAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginErrorDialog.this.dismiss();
                LoginDialog.getLoginDialog(getContext(), ((MainActivity) getOwnerActivity()).handler).show();
            }
        });

    }
}
