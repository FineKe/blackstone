package com.hdu.myship.blackstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import database.AlterRecord;
import database.Record;

public class AlterNotesActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAG="AlterNotesActivity";
    private EditText notes;
    private LinearLayout actionBack;
    private int speciesId;
    private List<AlterRecord> records;
    private AlterRecord record;
    private Intent data;
    private int groupPosition;
    private int childPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alter_notes);
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
        notes= (EditText) findViewById(R.id.activity_alter_notes_editText_notes);
        actionBack= (LinearLayout) findViewById(R.id.activity_alter_notes_linear_layout_action_back);
        notes.setText(RecordAlterActivity.recordList.get(groupPosition).get(childPosition).getRemark());
        notes.setSelection(notes.getText().length());
    }

    private void initEvents() {
        actionBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.activity_alter_notes_editText_notes:
                break;

            case R.id.activity_alter_notes_linear_layout_action_back:actionBack();
                break;
        }
    }

    private void actionBack() {
        String notesContent=notes.getText().toString();
        Log.d(TAG, "actionBack: "+notesContent);
        if(!notesContent.equals(""))
        {
            RecordAlterActivity.recordList.get(groupPosition).get(childPosition).setRemark(notesContent);
            RecordAlterActivity.recordList.get(groupPosition).get(childPosition).setRemarkIsNull(false);
            // data.putExtra("Remark",notesContent);
            //data.putExtra("isNull",false);
            //this.setResult(2,data);
            this.finish();
        }else
        {
            RecordAlterActivity.recordList.get(groupPosition).get(childPosition).setRemark(notesContent);
            RecordAlterActivity.recordList.get(groupPosition).get(childPosition).setRemarkIsNull(true);
            //data.putExtra("isNull",true);
            //this.setResult(2,data);
            this.finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            actionBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
