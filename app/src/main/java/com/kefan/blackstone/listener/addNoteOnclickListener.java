package com.kefan.blackstone.listener;

import android.view.View;

import com.kefan.blackstone.database.NoteTemplate;

public interface addNoteOnclickListener {
        
        public void onClick(View view, int groupPos, int childPos, NoteTemplate noteTemplate);
        
    }