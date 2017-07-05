package com.hdu.myship.blackstone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import ActivityUtil.BaseActivity;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener{

    private TextView messagePhone;
    private TextView message;

    private EditText phone;

    private LinearLayout actionBack;

    private BootstrapButton nextStep;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";
    private String phoneNumber;
    private String phoneCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password);
        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        userInformationSharedPreferences=getSharedPreferences(userInformation, Context.MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();
        phoneNumber=userInformationSharedPreferences.getString("mobile","");
        if(!phoneNumber.equals(""))
        {
            phoneCode=phoneNumber.substring(0,3)+"****"+phoneNumber.substring(7);
        }else
        {
            phoneCode="";
        }
    }

    private void initViews() {
        messagePhone= (TextView) findViewById(R.id.activity_forget_password_two_textView_phoneMessage);
        message= (TextView) findViewById(R.id.activity_forget_password_two_textView_message);

        phone= (EditText) findViewById(R.id.activity_forget_password_two_edit_text_code);

        actionBack= (LinearLayout) findViewById(R.id.activity_reset_password_linear_layout_action_back);

        nextStep= (BootstrapButton) findViewById(R.id.activity_resetPassword_boot_strap_button_next_step);

        messagePhone.setText("请输入"+phoneCode+"的完整手机号码，以验证身份");

        phone.addTextChangedListener(new TextWatcher() {
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
        nextStep.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_reset_password_linear_layout_action_back:
                actionBack();
                break;

            case R.id.activity_resetPassword_boot_strap_button_next_step:
                nextStep();
                break;
        }
    }


    private void actionBack() {
        this.finish();
       // overridePendingTransition(R.anim.in,R.anim.out);
    }

    private void nextStep() {
        phone.clearFocus();

        if(phoneNumber.equals(phone.getText().toString()))
        {
            startActivity(new Intent(this,ResetPasswordTwoActivity.class));
            //overridePendingTransition(R.anim.in,R.anim.out);
        }
        else
        {
            message.setText("手机号输入有误，请重新输入");
        }
    }

}
