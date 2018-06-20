package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.SpeciesConstant;
import com.kefan.blackstone.database.Note;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.service.RecordService;
import com.kefan.blackstone.service.impl.RecordServiceImpl;
import com.kefan.blackstone.ui.adapter.RecordDeatailedAdapter;
import com.kefan.blackstone.widget.HeaderBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyRecordTwoActivity extends BaseActivity{

    private static final int LOAD_DATA_COMPLETE=1;

    private String getRecordDeatailedURL = APIManager.BASE_URL + "v1/record/";
    private RequestQueue requestQueue;
    private JsonObjectRequest getRcordDeatailedRquest;
    private String TAG = "MyRecordTwoActivity";

    public static List<List<Note>> noteList;
    private List<String> father;

    private RecordDeatailedAdapter recordDeatailedAdapter;

    @BindView(R.id.headerbar_record_activity)
    HeaderBar headerbar;

    @BindView(R.id.activity_myrecord_two_expand_list_view)
    ExpandableListView expandListView;

    @BindView(R.id.activity_myrecord_two_text_view_date)
    TextView date;

    /**
     * 记录id
     */
    private Long recordId;

    private RecordService recordService;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_my_record_two;
    }
    @Override
    public void initData() {

        recordId=getIntent().getLongExtra("recordId",0l);
        recordService=new RecordServiceImpl();

        father=new ArrayList<>();
        noteList=new ArrayList<>();

        recordDeatailedAdapter=new RecordDeatailedAdapter(this,father,noteList);

        loadNotes();
    }

    @Override
    public void initView() {

        expandListView.setAdapter(recordDeatailedAdapter);
        expandListView.setGroupIndicator(null);

    }

    @Override
    public void initEvent() {

        headerbar.getLeftPart().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        headerbar.getRightTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_DATA_COMPLETE:

                    recordDeatailedAdapter.notifyDataSetChanged();

                    break;
            }
        }
    };

    private void loadNotes() {

        List<String> species=new ArrayList<>();
        List<List<Note>> speciesNotes=new ArrayList<>();
        List<Note> bird=new ArrayList<>();
        List<Note> amphibia=new ArrayList<>();
        List<Note> insect=new ArrayList<>();
        List<Note> reptiles=new ArrayList<>();

        Record record = recordService.findRecordById(recordId);
        List<Note> notes = record.getNotes();
        for (Note note : notes) {

            switch (note.getSpeciesType()) {

                case SpeciesConstant.BIRD:
                    bird.add(note);
                    break;

                case SpeciesConstant.AMPHIBIA:
                    amphibia.add(note);
                    break;

                case SpeciesConstant.INSECT:
                    insect.add(note);
                    break;

                case SpeciesConstant.REPTILES:
                    reptiles.add(note);
                    break;
            }

        }

        if (bird.size() > 0) {
            speciesNotes.add(bird);
            species.add("鸟类");
        }
        if (amphibia.size() > 0) {
            speciesNotes.add(amphibia);
            species.add("两栖类");
        }
        if (insect.size() > 0) {
            speciesNotes.add(insect);
            species.add("昆虫");
        }
        if (reptiles.size() > 0) {
            speciesNotes.add(reptiles);
            species.add("爬行类");
        }

        this.father.clear();
        this.noteList.clear();

        father.addAll(species);
        noteList.addAll(speciesNotes);

        handler.sendEmptyMessage(LOAD_DATA_COMPLETE);

    }


    private void alter() {
        Intent intent = new Intent(this, RecordAlterActivity.class);
        intent.putExtra("time", getIntent().getLongExtra("time", 0));
        intent.putExtra("recordId", recordId);
        startActivityForResult(intent, 1);
    }

}
