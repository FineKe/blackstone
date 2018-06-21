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
import com.kefan.blackstone.database.AlterRecord;
import com.kefan.blackstone.database.NoteTemplate;

import java.util.List;


public class AlterNotesActivity extends BaseActivity implements View.OnClickListener{

    private String TAG="AlterNotesActivity";
    private EditText notes;
    private LinearLayout actionBack;


    //引用上个activity noteTemplate
    private NoteTemplate noteTemplate=RecordAlterActivity.noteTemplate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alter_notes);
        initView();
        initEvents();
    }



    private void initView() {
        notes= (EditText) findViewById(R.id.activity_alter_notes_editText_notes);
        actionBack= (LinearLayout) findViewById(R.id.activity_alter_notes_linear_layout_action_back);

        //设置已经有的笔记
        notes.setText(noteTemplate.getRemark());

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
            //重新设置笔记
            noteTemplate.setRemark(notesContent);
            this.finish();
        }else
        {
            this.finish();
        }
    }

    /**
     * 重写该方法，用户通过物理键返回，也保存笔记
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
        {
            actionBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}
