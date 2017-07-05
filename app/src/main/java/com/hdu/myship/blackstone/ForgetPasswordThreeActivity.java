package com.hdu.myship.blackstone;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONException;
import org.json.JSONObject;

import ActivityUtil.ActivityCollector;
import ActivityUtil.BaseActivity;
import JavaBean.APIManager;

public class ForgetPasswordThreeActivity extends BaseActivity implements View.OnClickListener{
    private String resetPasswordURL= APIManager.rootDoname+"v1/user/forgetPwd/setPwd";
    private RequestQueue requestQueue;
    private JsonObjectRequest resetPasswordRequest;
    private ImageView actionBack;
    private ImageView showPassword;

    private EditText inputPassword;

    private TextView message;

    private BootstrapButton completed;

    private String phoneNumber;
    private String code;
    private Boolean isShowed=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password_three);
        initData();
        initViews();
        initEvents();
    }

    private void initData() {

    }

    private void initViews() {
        actionBack= (ImageView) findViewById(R.id.activity_make_team_image_view_action_back);
        showPassword= (ImageView) findViewById(R.id.activity_forget_password_three_image_view_show_password);

        inputPassword= (EditText) findViewById(R.id.activity_forget_password_three_edit_text_input_password);

        message= (TextView) findViewById(R.id.activity_forget_password_three_text_view_message);

        completed= (BootstrapButton) findViewById(R.id.activity_forget_password_three_boot_strap_button_completed);
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        completed.setOnClickListener(this);

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                message.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_make_team_image_view_action_back:
                actionBack();
                break;

            case R.id.activity_forget_password_three_image_view_show_password:
                showPassword();
                break;

            case R.id.activity_forget_password_three_boot_strap_button_completed:
                completed();
                break;
        }
    }

    private void actionBack() {
        this.finish();
    }

    private void showPassword() {
        if(!isShowed)
        {
            inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            showPassword.setImageResource(R.mipmap.see);
        }
        else
        {
            inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPassword.setImageResource(R.mipmap.no_see);
        }
        isShowed=!isShowed;
        inputPassword.setSelection(inputPassword.getText().length());
        inputPassword.postInvalidate();
    }

    private void completed() {
        if(inputPassword.getText().toString().length()<6||inputPassword.getText().toString().length()>16)
        {
            message.setText("密码为6~16位字母、数字或符号");
        }
        else
        {
            resetPassword();
        }
    }

    private void resetPassword()
    {
        requestQueue= Volley.newRequestQueue(this);
        phoneNumber=getIntent().getStringExtra("phoneNumber");
        code=getIntent().getStringExtra("code");
        JSONObject object=new JSONObject();
        try {
            object.put("mobile",phoneNumber);
            object.put("pwd",inputPassword.getText().toString());
            object.put("verifyCode",code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resetPasswordRequest=new JsonObjectRequest(Request.Method.POST, resetPasswordURL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        Toast.makeText(ForgetPasswordThreeActivity.this,"密码设定成功",Toast.LENGTH_SHORT).show();
                        ActivityCollector.finishAll();
                    }
                    else
                    {
                        String message=jsonObject.getString("message");
                        Toast.makeText(ForgetPasswordThreeActivity.this,message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ForgetPasswordThreeActivity.this, "没有网络", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(resetPasswordRequest);
    }
}
