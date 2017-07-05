package com.hdu.myship.blackstone;

import android.content.SharedPreferences;
import android.media.DrmInitData;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonInformationActivity extends BaseActivity implements View.OnClickListener{

    private TextView name;
    private TextView id;
    private TextView gender;

    private LinearLayout actionBack;

    private SharedPreferences userInformationSharedPreferences;
    private SharedPreferences.Editor userInformationEditor;
    private String userInformation="UesrInformation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_person_information);

        initData();
        initViews();
        initEvents();
    }

    private void initData() {
        userInformationSharedPreferences=getSharedPreferences(userInformation,MODE_PRIVATE);

    }

    private void initViews() {
        name= (TextView) findViewById(R.id.person_information_textView_name);
        id= (TextView) findViewById(R.id.person_information_textView_id);
        gender= (TextView) findViewById(R.id.person_information_textView_gender);

        actionBack= (LinearLayout) findViewById(R.id.activity_person_information_linear_layout_action_back);

        name.setText(userInformationSharedPreferences.getString("name",""));
        id.setText(userInformationSharedPreferences.getString("studentId",""));
        gender.setText(userInformationSharedPreferences.getString("gender",""));
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_person_information_linear_layout_action_back:actionBack();
                break;
        }
    }

    private void actionBack() {
        this.finish();
       // overridePendingTransition(R.anim.in,R.anim.out);
    }
}
