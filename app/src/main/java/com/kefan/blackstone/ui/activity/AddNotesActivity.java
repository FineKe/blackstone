package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kefan.blackstone.BaseActivity;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.AddNoteCommon;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.ui.fragment.AddRecordFragment;

import java.util.List;



public class AddNotesActivity extends BaseActivity implements View.OnClickListener {

    private String TAG="AddNotesActivity";

    private EditText notes;

    private LinearLayout actionBack;
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

        groupPosition=getIntent().getIntExtra(AddNoteCommon.GROUP_POSITION,0);
        childPosition=getIntent().getIntExtra(AddNoteCommon.CHILD_POSITION,0);
        Log.d(TAG, "initData: "+groupPosition+":"+childPosition);
        data=new Intent();
    }

    private void initView() {
        notes= (EditText) findViewById(R.id.add_notes_editText_notes);
        actionBack= (LinearLayout) findViewById(R.id.activity_add_notes_linear_layout_action_back);
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

            case R.id.activity_add_notes_linear_layout_action_back:actionBack();
                break;
        }
    }



    private void actionBack() {

        String notesContent=notes.getText().toString();

        Log.d(TAG, "actionBack: "+notesContent);

        if(!notesContent.equals(""))
        {
            data.putExtra(AddNoteCommon.REMARK,notesContent);
            data.putExtra(AddNoteCommon.ISNULL,false);

        }else
        {
            data.putExtra(AddNoteCommon.ISNULL,true);
        }

        this.setResult(AddNoteCommon.REQUEST_START_ADD_NOTE_ACTIVITY_CODE,data);
        this.finish();
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
