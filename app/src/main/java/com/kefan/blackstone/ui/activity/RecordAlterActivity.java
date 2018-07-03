package com.kefan.blackstone.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.kefan.blackstone.JavaBean.APIManager;
import com.kefan.blackstone.R;
import com.kefan.blackstone.common.SpeciesConstant;
import com.kefan.blackstone.data.listener.BaseErrorListener;
import com.kefan.blackstone.data.listener.BaseResponseListener;
import com.kefan.blackstone.data.req.AlterRecordReq;
import com.kefan.blackstone.data.req.NoteReq;
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
import com.kefan.blackstone.util.ToastUtil;
import com.kefan.blackstone.widget.HeaderBar;

import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<List<Note>> noteList = MyRecordTwoActivity.noteList;
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

    private Record record;

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

        recordService = new RecordServiceImpl();
        userService = new UserServiceImpl();

        recordId = getIntent().getLongExtra("recordId", 0l);
        group = new ArrayList<>();
        noteTemplateList = new ArrayList<>();
        adapter = new AddRecordExpandAdapter(group, noteTemplateList);
        record = recordService.findRecordById(recordId);
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

        uploadSwitch.setOpened(record.getAddToObservedList());
        uploadSwitch.setClickable(false);

        if (record.getAddToObservedList()) {
            headerbar.getRightTextView().setText("发表");
        } else {
            headerbar.getRightTextView().setText("保存");
        }

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

                if (record.getAddToObservedList()) {
                    //上传并保存本地
                    updateRecord(record);
                    uploadRecord();
                } else {
                    //只保存到本地
                    updateRecord(record);
                }


            }
        });

        uploadSwitch.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                uploadSwitch.setOpened(record.getAddToObservedList());
            }

            @Override
            public void toggleToOff(SwitchView view) {
                uploadSwitch.setOpened(record.getAddToObservedList());
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

                RecordAlterActivity.noteTemplate = noteTemplate;

                Intent intent = new Intent(RecordAlterActivity.this, AlterNotesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadRecord() {

        AlterRecordReq alterRecordReq = createRecord();

        recordService.alterRecord(userService.getToken(), alterRecordReq, new BaseResponseListener<Object>(Object.class) {

            @Override
            protected void onSuccess(Object data) {
                ToastUtil.showToast(getApplicationContext(), "修改成功");

            }

            @Override
            protected void onFailed(int code, String message) {
                ToastUtil.showToast(getApplicationContext(), message);
            }


        }, new BaseErrorListener(getApplicationContext()) {
            @Override
            protected void onError(VolleyError volleyError) {

            }
        });
    }

    /**
     * 记录记录
     *
     * @return
     */
    private AlterRecordReq createRecord() {

        List<NoteReq> notes = new ArrayList<>();

        AlterRecordReq alterRecordReq = new AlterRecordReq();

        for (List<NoteTemplate> noteTemplates : noteTemplateList) {

            for (NoteTemplate noteTemplate : noteTemplates) {

                if (noteTemplate.isChekced()) {

                    NoteReq note = copyFromTemplate(noteTemplate);
                    notes.add(note);

                }

            }

        }

        if (!getTime().equals(record.getTime())) {
            alterRecordReq.setTime(getTime());
        }

        alterRecordReq.setTime(record.getTime());
        alterRecordReq.setNotes(notes);
        alterRecordReq.setId(record.getNetId());
        return alterRecordReq;
    }

    private void updateRecord(Record record) {

        record.setNotes(null);
        record.save();

        List<Note> notes = new ArrayList<>();


        for (List<NoteTemplate> noteTemplates : noteTemplateList) {

            for (NoteTemplate noteTemplate : noteTemplates) {

                if (noteTemplate.isChekced()) {

                    Note note = copyFromTemplat(noteTemplate);
                    note.save();
                    notes.add(note);

                }

            }

        }


        record.setNotes(notes);
        record.save();
    }

    /**
     * 从笔记模板产生note 对象
     *
     * @param noteTemplate
     * @return
     */
    private Note copyFromTemplat(NoteTemplate noteTemplate) {

        return new Note(noteTemplate.getSpeciesId(), 0l, noteTemplate.getRemark()
                , noteTemplate.getSpeciesType(), noteTemplate.getFamily(), noteTemplate.getSpeciesName());
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
                group.add("兽类");

                List<NoteTemplate> bird = DataSupport.where("speciesType=?", "bird").find(NoteTemplate.class);
                List<NoteTemplate> insect = DataSupport.where("speciesType=?", "insect").find(NoteTemplate.class);
                List<NoteTemplate> amphibia = DataSupport.where("speciesType=?", "amphibia").find(NoteTemplate.class);
                List<NoteTemplate> reptiles = DataSupport.where("speciesType=?", "reptiles").find(NoteTemplate.class);
                List<NoteTemplate> mamal = DataSupport.where("speciesType=?", "mamal").find(NoteTemplate.class);

                noteTemplateList.add(bird);
                noteTemplateList.add(insect);
                noteTemplateList.add(amphibia);
                noteTemplateList.add(reptiles);
                noteTemplateList.add(mamal);

                //将添加的笔记填充笔记模板中
                for (List<Note> notes : noteList) {

                    for (Note note : notes) {

                        switch (note.getSpeciesType()) {

                            case SpeciesConstant.BIRD:

                                for (NoteTemplate noteTemplate : bird) {
                                    if (note.getSpeciesId().equals( noteTemplate.getSpeciesId())) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;


                            case SpeciesConstant.INSECT:

                                for (NoteTemplate noteTemplate : insect) {
                                    if (note.getSpeciesId().equals( noteTemplate.getSpeciesId())) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;

                            case SpeciesConstant.AMPHIBIA:

                                for (NoteTemplate noteTemplate : amphibia) {
                                    if (note.getSpeciesId().equals( noteTemplate.getSpeciesId())) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;

                            case SpeciesConstant.REPTILES:

                                for (NoteTemplate noteTemplate : reptiles) {
                                    if (note.getSpeciesId().equals( noteTemplate.getSpeciesId())) {
                                        noteTemplate.setChekced(true);
                                        noteTemplate.setRemark(note.getRemark());
                                    }
                                }

                                break;

                            case SpeciesConstant.MAMAL:

                                for (NoteTemplate noteTemplate : mamal) {


                                    if (note.getSpeciesId().equals( noteTemplate.getSpeciesId())) {

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
    private NoteReq copyFromTemplate(NoteTemplate noteTemplate) {

        NoteReq note = new NoteReq();
        note.setRemark(noteTemplate.getRemark());
        note.setSpeciesId(noteTemplate.getSpeciesId());
        return note;
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
