package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG="LOGIN";
    private String login_url="http://api.blackstone.ebirdnote.cn/v1/user/login";
    private BootstrapEditText user_name_Edit;
    private BootstrapEditText user_password_Edit;
    private BootstrapButton login_button;
    private BootstrapButton register_button;
    private BootstrapButton reset_password_button;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitView();//初始化控件
        reset_password_button.setOnClickListener(this);
        register_button.setOnClickListener(this);
        login_button.setOnClickListener(this);

    }

    private void InitView()
    {
        user_name_Edit= (BootstrapEditText) findViewById(R.id.user_name_bootstrapEditText);
        user_password_Edit= (BootstrapEditText) findViewById(R.id.password_bootstrapEditText);
        login_button= (BootstrapButton) findViewById(R.id.login_bootstrapButton);
        reset_password_button= (BootstrapButton) findViewById(R.id.reset_password_bootstrapButton);
        register_button= (BootstrapButton) findViewById(R.id.regist_bootstrapButton);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_bootstrapButton:
                login(this,user_name_Edit.getText().toString(),user_password_Edit.getText().toString());
                break;
            case R.id.regist_bootstrapButton:
                register(this);
                break;
            case R.id.reset_password_bootstrapButton:
                resetPwd(this);
                break;
            default:break;
        }
    }

    public void login(final Context context, String phone, String pwd)//登录处理
    {
        Map<String,String> map=new HashMap<>();
        map.put("username",phone);
        map.put("pwd",pwd);
        JSONObject object=new JSONObject(map);
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, login_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                UserInfo userInfo=null;
                int code;
                String message;
                JSONObject data;
                JSONObject user;
                String token;
                long expireAt;
                try {
                    code=jsonObject.getInt("code");
                    message=jsonObject.getString("message");
                    data=jsonObject.getJSONObject("data");
                    user=data.getJSONObject("user");
                    token=data.getString("token");
                    expireAt=data.getLong("expireAt");
                    Log.d(TAG, "onResponse: "+user.toString());
                    //userInfo.setId(user.getInt("id"));
                    //userInfo.setUsername(user.getString("username"));
                    //userInfo.setNikname(user.getString("nickname"));
                    if(code==88)
                    {
                        Toast.makeText(context,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(request);
    }

    public void resetPwd(Context context)//密码重置处理
    {
            Intent intent=new Intent(context,ResetPasswordActivity.class);
            startActivity(intent);
    }

    public void register(Context context)//用户注册处理
    {
        Intent intent=new Intent(context,register_activity.class);
        context.startActivity(intent);
    }


}
