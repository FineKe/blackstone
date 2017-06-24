package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MakeTeamActivity extends AppCompatActivity {

    private LinearLayout actionBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_make_team);
        actionBack= (LinearLayout) findViewById(R.id.activity_make_team_linear_layout_action_back);
        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
             //   overridePendingTransition(R.anim.in,R.anim.out);
            }
        });
    }
}
