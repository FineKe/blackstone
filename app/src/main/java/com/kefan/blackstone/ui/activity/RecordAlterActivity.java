package com.kefan.blackstone.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kefan.blackstone.BlackStoneApplication;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.SpeciesConstant;
import com.kefan.blackstone.data.req.AlterRecordReq;
import com.kefan.blackstone.data.req.RecordReq;
import com.kefan.blackstone.database.AlterRecord;
import com.kefan.blackstone.database.Note;
import com.kefan.blackstone.database.NoteTemplate;
import com.kefan.blackstone.database.Record;
import com.kefan.blackstone.listener.addNoteOnclickListener;
import com.kefan.blackstone.service.RecordService;
import com.kefan.blackstone.service.UserService;
import com.kefan.blackstone.service.impl.RecordServiceImpl;
import com.kefan.blackstone.service.impl.UserServiceImpl;
import com.kefan.blackstone.ui.adapter.AddRecordExpandAdapter;
import com.kefan.blackstone.widget.HeaderBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import ch.ielse.view.SwitchView;


public class RecordAlterActivity extends BaseActivity {

    public static final int LOAD_NODE_TEMPLATE_COMPLETE = 1;

    private String alterRecordURL = APIManager.BASE_URL + "v1/record/edit";
    private String TAG = "RecordAlterActivity";
    private static final int CREATE_OK = 1;

    public static List<List<AlterRecord>> recordList;

    private boolean datePickerShow = false;

    private Calendar calendar;

    private int year, month, day;

    private List<String> group;
    private List<List<Note>> noteList=MyRecordTwoActivity.noteList;
    private List<List<NoteTemplate>> noteTemplateList;

    public static NoteTemplate noteTemplate;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private AddRecordExpandAdapter adapter;


    @BindView(R.id.headerbar_alter_record_activity)
    HeaderBar headerbar;
    @BindView(R.id.tv_date_alter_record_activity)
    TextView date;
    @BindView(R.id.dp_date_alter_record_activity)
    DatePicker datePicker;
    @BindView(R.id.tv_upload_alter_record_activity)
    TextView upload;
    @BindView(R.id.sv_upload_alter_record_activity)
    SwitchView uploadSwitch;
    @BindView(R.id.expandListView_alter_record_activity)
    ExpandableListView expandListView;

    private Long recordId;

    private RecordService recordService;

    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

    }


    @Override
    protected int setLayout() {
        return R.layout.activity_record_alter;
    }


    @Override
    public void initData() {

        recordService=new RecordServiceImpl();
        userService=new UserServiceImpl();

        recordId=getIntent().getLongExtra("recordId",0l);
        group = new ArrayList<>();
        noteTemplateList = new ArrayList<>();
        adapter = new AddRecordExpandAdapter(group, noteTemplateList);

        createNoteTemplate();
    }

    @Override
    public void initView() {


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        date.setText(year + "年" + (month + 1) + "月" + day + "日");
        month++;

        expandListView.setAdapter(adapter);
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

        uploadSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                headerbar.getRightTextView().setText("发表");
                view.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                headerbar.getRightTextView().setText("保存");
                view.toggleSwitch(false);
            }
        });

        datePicker.init(year, month - 1, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year_, int monthOfYear, int dayOfMonth) {
                year = year_;
                month = monthOfYear + 1;
                day = dayOfMonth;
                date.setText(year + "年" + (month) + "月" + day + "日");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePickerShow == false) {
                    datePicker.setVisibility(View.VISIBLE);
                    datePickerShow = true;
                } else {
                    datePicker.setVisibility(View.GONE);
                    datePickerShow = false;
                }
            }
        });

        adapter.setAddNoteOnclickListener(new addNoteOnclickListener() {
            @Override
            public void onClick(View view, int groupPos, int childPos, NoteTemplate noteTemplate) {

                RecordAlterActivity.noteTemplate=noteTemplate;

                Intent intent=new Intent(RecordAlterActivity.this,AlterNotesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadRecord() {
        
    }

    private void createNoteTemplate() {

        executorService.submit(new Runnable() {
            @Override
            public void run() {


                Log.d(TAG, "createNoteTemplate: " + "本地获取");
                group.clear();
                noteTemplateList.clear();

                group.add("鸟类");
                group.add("昆虫");
                group.add("两栖类");
                group.add("爬行类");

                List<NoteTemplate> bird = DataSupport.where("speciesType=?", "bird").find(NoteTemplate.class);
                List<NoteTemplate> insect = DataSupport.where("speciesType=?", "insect").find(NoteTemplate.class);
                List<NoteTemplate> amphibia = DataSupport.where("speciesType=?", "amphibia").find(NoteTemplate.class);
                List<NoteTemplate> reptiles = DataSupport.where("speciesType=?", "reptiles").find(NoteTemplate.class);

                noteTemplateList.add(bird);
                noteTemplateList.add(insect);
                noteTemplateList.add(amphibia);
                noteTemplateList.add(reptiles);

                //将添加的笔记填充笔记模板中
                for (List<Note> notes : noteList) {

                    for (Note note : notes) {

                        switch (note.getSpeciesType()) {

                            case SpeciesConstant.BIRD:

                                for (NoteTemplate noteTemplate : bird) {
                                    if (note.getSpeciesId() == noteTemplate.getSpeciesId()) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;

                            case SpeciesConstant.AMPHIBIA:

                                for (NoteTemplate noteTemplate : amphibia) {
                                    if (note.getSpeciesId() == noteTemplate.getSpeciesId()) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;

                            case SpeciesConstant.INSECT:

                                for (NoteTemplate noteTemplate : insect) {
                                    if (note.getSpeciesId() == noteTemplate.getSpeciesId()) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;

                            case SpeciesConstant.REPTILES:

                                for (NoteTemplate noteTemplate : reptiles) {
                                    if (note.getSpeciesId() == noteTemplate.getSpeciesId()) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;
                        }
                    }
                }


                handler.sendEmptyMessage(LOAD_NODE_TEMPLATE_COMPLETE);
            }

        });
    }


    /**
     * 从笔记模板产生note 对象
     *
     * @param noteTemplate
     * @return
     */
    private Note copyFromTemplate(NoteTemplate noteTemplate) {

        return new Note(noteTemplate.getSpeciesId(),0l, noteTemplate.getRemark()
                , noteTemplate.getSpeciesType(), noteTemplate.getFamily(), noteTemplate.getSpeciesName());
    }


    /**
     * @param record
     * @return
     */
    private AlterRecordReq copyFromRecord(Record record) {

        return new AlterRecordReq(recordId,record.getTime(),record.getNotes());

    }


    /**
     * 获取时间
     *
     * @return
     */
    private Long getTime() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Long second = format.parse(year + "-" + month + "-" + day).getTime();

            return second;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0l;

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case LOAD_NODE_TEMPLATE_COMPLETE:

                    //刷新adapter
                    adapter.notifyDataSetChanged();

                    break;
            }
        }

    };
}
