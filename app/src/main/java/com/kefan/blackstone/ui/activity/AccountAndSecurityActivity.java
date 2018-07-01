package com.kefan.blackstone.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.kefan.blackstone.BaseActivity;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.service.impl.UserServiceImpl;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AccountAndSecurityActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG ="AccountAndSecurity";
    private String LogOutURL= APIManager.BASE_URL +"v1/user/logout";
    private RequestQueue requestQueue;

    private LinearLayout tab_personInformation;
    private LinearLayout tab_resetPassword;
    private LinearLayout tab_resetPhone;

    private LinearLayout actionBack;

    private BootstrapButton logOut;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String isLoginedFile="isLogin";
    private Boolean isLogined;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_and_security);

        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        /**
         * 访问用户是否登录的文件
         */
        sharedPreferences=getSharedPreferences(isLoginedFile,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        /**
         * 访问用户信息的文件
         */
        userInformationSharedPreferences=getSharedPreferences(userInformation,MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();

        /**
         * 创建请求队列
         */
        requestQueue= Volley.newRequestQueue(this);

        //isLogined=sharedPreferences.getBoolean("islogined",true);

    }

    private void initViews() {
        tab_personInformation= (LinearLayout) findViewById(R.id.account_security_linearlayout_person_information);
        tab_resetPassword= (LinearLayout) findViewById(R.id.account_security_linearlayout_reset_password);
        tab_resetPhone= (LinearLayout) findViewById(R.id.account_security_linearlayout_reset_phone);

        actionBack= (LinearLayout) findViewById(R.id.activity_account_security_linear_layout_action_back);
        logOut= (BootstrapButton) findViewById(R.id.account_and_security_bootStrap_button_logout);
    }

    private void initEvents() {
        tab_personInformation.setOnClickListener(this);
        tab_resetPassword.setOnClickListener(this);
        tab_resetPhone.setOnClickListener(this);

        actionBack.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.account_security_linearlayout_person_information:
                startActivity(new Intent(this,PersonInformationActivity.class));
                //overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.account_security_linearlayout_reset_password:
                startActivity(new Intent(this,ResetPasswordActivity.class));
               // overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.account_security_linearlayout_reset_phone:
                startActivity(new Intent(this,ResetPhoneActivity.class));
                //overridePendingTransition(R.anim.in,R.anim.out);
                break;

            case R.id.activity_account_security_linear_layout_action_back:
                actionBack();
                break;

            case R.id.account_and_security_bootStrap_button_logout:
                    showDialog();
                break;
        }
    }

    private void actionBack() {
        this.finish();
      //  overridePendingTransition(R.anim.in,R.anim.out);
    }


    private void resetUserInfomation()//重置用户信息
    {
        userInformationEditor.putLong("id",0);
        userInformationEditor.putString("mobile","");
        userInformationEditor.putString("studentId","");
        userInformationEditor.putString("name","");
        userInformationEditor.putString("gender","");
        userInformationEditor.putString("mail","");
        userInformationEditor.putString("token","");
        userInformationEditor.putLong("expireAt",0);
        userInformationEditor.apply();
    }

    private void showDialog()
    {
        final LogOutDialog logOutDialog=new LogOutDialog(this,R.style.LogOutDialog,R.layout.log_out_dialog);
        logOutDialog.setCancelable(false);
        logOutDialog.show();
        TextView sure= (TextView) logOutDialog.findViewById(R.id.log_out_textView_sure);
        TextView cancel= (TextView) logOutDialog.findViewById(R.id.log_out_textView_cancel);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new UserServiceImpl().logout();

                Intent intent=new Intent("logout");
                sendBroadcast(intent);

                logOutDialog.dismiss();

                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutDialog.dismiss();
            }
        });
    }

    private class LogOutDialog extends Dialog {
        private Context context;
        private int resId;
        public LogOutDialog(Context context, int resLayout) {
            this(context,0,0);
        }
        public LogOutDialog(Context context, int themeResId, int resLayout) {
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
}
