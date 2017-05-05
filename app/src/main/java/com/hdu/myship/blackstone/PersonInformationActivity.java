package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PersonInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_person_information);
    }
}
