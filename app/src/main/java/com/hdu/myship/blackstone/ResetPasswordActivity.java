package com.hdu.myship.blackstone;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG="MAIN";
    private String submit_url="http://api.blackstone.ebirdnote.cn/v1/user/forgetPwd/setPwd";
    private String get_url="http://api.blackstone.ebirdnote.cn/v1/user/forgetPwd/verifyCode/mobile";
    private RequestQueue requestQueue;
    private int color= Color.argb(100,52,119,197);

    private BootstrapEditText phone_edit;
    private BootstrapEditText passwotd_edit;
    private BootstrapEditText confirm_edit;
    private BootstrapEditText writeCode_edit;

    private BootstrapButton get_code_button;
    private BootstrapButton submit_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initView();
        initEvents();
    }

    private void initEvents() {
        get_code_button.setOnClickListener(this);
        submit_button.setOnClickListener(this);
    }


    private void initView()
    {
        phone_edit= (BootstrapEditText) findViewById(R.id.reset_password_bootstrapEdit_phone);
        passwotd_edit= (BootstrapEditText) findViewById(R.id.reset_password_bootstrapEdit_password);
        confirm_edit= (BootstrapEditText) findViewById(R.id.reset_password_bootstrapEdit_password_confirm);
        writeCode_edit= (BootstrapEditText) findViewById(R.id.reset_password_bootstrapEdit_writeVerifyCode);

        get_code_button= (BootstrapButton) findViewById(R.id.reset_password_bootstrapButton_getVerifyCode);
        submit_button= (BootstrapButton) findViewById(R.id.reset_password_bootstrapButton_submit);

    }

    @Override
    public void onClick(View v) {
        requestQueue= Volley.newRequestQueue(this);

        switch (v.getId())
        {
            case R.id.reset_password_bootstrapButton_getVerifyCode:
                getCode();
                break;
            case R.id.reset_password_bootstrapButton_submit:
                submit();
                break;
        }
    }

    private void getCode()
    {
        if((passwotd_edit.getText().toString().equals(confirm_edit.getText().toString()))&&(!phone_edit.getText().toString().equals(""))&&
        !passwotd_edit.getText().toString().equals("")&&!confirm_edit.getText().toString().equals(""))//如果两次密码一不为空，且号码不为空，向服务器发送请求
        {

            Map<String,Long> map=new HashMap<>();
            map.put("number",Long.parseLong(phone_edit.getText().toString()));
            Log.d(TAG, "getCode: "+map.toString());
            JSONObject object=new JSONObject(map);
            JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, get_url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        int code=jsonObject.getInt("code");
                        String message=jsonObject.getString("message");
                        if(code==88)
                        {
                            Toast.makeText(getApplicationContext(),"获取验证码成功",Toast.LENGTH_SHORT).show();
                            submit_button.setEnabled(true);
                        }else
                        {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_SHORT).show();;
                }
            });
            requestQueue.add(request);
        }
        else if(phone_edit.getText().toString().length()!=11)
        {
            Toast.makeText(getApplicationContext(),"号码不合法",Toast.LENGTH_SHORT).show();
        }
        else if(passwotd_edit.getText().toString().equals("")||confirm_edit.getText().toString().equals(""))
        {
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(getApplicationContext(),"密码不一致",Toast.LENGTH_SHORT).show();
        }
    }

    private void submit()
    {
        HashMap<String,String> map=new HashMap<>();
        map.put("username",phone_edit.getText().toString());
        map.put("pwd",confirm_edit.getText().toString());
        map.put("verifyCode",writeCode_edit.getText().toString());
        JSONObject object=new JSONObject(map);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, submit_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    String message=jsonObject.getString("message");
                    if(code==88)
                    {
                        Toast.makeText(getApplicationContext(),"密码重置成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),"请求失败",Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }



}
