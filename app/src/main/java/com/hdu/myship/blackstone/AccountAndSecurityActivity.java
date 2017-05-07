package com.hdu.myship.blackstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class AccountAndSecurityActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout tab_personInformation;
    private LinearLayout tab_resetPassword;
    private LinearLayout tab_resetPhone;

    private ImageButton actionBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_account_and_security);
        initViews();
        initEvents();
    }

    private void initViews() {
        tab_personInformation= (LinearLayout) findViewById(R.id.account_security_linearlayout_person_information);
        tab_resetPassword= (LinearLayout) findViewById(R.id.account_security_linearlayout_reset_password);
        tab_resetPhone= (LinearLayout) findViewById(R.id.account_security_linearlayout_reset_phone);

        actionBack= (ImageButton) findViewById(R.id.account_security_imageButton_action_back);
    }

    private void initEvents() {
        tab_personInformation.setOnClickListener(this);
        tab_resetPassword.setOnClickListener(this);
        tab_resetPhone.setOnClickListener(this);

        actionBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.account_security_linearlayout_person_information:
                startActivity(new Intent(this,PersonInformationActivity.class));
                break;

            case R.id.account_security_linearlayout_reset_password:
                break;

            case R.id.account_security_linearlayout_reset_phone:
                startActivity(new Intent(this,ResetPhoneActivity.class));
                break;

            case R.id.account_security_imageButton_action_back:
                startActivity(new Intent(this,ResetPasswordActivity.class));
                break;
        }
    }
}
