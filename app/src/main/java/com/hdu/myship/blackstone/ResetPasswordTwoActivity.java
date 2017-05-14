package com.hdu.myship.blackstone;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ResetPasswordTwoActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView messagePhone;
    private TextView message;

    private EditText phone;

    private ImageView actionBack;

    private BootstrapButton sure;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password_two);
        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        userInformationSharedPreferences=getSharedPreferences(userInformation,MODE_PRIVATE);
        userInformationEditor=userInformationSharedPreferences.edit();
    }


    private void initViews() {
        messagePhone= (TextView) findViewById(R.id.activity_forget_password_two_textView_phoneMessage);
        message= (TextView) findViewById(R.id.activity_forget_password_two_textView_message);

        phone= (EditText) findViewById(R.id.activity_forget_password_two_edit_text_code);//密码

        actionBack= (ImageView) findViewById(R.id.activity_forget_password_two_imageView_actionBack);

        sure= (BootstrapButton) findViewById(R.id.activity_resetPasswordTwo_boot_strap_button_sue);

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
        sure.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_forget_password_two_imageView_actionBack:actionBack();
                break;

            case R.id.activity_resetPasswordTwo_boot_strap_button_sue:sure();
                break;
        }
    }


    private void clean() {

    }

    private void actionBack() {
        this.finish();
    }

    private void sure() {
        if(!phone.getText().toString().equals(userInformationSharedPreferences.getString("password","")))
        {
            message.setText("密码有误，请重新输入");
        }
    }
}
