package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ResetPhoneActivity extends AppCompatActivity implements View.OnClickListener{
    private String getCodeURL="http://api.blackstone.ebirdnote.cn/v1/user/verifyCode/changeMobile";
    private String submitURL="http://api.blackstone.ebirdnote.cn/v1/user/changeMobile";
    private RequestQueue requestQueue;
    private JsonObjectRequest getCodeRequest;
    private JsonObjectRequest submitRquest;
    private UserInformationUtil userInformation;
    private TextView phone;
    private BootstrapButton update;
    private LinearLayout actionBack;
    private String phoneNumber;
    private String phoneCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_phone);

        initData();
        initView();
        initEvents();
    }

    private void initData() {
        userInformation=new UserInformationUtil(this);
        phoneNumber=userInformation.getUserName();
        phoneCode=phoneNumber.substring(0,3)+"****"+phoneNumber.substring(7);
        requestQueue= Volley.newRequestQueue(this);

    }

    private void initView() {
        phone= (TextView) findViewById(R.id.activity_reset_phone_text_view_phone);

        update= (BootstrapButton) findViewById(R.id.activity_reset_phone_boot_strap_button_update_phone_number);

        actionBack= (LinearLayout) findViewById(R.id.activity_reset_phone_linear_layout_action_back);

        phone.setText(phoneCode);
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_reset_phone_linear_layout_action_back:
                actionBack();
                break;

            case R.id.activity_reset_phone_boot_strap_button_update_phone_number:
                update();
                break;
        }
    }

    private void actionBack() {
        this.finish();
      //  overridePendingTransition(R.anim.in,R.anim.out);
    }

    private void update() {
        showUpdateDialog();
    }

    private void showUpdateDialog() {
        final updatePhoneNumberDialog updatePhoneNumberDialog=new updatePhoneNumberDialog(this,R.style.LoginDialog,R.layout.update_phone_number_dialog);
        updatePhoneNumberDialog.setCancelable(false);
        updatePhoneNumberDialog.show();

        ImageView actionCancel= (ImageView) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_dialog_imageView_actionCancel);
        final EditText inputPhone= (EditText) updatePhoneNumberDialog.findViewById(R.id.update_phone_numbe_dialog_editText_account);
        final EditText inputCode= (EditText) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_dialog_editText_code);
        final TextView getCode= (TextView) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_text_view_get_code);
        TextView Ok= (TextView) updatePhoneNumberDialog.findViewById(R.id.update_phone_number_dialog_textView_Ok);

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhoneNumberDialog.dismiss();
            }
        });

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPhone.getText().toString().length()!=11)
                {
                    Toast.makeText(ResetPhoneActivity.this, "请输入有效的11位手机号码", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("number",inputPhone.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getCodeRequest=new JsonObjectRequest(Request.Method.POST, getCodeURL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                int code=jsonObject.getInt("code");
                                if(code==88)
                                {
                                    Toast.makeText(ResetPhoneActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String message=jsonObject.getString("message");
                                    Toast.makeText(ResetPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> headers=new HashMap<String, String>();
                            headers.put("token",userInformation.getToken());
                            return headers;
                        }
                    };
                    requestQueue.add(getCodeRequest);
                }
            }

        });

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputCode.getText().toString().length()!=6)
                {
                    Toast.makeText(ResetPhoneActivity.this, "请输入有效的验证码", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("mobile",inputPhone.getText().toString());
                        jsonObject.put("verifyCode",inputCode.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    submitRquest=new JsonObjectRequest(Request.Method.POST, submitURL, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                int code=jsonObject.getInt("code");
                                if(code==88)
                                {
                                    updatePhoneNumberDialog.dismiss();
                                    userInformation.setUserName(inputPhone.getText().toString());
                                    finish();
                                    phone.setText(inputPhone.getText().toString().substring(0,3)+"****"+inputPhone.getText().toString().substring(7));
                                }
                                else
                                {
                                    String message=jsonObject.getString("message");
                                    Toast.makeText(ResetPhoneActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(ResetPhoneActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> headers=new HashMap<String, String>();
                            headers.put("token",userInformation.getToken());
                            return headers;
                        }
                    };
                    requestQueue.add(submitRquest);
                }

            }
        });
    }

    public class updatePhoneNumberDialog extends Dialog {
        private Context context;
        private int resId;
        public updatePhoneNumberDialog(Context context, int resLayout) {
            this(context,0,0);
        }
        public updatePhoneNumberDialog(Context context, int themeResId, int resLayout) {
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
