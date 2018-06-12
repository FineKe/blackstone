package com.kefan.blackstone.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.HandlerConstant;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.activity.ForgetPasswordActivity;
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.vo.TokenVO;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallBeatIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录 dialog
 * Created by MY SHIP on 2017/5/4.
 */

public class LoginDialog extends Dialog {

    private int resId;

    private boolean isShowed = false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation = "UesrInformation";


    @BindView(R.id.update_phone_numbe_dialog_editText_account)
    EditText account;

    @BindView(R.id.update_phone_number_dialog_editText_code)
    EditText password;

    @BindView(R.id.update_phone_number_text_view_get_code)
    TextView forget;

    @BindView(R.id.update_phone_number_dialog_textView_Ok)
    TextView ok;

    @BindView(R.id.login_dialog_imageView_actionCancel)
    ImageView cancel;

    @BindView(R.id.login_dialog_imageView_showPassword)
    ImageView showPwd;

    @BindView(R.id.loading_login_dialog)
    AVLoadingIndicatorView loading;

    private static Handler handler;

    private UserService userService;


    private LoginDialog(Context context, int resLayout) {
        this(context, 0, 0);
    }

    private LoginDialog(Context context, int themeResId, int resLayout) {
        super(context, themeResId);
        this.resId = resLayout;
        userService = new UserServiceImpl();

    }

    public static LoginDialog getLoginDialog(Context context, Handler mhandler) {
        LoginDialog loginDialog = new LoginDialog(context, R.style.LoginDialog, R.layout.login_dialog);
        loginDialog.setCancelable(false);
        handler=mhandler;
        return loginDialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(resId);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }


    private void initView() {

    }

    private void initEvent() {

        showPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowed) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPwd.setImageResource(R.mipmap.see);
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPwd.setImageResource(R.mipmap.no_see);
                }
                isShowed = !isShowed;
                password.setSelection(password.getText().length());
                password.postInvalidate();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LoginDialog.this.dismiss();

            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                userService.login(account.getText().toString(), password.getText().toString()
                        , new BaseResponseListener<TokenVO>() {

                            @Override
                            protected void onSuccess(TokenVO data) {

                                userService.saveUser(data,password.getText().toString());
                                ToastUtil.showToast(getContext(),"登录成功");
                                LoginDialog.this.dismiss();

                                handler.sendEmptyMessage(HandlerConstant.LOGIN_SUCCESS);

                                hideLoading();
                            }

                            @Override
                            protected void onFailed(int code, String message) {
                                ToastUtil.showToast(getContext(),message);
                                hideLoading();
                            }
                        }
                        , new BaseErrorListener(getContext()) {
                            @Override
                            protected void onError(VolleyError volleyError) {
                                super.onError(volleyError);
                                hideLoading();
                            }
                        });
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), ForgetPasswordActivity.class));
            }
        });

    }

    private void showErrorDialog() {
        final LoginErrorDialog loginErrorDialog = LoginErrorDialog.buildLoginErrorDialog(getContext());
        loginErrorDialog.show();
    }

    private void showLoading() {

        loading.smoothToShow();

    }

    private void hideLoading() {

        loading.smoothToHide();

    }

}
