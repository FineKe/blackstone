package com.hdu.myship.blackstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ActivityUtil.BaseActivity;

public class ForgetPasswordTwoActivity extends BaseActivity implements View.OnClickListener{
    private String getCodeURL="http://api.blackstone.ebirdnote.cn/v1/user/forgetPwd/verifyCode/mobile";
    private RequestQueue requestQueue;

    private TextView messagePhone;
    private TextView message;
    private TextView getCode;

    private EditText code;

    private ImageView actionBack;

    private BootstrapButton sure;

    private String phoneNumber;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password_two);
        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        phoneNumber=getIntent().getStringExtra("phoneNumber");
        requestQueue= Volley.newRequestQueue(this);
    }

    private void initViews() {
        messagePhone= (TextView) findViewById(R.id.activity_forget_password_two_textView_phoneMessage);
        message= (TextView) findViewById(R.id.activity_forget_password_two_textView_message);
        getCode= (TextView) findViewById(R.id.activity_forget_password_two_textView_get_code);

        code= (EditText) findViewById(R.id.activity_forget_password_two_edit_text_code);

        actionBack= (ImageView) findViewById(R.id.activity_make_team_image_view_action_back);

        sure= (BootstrapButton) findViewById(R.id.activity_forget_password_two_bootStarp_button_sure);


        messagePhone.setText("请通过"+phoneNumber+"手机号获取6位数字验证码");

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count==0)
                {
                    message.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
         switch (v.getId())
         {
             case R.id.activity_make_team_image_view_action_back:
                 actionBack();
                 break;

             case R.id.activity_forget_password_two_textView_get_code:
                 getCode();
                 break;

             case R.id.activity_forget_password_two_bootStarp_button_sure:
                 sure();
                 break;
         }
    }

    private void actionBack() {
        this.finish();
    }

    private void getCode() {
        Map<String,String> map=new HashMap<>();
        map.put("number",phoneNumber);
        JSONObject jsonObject=new JSONObject(map);
        JsonObjectRequest getCodeRequest=new JsonObjectRequest(Request.Method.POST, getCodeURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    String messsage=jsonObject.getString("message");
                    if(code==88)
                    {
                        getCode.setClickable(false);
                        getCode.setTextColor(getResources().getColor(R.color.verifyCodeColor));
                        startCount();
                    }else
                    {
                        Toast.makeText(getApplicationContext(),messsage,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ForgetPasswordTwoActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(getCodeRequest);

    }



    private void sure() {

        startActivity(new Intent(this,ForgetPasswordThreeActivity.class).putExtra("code",code.getText().toString()).putExtra("phoneNumber",phoneNumber));
    }

    private void startCount()
    {
        count=59;
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        getCode.setText(count+"s后再次获取");
                        if(count==0)
                        {
                            getCode.setText("获取验证码");
                            getCode.setTextColor(getResources().getColor(R.color.mycolor));
                            getCode.setClickable(true);
                            timer.cancel();

                        }
                        count--;
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);
    }
}
