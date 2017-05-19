package com.hdu.myship.blackstone;

import android.content.Intent;
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

    private Intent data;
    private int groupPosition;
    private int childPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_notes);
        initData();
        initView();
        initEvents();

    }

    private void initData() {

        groupPosition=getIntent().getIntExtra("groupPosition",0);
        childPosition=getIntent().getIntExtra("childPosition",0);
        Log.d(TAG, "initData: "+groupPosition+":"+childPosition);
        data=new Intent();
    }

    private void initView() {
        notes= (EditText) findViewById(R.id.add_notes_editText_notes);
        actionBack= (ImageButton) findViewById(R.id.activity_suggestion_image_button_action_back);
        notes.setText(MainActivity.records.get(groupPosition).get(childPosition).getRemark());
        notes.setSelection(notes.getText().length());
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
            MainActivity.records.get(groupPosition).get(childPosition).setRemark(notesContent);
            MainActivity.records.get(groupPosition).get(childPosition).setRemarkIsNull(false);
           // data.putExtra("Remark",notesContent);
            //data.putExtra("isNull",false);
            //this.setResult(2,data);
            this.finish();
        }else
        {
            MainActivity.records.get(groupPosition).get(childPosition).setRemark(notesContent);
            MainActivity.records.get(groupPosition).get(childPosition).setRemarkIsNull(true);
            //data.putExtra("isNull",true);
            //this.setResult(2,data);
            this.finish();
        }
    }
}
