package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class SuggestionsActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton actionBack;

    private TextView send;

    private EditText write;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_suggestions);

        initViews();
        initEvents();
    }

    private void initViews() {
        actionBack= (ImageButton) findViewById(R.id.activity_suggestion_image_button_action_back);
        send= (TextView) findViewById(R.id.activity_suggestion_text_view_send);
        write= (EditText) findViewById(R.id.activity_suggestion_edit_text_write_suggestions);
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_suggestion_image_button_action_back:
                actionBack();
                break;

            case R.id.activity_suggestion_text_view_send:
                send();
                break;
        }
    }

    private void actionBack() {
        this.finish();
        overridePendingTransition(R.anim.in,R.anim.out);
    }

    private void send() {
    }
}
