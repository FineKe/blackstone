package com.hdu.myship.blackstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.litepal.crud.DataSupport;

import java.util.List;

import database.Record;

public class AddNotesActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG="AddNotesActivity";

    private EditText notes;

    private ImageButton actionBack;
    private int speciesId;

    private List<Record> records;
    private Record record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        speciesId=getIntent().getIntExtra("speciesId",1);
        setContentView(R.layout.activity_add_notes);
        initData();
        initView();
        initEvents();
    }

    private void initData() {
        records= DataSupport.where("speciesId=?",speciesId+"").find(Record.class);
        record=records.get(0);

    }

    private void initView() {
        notes= (EditText) findViewById(R.id.add_notes_editText_notes);
        actionBack= (ImageButton) findViewById(R.id.activity_suggestion_image_button_action_back);
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_notes_editText_notes:
                break;

            case R.id.activity_suggestion_image_button_action_back:actionBack();
                break;
        }
    }

    private void actionBack() {
        String notesContent=notes.getText().toString();
        Log.d(TAG, "actionBack: "+notesContent);
        if(!notesContent.equals(""))
        {
            record.setRemarkIsNull(false);
            record.setRemark(notesContent);
            record.save();
            this.finish();
        }else
        {
            this.finish();
        }
    }
}
