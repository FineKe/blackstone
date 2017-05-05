package com.hdu.myship.blackstone;

import android.app.Dialog;
import android.app.backup.RestoreObserver;
import android.content.Intent;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.makeramen.roundedimageview.RoundedImageView;

import ShapeUtil.GlideRoundTransform;

import static com.hdu.myship.blackstone.R.layout.login_dialog;

public class PersonCenterActivity extends AppCompatActivity implements View.OnClickListener{
    private RoundedImageView roundedImageView;
    private BootstrapButton button_login;
    private BootstrapButton button_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_person_center);

        initView();
        initEvents();
    }

    private void initEvents() {
    }

    private void initView() {

    }

    private void showLoginDialog()
    {
        Dialog dialog=new Dialog(this);
        View v=View.inflate(this,R.layout.login_dialog,null);
        dialog.setContentView(v);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

        }
    }
}
