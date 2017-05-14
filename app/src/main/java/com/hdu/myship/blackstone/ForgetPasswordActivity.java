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

import com.beardedhen.androidbootstrap.BootstrapButton;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView messagePhone;
    private TextView message;

    private EditText phone;

    private ImageView actionBack;

    private BootstrapButton nextStep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forget_password);
        initViews();
        initEvents();
    }

    private void initViews() {
        messagePhone= (TextView) findViewById(R.id.activity_forget_password_two_textView_phoneMessage);
        message= (TextView) findViewById(R.id.activity_forget_password_two_textView_message);

        phone= (EditText) findViewById(R.id.activity_forget_password_two_edit_text_code);

        actionBack= (ImageView) findViewById(R.id.activity_forget_password_two_imageView_actionBack);

        nextStep= (BootstrapButton) findViewById(R.id.activity_forget_Password_boot_strap_button_next_step);

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
            case R.id.activity_forget_password_two_imageView_actionBack:
                actionBack();
                break;

            case R.id.activity_forget_Password_boot_strap_button_next_step:
                nextStep();
                break;
        }
    }

    private void actionBack() {
        this.finish();
    }

    private void nextStep() {
        startActivity(new Intent(this,ForgetPasswordTwoActivity.class).putExtra("phoneNumber",phone.getText().toString()));
    }
}
