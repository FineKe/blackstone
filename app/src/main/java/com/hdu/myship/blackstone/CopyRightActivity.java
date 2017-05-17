package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CopyRightActivity extends AppCompatActivity {

    private ImageView actionBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_copy_right);
        actionBack= (ImageView) findViewById(R.id.activity_make_team_image_view_action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in,R.anim.out);
            }
        });
    }
}
