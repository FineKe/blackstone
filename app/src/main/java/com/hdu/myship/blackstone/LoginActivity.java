package com.hdu.myship.blackstone;

import android.content.ContentValues;
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
import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import database.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG="LOGIN";
    private String login_url="http://api.blackstone.ebirdnote.cn/v1/user/login";//数据提交接口地址
    private BootstrapEditText user_name_Edit;
    private BootstrapEditText user_password_Edit;
    private BootstrapButton login_button;
    private BootstrapButton register_button;
    private BootstrapButton reset_password_button;

    private User user_;


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
        user_=new User();//创建一个用户对象
        user_= DataSupport.findFirst(User.class);//从数据库user表中读取第一个用户的数据
        if(user_!=null)//从数据库中读取用户的账号和密码，并将其填充到相应的edittext中，实现密码的记住功能
        {
            user_name_Edit.setText(user_.getUserName());
            user_password_Edit.setText(user_.getPassword());
        }

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
    {   if(phone.equals("")||pwd.equals(""))
        {
            Toast.makeText(context,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String,String> map=new HashMap<>();
        map.put("username",phone);
        map.put("pwd",pwd);
        JSONObject object=new JSONObject(map);
        RequestQueue requestQueue= Volley.newRequestQueue(context);//创建一个网络请求队列
        /**
         * 创建一个json请求
         */
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, login_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code;
                String message;
                JSONObject data;
                JSONObject user;
                String token;
                long expireAt;
                try {
                    code=jsonObject.getInt("code");
                    message=jsonObject.getString("message");
                    if(code==88)
                    {
                        data=jsonObject.getJSONObject("data");

                        token=data.getString("token");
                        User user_=new User();
                        user_.setUserName(user_name_Edit.getText().toString());
                        user_.setPassword(user_password_Edit.getText().toString());
                        user_.setToken(token);//如果用户登录成功的话，就将其用户名密码和token更新存到数据库中
                        user_.update(1);//更新第一个用户的信息
                        user_.save();//保存一下
                        Toast.makeText(context,"登陆成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,MainActivity.class);//跳转到主界面
                        startActivity(intent);
                        finish();//将该activity销毁
                    }
                    else
                    {
                        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: "+message);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context,"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    public void resetPwd(Context context)//密码重置处理
    {
            Intent intent=new Intent(context,ResetPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    public void register(Context context)//用户注册处理
    {
        Intent intent=new Intent(context,register_activity.class);
        context.startActivity(intent);
    }


}
