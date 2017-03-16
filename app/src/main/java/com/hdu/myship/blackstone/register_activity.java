package com.hdu.myship.blackstone;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.HashMap;
import java.util.Map;

public class register_activity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener{
    String TAG="REGISTER";
    private String register_url="http://api.blackstone.ebirdnote.cn/v1/user/register/phone";
    private String getVerifyCode_url="http://api.blackstone.ebirdnote.cn/v1/user/verifyCode/mobile";
    private String check_url="http://api.blackstone.ebirdnote.cn/v1/user/check";
    private BootstrapEditText register_get_user_name;
    private BootstrapEditText register_get_user_pwd;
    private BootstrapEditText register_get_user_phone;
    private BootstrapEditText getRegister_get_user_code_Edit;
    private BootstrapEditText register_get_user_phone_bootstrapEdit;
    private BootstrapButton register_get_verrifyCode;
    private BootstrapButton register_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        View Title=LayoutInflater.from(this).inflate(R.layout.actionbar_title_layout,null);
        actionBar.setCustomView(Title);
        InitView();
        register_get_user_name.setOnFocusChangeListener(this);
        register_get_verrifyCode.setOnClickListener(this);
        register_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.regester_submit_bootstrapButton:
                    submit(this,register_get_user_name.getText().toString(),register_get_user_pwd.getText().toString(),register_get_user_phone.getText().toString(),getRegister_get_user_code_Edit.getText().toString())
                    ;
                    break;
                case R.id.get_verifyCode_bootstrapButton:
                    getVerifyCode(this,register_get_user_phone.getText().toString());
                    break;
                default:break;
            }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {//当用户输入完用户名后检查该用户名是否已经被注册过
        if(!register_get_user_phone_bootstrapEdit.getText().toString().equals(""))//用户名不为空时进行check
        {
            checkUserName(this,register_get_user_phone_bootstrapEdit.getText().toString());
        }

    }

    private void InitView()
    {   register_get_user_phone_bootstrapEdit= (BootstrapEditText) findViewById(R.id.register_get_user_phone_bootstrapEdit);
        getRegister_get_user_code_Edit= (BootstrapEditText) findViewById(R.id.get_verifyCode_bootstrapEdit);
        register_get_user_phone= (BootstrapEditText) findViewById(R.id.register_get_user_phone_bootstrapEdit);
        register_get_user_name= (BootstrapEditText) findViewById(R.id.register_user_name_bootstrapEdit);
        register_get_user_pwd= (BootstrapEditText) findViewById(R.id.register_user_pwd_bootstrapEdit);
        register_get_verrifyCode= (BootstrapButton) findViewById(R.id.get_verifyCode_bootstrapButton);
        register_submit= (BootstrapButton) findViewById(R.id.regester_submit_bootstrapButton);
    }
    private void checkUserName(final Context context, String string)  {
        RequestQueue requestQueue= Volley.newRequestQueue(context);
        Map<String,String> name=new HashMap<>();
        name.put("username",string);
        final JSONObject object=new JSONObject(name);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, check_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                //Toast.makeText(context,jsonObject.toString(),Toast.LENGTH_SHORT).show();
                try {
                   int  code=jsonObject.getInt("code");
                    Toast.makeText(context,String.valueOf(code),Toast.LENGTH_SHORT).show();
                    if(code!=88)
                    {
                        Toast.makeText(context,"手机号已被注册",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    private void getVerifyCode(final Context context,String string)
    {
        RequestQueue requestqueue=Volley.newRequestQueue(context);
        Map<String,String> phone=new HashMap<>();
        System.out.println(string);
        phone.put("number",string);
        JSONObject object=new JSONObject(phone);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, getVerifyCode_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code;
                String message;
                try {
                    code=jsonObject.getInt("code");
                    message=jsonObject.getString("message");
                    if(code==88)
                    {
                        Toast.makeText(context,"获取验证码成功",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(context,message+":请重新获取",Toast.LENGTH_SHORT).show();
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
        requestqueue.add(request);
    }

    private void submit(final Context context, String nickname, String pwd, String phoneNumber, String verifyCode)
    {
        Map<String,String> map=new HashMap<>();
        map.put("username",phoneNumber);
        map.put("pwd",pwd);
        map.put("nickname",nickname);
        map.put("verifyCode",verifyCode);
        JSONObject object=new JSONObject(map);
        System.out.println(verifyCode);
        RequestQueue requestqueue=Volley.newRequestQueue(context);
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST, register_url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                int code;
                String message;
                try {
                    code=jsonObject.getInt("code");
                    message=jsonObject.getString("message");
                    if(code==88)
                    {
                        Toast.makeText(context,"注册成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(context,"注册失败："+message,Toast.LENGTH_SHORT).show();
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
        requestqueue.add(request);
    }


}
