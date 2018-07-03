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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.SpeciesConstant;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.database.Note;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.service.RecordService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.RecordServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.adapter.RecordDeatailedAdapter;
import com.kefan.blackstone.vo.RecordDetailedVO;
import com.kefan.blackstone.widget.HeaderBar;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyRecordTwoActivity extends BaseActivity {

    private static final int LOAD_DATA_COMPLETE = 1;

    public static final int LOAD_NET_DATA_COMPLETE = 2;
    private String TAG = "MyRecordTwoActivity";

    public static List<List<Note>> noteList;
    public static List<String> father;

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

    public static Record record;

    public RecordService recordService;

    public UserService userService;


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

        recordId = getIntent().getLongExtra("recordId", 0l);
        recordService = new RecordServiceImpl();
        userService = new UserServiceImpl();

        father = new ArrayList<>();
        noteList = new ArrayList<>();

        recordDeatailedAdapter = new RecordDeatailedAdapter(this, father, noteList);

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

                alter();

            }
        });


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_DATA_COMPLETE:

                    recordDeatailedAdapter.notifyDataSetChanged();

                    break;

                case LOAD_NET_DATA_COMPLETE:

                    loadNotes();

                    break;
            }
        }
    };

    private void loadNotes() {

        final List<String> species = new ArrayList<>();
        List<List<Note>> speciesNotes = new ArrayList<>();
        List<Note> bird = new ArrayList<>();
        List<Note> amphibia = new ArrayList<>();
        List<Note> insect = new ArrayList<>();
        List<Note> reptiles = new ArrayList<>();
        List<Note> mamal = new ArrayList<>();

        record = recordService.findRecordById(recordId);


        String chineseName = record.getNotes().get(0).getChineseName();

        //判断笔记数据是否已经填充，如果没有填充 则请求服务器
        if (chineseName == null || chineseName.length() <= 0) {

            recordService.getRecordDetailed(userService.getToken(), record.getNetId(), new BaseResponseListener<RecordDetailedVO>(RecordDetailedVO.class) {

                @Override
                protected void onSuccess(RecordDetailedVO data) {

                    record.setLat(data.getLat());
                    record.setLon(data.getLon());

                    List<RecordDetailedVO.NotesBean> notesBeans = data.getNotes();

                    DataSupport.deleteAll(Note.class, "record_id=?", "" + recordId);

                    //设置经纬度
                    record.setLon(data.getLon());
                    record.setLat(data.getLat());

                    List<Note> notes = new ArrayList<>();

                    for (RecordDetailedVO.NotesBean notesBean : notesBeans) {


                        RecordDetailedVO.NotesBean.SpeciesBean speciesBean = notesBean.getSpecies();
                        Note note = new Note(speciesBean.getId()
                                , notesBean.getId()
                                , notesBean.getRemark()
                                , speciesBean.getSpeciesType()
                                , speciesBean.getFamily() == null ? speciesBean.getChineseName() : speciesBean.getFamily()
                                , speciesBean.getChineseName());

                        note.save();

                        notes.add(note);

                    }

                    record.setNotes(notes);
                    record.save();

                    handler.sendEmptyMessage(LOAD_NET_DATA_COMPLETE);

                }

                @Override
                protected void onFailed(int code, String message) {

                    handler.sendEmptyMessage(LOAD_NET_DATA_COMPLETE);

                }
            }, new BaseErrorListener(getApplicationContext()) {
                @Override
                protected void onError(VolleyError volleyError) {

                }
            });

        } else {

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
                    case SpeciesConstant.MAMAL:
                        mamal.add(note);
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
            if (mamal.size() > 0) {
                speciesNotes.add(mamal);
                species.add("兽类");
            }

            this.father.clear();
            this.noteList.clear();

            father.addAll(species);
            noteList.addAll(speciesNotes);

            handler.sendEmptyMessage(LOAD_DATA_COMPLETE);
        }
    }


    private void alter() {
        Intent intent = new Intent(this, RecordAlterActivity.class);
        intent.putExtra("time", getIntent().getLongExtra("time", 0));
        intent.putExtra("recordId", recordId);
        startActivityForResult(intent, 1);
    }

}
