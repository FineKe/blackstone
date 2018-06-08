package com.kefan.blackstone.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ui.dialog.LoginErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import JavaBean.APIManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import util.UserSharePreferenceUtil;

/**
 * Created by MY SHIP on 2017/5/4.
 */

public class LoginDialog extends Dialog {

    private int resId;

    private boolean isShowed=false;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";

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


    private LoginDialog(Context context, int resLayout) {
        this(context,0,0);
    }

    private LoginDialog(Context context, int themeResId, int resLayout) {
        super(context, themeResId);
        this.resId = resLayout;
    }

    public static LoginDialog getLoginDialog(Context context) {
        LoginDialog loginDialog=new LoginDialog(context,R.style.LoginDialog,R.layout.login_dialog);
        loginDialog.setCancelable(false);
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
                if(!isShowed)
                {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPwd.setImageResource(R.mipmap.see);
                }
                else
                {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPwd.setImageResource(R.mipmap.no_see);
                }
                isShowed=!isShowed;
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
                Map<String,String> loginMap=new HashMap<String, String>();
                loginMap.put("username",account.getText().toString());
                loginMap.put("pwd",password.getText().toString());
                JSONObject loginJsonObject=new JSONObject(loginMap);
                JsonObjectRequest loginRequest=new JsonObjectRequest(Request.Method.POST, APIManager.LOGIN_URL, loginJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            int code=jsonObject.getInt("code");
                            if(code==88)
                            {
                                UserSharePreferenceUtil.save(getContext(),jsonObject,password.getText().toString());
                                LoginDialog.this.dismiss();
                            }
                            else
                            {

                                LoginDialog.this.dismiss();
                                showErrorDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(),"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });
               BlackStoneApplication.getRequestQueue().add(loginRequest);
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
        final LoginErrorDialog loginErrorDialog=LoginErrorDialog.buildLoginErrorDialog(getContext());
        loginErrorDialog.show();
    }
}
