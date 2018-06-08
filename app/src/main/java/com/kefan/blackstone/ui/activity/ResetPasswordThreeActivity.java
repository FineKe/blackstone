package com.kefan.blackstone.ui.activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.kefan.blackstone.ActivityUtil.ActivityCollector;
import com.kefan.blackstone.ActivityUtil.BaseActivity;
import com.kefan.blackstone.R;

import JavaBean.APIManager;

public class ResetPasswordThreeActivity extends BaseActivity implements View.OnClickListener{
    private String ResetPasswordURL= APIManager.BASE_URL +"v1/user/pwd";
    private RequestQueue requestQueue;
    private JsonObjectRequest resetPasswordRequest;
    private LinearLayout actionBack;
    private ImageView showPassword;

    private EditText inputPassword;

    private TextView message;

    private BootstrapButton completed;

    private Boolean isShowed=false;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password_three);

        initViews();
        initEvents();
    }

    private void initViews() {
        actionBack= (LinearLayout) findViewById(R.id.activity_reset_password_three_linear_layout_action_back);
        showPassword= (ImageView) findViewById(R.id.activity_reset_password_three_image_view_show_password);

        inputPassword= (EditText) findViewById(R.id.activity_reset_password_three_edit_text_input_password);

        message= (TextView) findViewById(R.id.activity_forget_password_three_text_view_message);

        completed= (BootstrapButton) findViewById(R.id.activity_reset_password_three_boot_strap_button_completed);
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
            case R.id.activity_reset_password_three_linear_layout_action_back:
                actionBack();
                break;

            case R.id.activity_reset_password_three_image_view_show_password:
                showPassword();
                break;

            case R.id.activity_reset_password_three_boot_strap_button_completed:
                completed();
                break;
        }
    }

    private void actionBack() {
        this.finish();
      //  overridePendingTransition(R.anim.in,R.anim.out);

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
        if(inputPassword.getText().length()<6||inputPassword.getText().length()>16)
        {
            message.setText("密码为6~16位字母、数字或符号");
        }
        else
        {
            resetPassword();
        }
    }

    private void resetPassword()
    {   userInformationSharedPreferences=getSharedPreferences(userInformation,MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();
        String oldPassword=userInformationSharedPreferences.getString("password","");
        final String token=userInformationSharedPreferences.getString("token","");
        requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("originPwd",oldPassword);
            jsonObject.put("newPwd",inputPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resetPasswordRequest=new JsonObjectRequest(Request.Method.POST, ResetPasswordURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    int code=jsonObject.getInt("code");
                    if(code==88)
                    {
                        Toast.makeText(ResetPasswordThreeActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                        userInformationEditor.putString("password",inputPassword.getText().toString()).apply();
                        ActivityCollector.finishAll();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ResetPasswordThreeActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap();
                headers.put("token",token);
                return headers;
            }
        };

        requestQueue.add(resetPasswordRequest);
    }
}
