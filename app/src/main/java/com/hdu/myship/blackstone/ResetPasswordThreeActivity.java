package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ResetPasswordThreeActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView actionBack;
    private ImageView showPassword;

    private EditText inputPassword;

    private TextView message;

    private BootstrapButton completed;

    private Boolean isShowed=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_reset_password_three);

        initViews();
        initEvents();
    }

    private void initViews() {
        actionBack= (ImageView) findViewById(R.id.activity_make_team_image_view_action_back);
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
            case R.id.activity_make_team_image_view_action_back:
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
        overridePendingTransition(R.anim.in,R.anim.out);
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
    }
}
